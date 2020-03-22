
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;

public interface ManagerRepository extends CrudRepository<Manager, String> {

	Manager findById(int id) throws DataAccessException;

	@Query("SELECT residencia FROM Residencia residencia WHERE residencia.manager.user.username LIKE :username%")
	Residencia findResidenciaByManagerUsername(@Param("username") String username) throws DataAccessException;

	@Query("SELECT manager FROM Manager manager WHERE manager.user.username LIKE :username%")
	Manager findByUsername(@Param("username") String username) throws DataAccessException;

	@Query("SELECT count(peticiones.id) FROM PeticionExcursion peticiones WHERE peticiones.excursion LIKE :excursion AND peticiones.residencia.manager LIKE :manager ")
	Integer countPeticionesByExcursion(@Param("excursion") Excursion excursion, @Param("manager") Manager manager)
			throws DataAccessException;

}
