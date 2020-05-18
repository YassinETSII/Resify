/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.springdatajpa.ActividadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ActividadService {

	@Autowired
	private ActividadRepository actividadRepository;	
	
	@Autowired
	private ResidenciaService residenciaService;	
	
//	@Autowired
//	private UserService userService;
//	
//	@Autowired
//	private AuthoritiesService authoritiesService;

//	@Autowired
//	public ActividadService(ActividadRepository actividadRepository) {
//		this.actividadRepository = actividadRepository;
//	}	

	@Transactional(readOnly = true)
	public Actividad findActividadById(int id) throws DataAccessException {
		return actividadRepository.findById(id);
	}

	@Transactional
	public void saveActividad(Actividad actividad) throws DataAccessException {
		actividadRepository.save(actividad);	
	}
	
	@Transactional
	public void deleteActividad(Actividad actividad) throws DataAccessException {
		actividadRepository.delete(actividad);	
	}
	
	@Transactional
	public Iterable<Actividad> findAllMine(Manager manager) {
		return actividadRepository.findAllMine(manager.getId());
	}
	
	@Transactional
	public Iterable<Actividad> findAll() {
		return actividadRepository.findAll();
	}

	public Iterable<Actividad> findAllMineAnciano(Anciano anciano) {
		return actividadRepository.findAllMineAnciano(anciano.getId(), java.sql.Date.valueOf(LocalDate.now()));
	}
	
	@Transactional
	public Long countActividades() {
		return actividadRepository.count();
	}

	@Transactional
	public Double avgActividadesByResidencia() {
		return Double.valueOf(this.actividadRepository.count())/this.residenciaService.countResidencias().doubleValue();
	}		
	
	

}
