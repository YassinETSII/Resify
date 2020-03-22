package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Actividad;


public interface ActividadRepository extends  CrudRepository<Actividad, String>{
	
	Actividad findById(int id) throws DataAccessException;
	
	@Query("SELECT a FROM Actividad a WHERE a.residencia.manager.id =:id")
	Iterable<Actividad> findAllMine(@Param("id") int id) throws DataAccessException;
	
}
