package org.springframework.samples.petclinic.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Feedback;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.repository.springdatajpa.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private ExcursionService excursionService;


	@Transactional
	public Feedback findFeedbackById(int id) throws DataAccessException {
		return feedbackRepository.findById(id);
	}

	/*
	@Transactional
	public Iterable<Feedback> findAllMineResidencia(Residencia residencia) throws DataAccessException {
		return feedbackRepository.findByResidencia(residencia.getId(), java.sql.Date.valueOf(LocalDate.now()));
	}*/

	@Transactional
	public Iterable<Feedback> findAllMineOrganizador(Organizador organizador) throws DataAccessException {
		return feedbackRepository.findByOrganizador(organizador.getId());
	}

	@Transactional
	public Double countFeedbackByExcursion(Excursion excursion) throws DataAccessException {
		return feedbackRepository.findByExcursionAceptada(excursion.getId());
	}

	@Transactional
	public void save(Feedback feedback) throws DataAccessException {
		feedbackRepository.save(feedback);
	}

	@Transactional(readOnly = true)
	public int countFeedbacksByExcursion(Excursion excursion, Manager manager) throws DataAccessException {
		return feedbackRepository.countFeedbacksByExcursion(excursion, manager);
	}

	@Transactional
	public Long countFeedbacks() {
		return this.feedbackRepository.count();
	}

	@Transactional
	public Double avgFeedbacksByExcursion() {
		Double res = 0.;
		if (!this.excursionService.countExcursiones().equals(0L)) {
			res = Double.valueOf(this.feedbackRepository.count())/this.excursionService.countExcursiones().doubleValue();
		}
		return res;
	}

}