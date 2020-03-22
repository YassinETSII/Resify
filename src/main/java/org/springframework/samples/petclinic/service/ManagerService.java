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
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ManagerService {

	private ManagerRepository managerRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public ManagerService(final ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	@Transactional(readOnly = true)
	public Manager findManagerById(final int id) throws DataAccessException {
		return this.managerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Manager findManagerByUserName(String username) throws DataAccessException {
		return managerRepository.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public Manager findManagerByUsername(final String username) throws DataAccessException {
		return this.managerRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Residencia findResidenciaByManagerUsername(String username) throws DataAccessException {
		return managerRepository.findResidenciaByManagerUsername(username);
	}

	@Transactional(readOnly = true)
	public int countPeticionesByExcursion(Excursion excursion, Manager manager) throws DataAccessException {
		return managerRepository.countPeticionesByExcursion(excursion, manager);
	}

	@Transactional
	public void saveManager(final Manager manager) throws DataAccessException {
		// creating manager
		this.managerRepository.save(manager);
		// creating user
		this.userService.saveUser(manager.getUser());
		// creating authorities
		this.authoritiesService.saveAuthorities(manager.getUser().getUsername(), "manager");
	}

}
