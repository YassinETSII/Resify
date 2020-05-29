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
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.samples.petclinic.repository.springdatajpa.VisitaSanitariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class VisitaSanitariaService {

	@Autowired
	private VisitaSanitariaRepository visitaSanitariaRepository;	
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Transactional(readOnly = true)
	public VisitaSanitaria findVisitaSanitariaById(int id) throws DataAccessException {
		return visitaSanitariaRepository.findById(id);
	}

	@Transactional
	public void saveVisitaSanitaria(VisitaSanitaria visitaSanitaria) throws DataAccessException {
		visitaSanitariaRepository.save(visitaSanitaria);	
	}
	
	@Transactional
	public void deleteVisitaSanitaria(VisitaSanitaria visitaSanitaria) throws DataAccessException {
		visitaSanitariaRepository.delete(visitaSanitaria);	
	}

	@Transactional
	public Iterable<VisitaSanitaria> findAllMine(final Manager manager) {
		Residencia residencia = this.residenciaService.findMine(manager);
		int id = residencia.getId();
		return this.visitaSanitariaRepository.findAllMine(id);
	}

	@Transactional
	public Long countVisitasSanitarias() {
		return this.visitaSanitariaRepository.count();
	}

	@Transactional
	public Double avgVisitasSanitariasByResidencia() {
		Double res = 0.;
		if (!this.residenciaService.countResidencias().equals(0L)) {
			res = Double.valueOf(this.visitaSanitariaRepository.count())/this.residenciaService.countResidencias().doubleValue();
		}
		return res;
	}		

}
