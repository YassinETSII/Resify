
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Excursion;

public interface ExcursionRepository extends CrudRepository<Excursion, String> {

	Excursion findById(int id) throws DataAccessException;

	@Query("SELECT excursion FROM Excursion excursion WHERE excursion.organizador.id =:id")
	Iterable<Excursion> findAllMine(@Param("id") int id) throws DataAccessException;

	@Query("SELECT excursion FROM Excursion excursion WHERE excursion.finalMode = true AND excursion.fechaInicio > :today")
	Iterable<Excursion> findAllPublishedAndFuture(@Param("today") Date today) throws DataAccessException;

	@Query("SELECT excursion FROM Participacion p JOIN p.excursion excursion WHERE  p.anciano.id =:id AND excursion.finalMode = true AND excursion.fechaInicio > :today")
	Iterable<Excursion> findAllMineAnciano(@Param("id") int id, @Param("today") Date today);
	
	@Query("SELECT e FROM PeticionExcursion p JOIN p.excursion e WHERE p.residencia.id =:id AND p.estado = 'aceptada' AND e.finalMode = true AND e.fechaFin <= :today")
	Iterable<Excursion> findAllFinishedResidencia(@Param("id") int id, @Param("today") Date today);

	@Query("SELECT COUNT(e) FROM Excursion e GROUP BY e.organizador")
	Iterable<Long> countExcursionesByOrganizador() throws DataAccessException;
}
