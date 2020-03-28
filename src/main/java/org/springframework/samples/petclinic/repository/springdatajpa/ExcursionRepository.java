package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Excursion;


public interface ExcursionRepository extends  CrudRepository<Excursion, String>{
	
	Excursion findById(int id) throws DataAccessException;
	
	@Query("SELECT excursion FROM Excursion excursion WHERE excursion.organizador.id =:id")
	Iterable<Excursion> findAllMine(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT excursion FROM Excursion excursion WHERE excursion.finalMode = true AND excursion.fechaInicio > :today")
	Iterable<Excursion> findAllPublished(@Param("today") LocalDate today) throws DataAccessException;
	
}
