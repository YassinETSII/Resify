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
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.AncianoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class AncianoService {

	private AncianoRepository	ancianoRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@Autowired
	public AncianoService(final AncianoRepository ancianoRepository) {
		this.ancianoRepository = ancianoRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Anciano> findAncianos() throws DataAccessException {
		return this.ancianoRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Anciano findAncianoById(final int id) throws DataAccessException {
		return this.ancianoRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Anciano findAncianoByUsername(final String username) throws DataAccessException {
		return this.ancianoRepository.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public Iterable<Anciano> findAncianosMiResidencia(final Residencia residencia) throws DataAccessException {
		return this.ancianoRepository.findAncianosMiResidencia(residencia.getId());
	}

	@Transactional
	public void saveAnciano(final Anciano anciano) throws DataAccessException {
		//creating anciano
		this.ancianoRepository.save(anciano);
		//creating user
		this.userService.saveUser(anciano.getUser());
		//creating authorities
		this.authoritiesService.saveAuthorities(anciano.getUser().getUsername(), "anciano");
	}

}
