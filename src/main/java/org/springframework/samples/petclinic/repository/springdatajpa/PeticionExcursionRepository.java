package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.PeticionExcursion;

public interface PeticionExcursionRepository extends CrudRepository<PeticionExcursion, String> {

	PeticionExcursion findById(int id) throws DataAccessException;
	
	@Query("SELECT peticion FROM PeticionExcursion peticion WHERE peticion.residencia.id =:id")
	Iterable<PeticionExcursion> findByResidencia(@Param("id") int id) throws DataAccessException;

	@Query("SELECT peticion FROM PeticionExcursion peticion WHERE peticion.excursion.organizador.id =:id AND peticion.estado = 'pendiente'")
	Iterable<PeticionExcursion> findByOrganizador(@Param("id") int id) throws DataAccessException;


}