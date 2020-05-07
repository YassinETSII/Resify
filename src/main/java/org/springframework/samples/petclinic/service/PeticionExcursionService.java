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
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.PeticionExcursionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PeticionExcursionService {

	@Autowired
	private PeticionExcursionRepository peticionExcursionRepository;

	@Transactional
	public PeticionExcursion findPeticionExcursionById(int id) throws DataAccessException {
		return peticionExcursionRepository.findById(id);
	}

	@Transactional
	public Iterable<PeticionExcursion> findAllMineResidencia(Residencia residencia) throws DataAccessException {
		return peticionExcursionRepository.findByResidencia(residencia.getId(), java.sql.Date.valueOf(LocalDate.now()));
	}

	@Transactional
	public Iterable<PeticionExcursion> findAllMineOrganizador(Organizador organizador) throws DataAccessException {
		return peticionExcursionRepository.findByOrganizador(organizador.getId());
	}

	@Transactional
	public Double countPeticionExcursionAceptadaByExcursion(Excursion excursion) throws DataAccessException {
		return peticionExcursionRepository.findByExcursionAceptada(excursion.getId());
	}

	@Transactional
	public void save(PeticionExcursion peticionExcursion) throws DataAccessException {
		peticionExcursionRepository.save(peticionExcursion);
	}

	@Transactional(readOnly = true)
	public int countPeticionesByExcursion(Excursion excursion, Manager manager) throws DataAccessException {
		return peticionExcursionRepository.countPeticionesByExcursion(excursion, manager);
	}
	
	@Transactional(readOnly = true)
	public int countPeticionesByExcursionAceptada(Excursion excursion, Manager manager) throws DataAccessException {
		return peticionExcursionRepository.countPeticionesAceptadasByExcursion(excursion, manager);
	}

}