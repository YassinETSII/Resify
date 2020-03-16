package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.PeticionExcursion;


public interface PeticionExcursionRepository extends  CrudRepository<PeticionExcursion, String>{
	
	PeticionExcursion findById(int id) throws DataAccessException;
	
}
