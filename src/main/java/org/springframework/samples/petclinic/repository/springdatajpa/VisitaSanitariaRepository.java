
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.VisitaSanitaria;

public interface VisitaSanitariaRepository extends CrudRepository<VisitaSanitaria, String> {

	VisitaSanitaria findById(int id) throws DataAccessException;

	@Query("SELECT visita FROM VisitaSanitaria visita WHERE visita.residencia LIKE :residencia")
	Iterable<VisitaSanitaria> findAllMine(@Param("residencia") Residencia residencia) throws DataAccessException;

//	@Query("SELECT excursion FROM Excursion excursion WHERE excursion.finalMode = true AND excursion.fechaInicio > :today")
//	Iterable<Excursion> findAllPublishedAndFuture(@Param("today") Date today) throws DataAccessException;
//
//	@Query("SELECT excursion FROM Participacion p JOIN p.excursion excursion WHERE  p.anciano.id =:id AND excursion.finalMode = true AND excursion.fechaInicio > :today")
//	Iterable<Excursion> findAllMineAnciano(@Param("id") int id, @Param("today") Date today);

}