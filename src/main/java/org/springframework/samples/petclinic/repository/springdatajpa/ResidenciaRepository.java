
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


}
