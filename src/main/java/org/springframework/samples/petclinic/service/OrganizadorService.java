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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.repository.springdatajpa.OrganizadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class OrganizadorService {

	private OrganizadorRepository organizadorRepository;	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public OrganizadorService(OrganizadorRepository organizadorRepository) {
		this.organizadorRepository = organizadorRepository;
	}	

	@Transactional(readOnly = true)
	public Organizador findOrganizadorById(int id) throws DataAccessException {
		return organizadorRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Organizador findOrganizadorByUsername(String username) throws DataAccessException {
		return organizadorRepository.findByUsername(username);
	}

//	@Transactional
//	public Organizador findOrganizadorAutentificado(Principal p) {
//		return organizadorRepository.findByUsername(p.getName());
//	}
	
	@Transactional
	public void saveOrganizador(Organizador organizador) throws DataAccessException {
		//creating organizador
		organizadorRepository.save(organizador);		
		//creating user
		userService.saveUser(organizador.getUser());
		//creating authorities
		authoritiesService.saveAuthorities(organizador.getUser().getUsername(), "organizador");
	}		

}
