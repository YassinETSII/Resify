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
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.repository.springdatajpa.ExcursionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ExcursionService {

	@Autowired
	private ExcursionRepository excursionRepository;	
	
//	@Autowired
//	private UserService userService;
//	
//	@Autowired
//	private AuthoritiesService authoritiesService;

//	@Autowired
//	public ExcursionService(ExcursionRepository excursionRepository) {
//		this.excursionRepository = excursionRepository;
//	}	

	@Transactional(readOnly = true)
	public Excursion findExcursionById(int id) throws DataAccessException {
		return excursionRepository.findById(id);
	}

	@Transactional
	public void saveExcursion(Excursion excursion) throws DataAccessException {
		excursionRepository.save(excursion);	
	}
	
	@Transactional
	public void deleteExcursion(Excursion excursion) throws DataAccessException {
		excursionRepository.delete(excursion);	
	}

	@Transactional
	public Iterable<Excursion> findAllMine(Organizador organizador) {
		return excursionRepository.findAllMine(organizador.getId());
	}	
	
	@Transactional
	public Iterable<Excursion> findAllPublished() {
		return excursionRepository.findAllPublished(LocalDate.now());
	}	
	
	@Transactional
	public Iterable<Excursion> findAll() {
		return excursionRepository.findAll();
	}		
	
	

}
