
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

	@Query("SELECT r.nombre, case WHEN (COUNT(DISTINCT b.id) = 0) THEN 0 WHEN (COUNT(DISTINCT i.id) = 0) THEN COUNT(DISTINCT b.id) "
			+ "ELSE (COUNT(DISTINCT b.id)/COUNT(DISTINCT i.id)) END AS ratioAcciones FROM BuenaAccion b, Incidencia i JOIN "
			+ "Residencia r WHERE r.id = b.residencia.id AND r.id = i.residencia.id GROUP BY r.id ORDER BY ratioAcciones DESC")
	Iterable<Residencia> findTop() throws DataAccessException;

}
