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
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.springdatajpa.BuenaAccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuenaAccionService {

	@Autowired
	private BuenaAccionRepository buenaAccionRepository;	


	@Transactional(readOnly = true)
	public BuenaAccion findBuenaAccionById(int id) throws DataAccessException {
		return buenaAccionRepository.findById(id);
	}

	@Transactional
	public void saveBuenaAccion(BuenaAccion buenaAccion) throws DataAccessException {
		buenaAccionRepository.save(buenaAccion);	
	}
	
	@Transactional
	public Iterable<BuenaAccion> findAllMine(Manager manager) {
		return buenaAccionRepository.findAllMine(manager.getId());
	}
	
	@Transactional
	public Iterable<BuenaAccion> findAll() {
		return buenaAccionRepository.findAll();
	}

	@Transactional
	public Long countBuenasAcciones() {
		return buenaAccionRepository.count();
	}		
	
}
