
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Manager;

public interface ManagerRepository extends CrudRepository<Manager, String> {

	Manager findById(int id) throws DataAccessException;

	@Query("SELECT manager FROM Manager manager WHERE manager.user.username LIKE :username%")
	Manager findByUsername(@Param("username") String username) throws DataAccessException;
}
