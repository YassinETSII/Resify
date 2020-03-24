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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.ResidenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ResidenciaService {

	@Autowired
	private ResidenciaRepository residenciaRepository;

	@Transactional(readOnly = true)
	public Residencia findResidenciaById(final int id) throws DataAccessException {
		return this.residenciaRepository.findById(id);
	}

	@Transactional
	public void saveResidencia(final Residencia residencia) throws DataAccessException {
		this.residenciaRepository.save(residencia);
	}

	@Transactional
	public Iterable<Residencia> findAllMine(final Manager manager) {
		return this.residenciaRepository.findAllMine(manager.getId());
	}

	@Transactional
	public Iterable<Residencia> findAll() {
		return this.residenciaRepository.findAll();
	}

	@Transactional
	public List<Residencia> findTop(int nResults) {
		List<Residencia> list = new ArrayList<>();
		Iterator<Object[]> lt = this.residenciaRepository.findTop().iterator();
		int i = 0;
		while (i < nResults) {
			if (lt.hasNext()) {
				list.add((Residencia) lt.next()[0]);
				System.out.println(lt.next());
			}
			i++;
		}
		return list;
	}

}
