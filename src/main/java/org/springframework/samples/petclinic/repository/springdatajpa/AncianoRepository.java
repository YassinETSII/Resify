
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Anciano;

public interface AncianoRepository extends CrudRepository<Anciano, String> {

	Anciano findById(int id) throws DataAccessException;

	@Query("SELECT anciano FROM Anciano anciano WHERE anciano.user.username LIKE :username%")
	Anciano findByUsername(@Param("username") String username) throws DataAccessException;
}
