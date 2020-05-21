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
import org.springframework.samples.petclinic.model.Incidencia;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.springdatajpa.IncidenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IncidenciaService {

	@Autowired
	private IncidenciaRepository incidenciaRepository;	

	@Transactional(readOnly = true)
	public Incidencia findIncidenciaById(int id) throws DataAccessException {
		return incidenciaRepository.findById(id);
	}

	@Transactional
	public void saveIncidencia(Incidencia incidencia) throws DataAccessException {
		incidenciaRepository.save(incidencia);	
	}
	
	@Transactional
	public Iterable<Incidencia> findAllMine(Manager manager) {
		return incidenciaRepository.findAllMine(manager.getId());
	}
	
	@Transactional
	public Iterable<Incidencia> findAll() {
		return incidenciaRepository.findAll();
	}

	@Transactional
	public Long countIncidencias() {
		return this.incidenciaRepository.count();
	}		
	
	

}
