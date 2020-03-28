
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Residencia;

public interface ResidenciaRepository extends CrudRepository<Residencia, String> {

	Residencia findById(int id) throws DataAccessException;

	@Query("SELECT residencia FROM Residencia residencia WHERE residencia.manager.id =:id")
	Iterable<Residencia> findAllMine(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT count(buenaAccion.id) FROM BuenaAccion buenaAccion WHERE buenaAccion.residencia.id =:id")
	Double countBuenasAccionesByResidenciaId(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT count(incidencia.id) FROM Incidencia incidencia WHERE incidencia.residencia.id =:id")
	Double countIncidenciasByResidenciaId(@Param("id") int id) throws DataAccessException;


	//	@Query("SELECT r, CASE WHEN (COUNT(DISTINCT b.id) = 0) THEN 0 WHEN (COUNT(DISTINCT i.id) = 0) THEN COUNT(DISTINCT b.id) "
	//			+ "ELSE (COUNT(DISTINCT b.id)/COUNT(DISTINCT i.id)) END AS ratioAcciones FROM BuenaAccion b, Incidencia i JOIN "
	//			+ "Residencia r WHERE (r.id = b.residencia.id AND r.id = i.residencia.id) GROUP BY r.id ORDER BY ratioAcciones DESC")
	//	@Query("SELECT r, COUNT(DISTINCT b.id) AS ratioAcciones FROM BuenaAccion b join "
	//			+ "Residencia r where r.id = b.residencia.id group by r.id")
	//	@Query("SELECT r, COUNT(b) FROM BuenaAccion b JOIN b.residencia r GROUP BY r.id ORDER BY COUNT(b) desc")
	@Query("SELECT r, CASE WHEN (COUNT(DISTINCT b.id) = 0) THEN 0 WHEN (COUNT(DISTINCT i.id) = 0) THEN COUNT(DISTINCT b.id) ELSE (COUNT(DISTINCT b)/COUNT(DISTINCT i)) END AS ratioAcciones FROM BuenaAccion b, Incidencia i JOIN b.residencia r WHERE r.id = i.residencia.id GROUP BY r.id ORDER BY ratioAcciones desc")
	Iterable<Object[]> findTop() throws DataAccessException;

	//	SELECT residencias.nombre, CASE WHEN (COUNT(DISTINCT buenas_acciones.id) = 0) THEN 0 WHEN (COUNT(DISTINCT incidencias.id) = 0)
	//	THEN COUNT(DISTINCT buenas_Acciones.id) ELSE (COUNT(DISTINCT buenas_acciones.id)/COUNT(DISTINCT incidencias.id)) END AS
	//	ratioAcciones FROM Buenas_Acciones, Incidencias JOIN Residencias WHERE residencias.id = buenas_acciones.residencia_id AND
	//	residencias.id = incidencias.residencia_id GROUP BY residencias.id ORDER BY ratioAcciones DESC
}
