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
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Queja;
import org.springframework.samples.petclinic.repository.springdatajpa.QuejaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class QuejaService {

	@Autowired
	private QuejaRepository quejaRepository;	

	@Transactional(readOnly = true)
	public Iterable<Queja> findQuejasByManager(Manager manager) throws DataAccessException {
		return quejaRepository.findQuejasByManagerId(manager.getId());
	}

	@Transactional(readOnly = true)
	public Queja findQuejaById(int id) throws DataAccessException {
		return quejaRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Double countQuejasHoyByAnciano(Anciano anciano) throws DataAccessException {
		
		return quejaRepository.countQuejasByTiempoYAncianoId(anciano.getId(),Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	@Transactional
	public void saveQueja(Queja queja) throws DataAccessException {
		quejaRepository.save(queja);	
	}

	@Transactional
	public Long countQuejas() {
		return this.quejaRepository.count();
	}

	@Transactional
	public Double avgQuejasByAnciano() {
		Double res = 0.;
		Iterable<Long> list = this.quejaRepository.countQuejasByAnciano();
		int i = 0;
		for(Long x: list) {
			i+=1;
			res+=x;
		}
		if(i!=0) {
			res/=i;
		}
		
		return res;
	}		


	@Transactional
	public Double avgQuejasByResidencia() {
		Double res = 0.;
		Iterable<Long> list = this.quejaRepository.countQuejasByResidencia();
		int i = 0;
		for(Long x: list) {
			i+=1;
			res+=x;
		}
		if(i!=0) {
			res/=i;
		}
		
		return res;
	}		
	

}
