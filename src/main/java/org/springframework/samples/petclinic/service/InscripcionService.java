/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class InscripcionService {

	@Autowired
	private InscripcionRepository inscripcionRepository;


	@Transactional(readOnly = true)
	public Inscripcion findInscripcionById(final int id) throws DataAccessException {
		return this.inscripcionRepository.findById(id);
	}

	@Transactional
	public void saveInscripcion(final Inscripcion inscripcion) throws DataAccessException {
		this.inscripcionRepository.save(inscripcion);
	}

	@Transactional
	public Iterable<Inscripcion> findAllMineAnciano(final Anciano anciano) {
		return this.inscripcionRepository.findAllMineAnciano(anciano.getId());
	}

	@Transactional
	public Iterable<Inscripcion> findAllMineManager(final Manager manager) {
		return this.inscripcionRepository.findAllMineManager(manager.getId());
	}

	@Transactional
	public Integer cuentaAceptadasEnResidencia(final Residencia residencia) {
		return this.inscripcionRepository.cuentaAceptadasEnResidencia(residencia.getId());
	}

	@Transactional
	public Iterable<Inscripcion> findAll() {
		return this.inscripcionRepository.findAll();
	}

	@Transactional
	public Long countInscripciones() {
		return this.inscripcionRepository.count();
	}

	@Transactional
	public Long countInscripcionesAceptadas() {
		return this.inscripcionRepository.countInscripcionesAceptadas();
	}

	@Transactional
	public Double ratioInscripcionesAceptadas() {
		Double res = 0.;
		if (!this.countInscripciones().equals(0L)) {
			res = this.countInscripcionesAceptadas().doubleValue()/this.countInscripciones().doubleValue();
		}
		return res;
	}

	@Transactional
	public Long countInscripcionesRechazadas() {
		return this.inscripcionRepository.countInscripcionesRechazadas();
	}

	@Transactional
	public Double ratioInscripcionesRechazadas() {
		Double res = 0.;
		if (!this.countInscripciones().equals(0L)) {
			res = this.countInscripcionesRechazadas().doubleValue()/this.countInscripciones().doubleValue();
		}
		return res;
	}

}
