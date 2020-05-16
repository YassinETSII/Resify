
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Queja;

public interface QuejaRepository extends CrudRepository<Queja, String> {

	Queja findById(int id) throws DataAccessException;

	@Query("SELECT q FROM Queja q WHERE q.anciano.id IN (SELECT i.anciano.id FROM Inscripcion i WHERE i.estado = 'aceptada' AND i.residencia.manager.id = :id) ORDER BY q.fecha desc")
	Iterable<Queja> findQuejasByManagerId(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT count(q.id) FROM Queja q WHERE q.anciano.id = :id AND q.fecha> :fecha")
	Double countQuejasByTiempoYAncianoId(@Param("id") int id, @Param("fecha") Date fecha) throws DataAccessException;

	@Query("SELECT count(q) FROM Queja q GROUP BY q.anciano")
	Iterable<Long> countQuejasByAnciano() throws DataAccessException;

	@Query("SELECT count(q) FROM Queja q, Inscripcion i WHERE q.anciano = i.anciano AND i.estado = 'aceptada' GROUP BY i.residencia")
	Iterable<Long> countQuejasByResidencia();
}
