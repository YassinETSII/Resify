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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
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
	public Residencia findMine(final Manager manager) {
		return this.residenciaRepository.findMine(manager.getId());
	}

	@Transactional
	public Iterable<Residencia> findAll() {
		return this.residenciaRepository.findAll();
	}

	@Transactional
	public Double getRatio(final Residencia residencia) {
		Double res = 0.;
		Double buenasAcciones = this.residenciaRepository.countBuenasAccionesByResidenciaId(residencia.getId());
		Double incidencias = this.residenciaRepository.countIncidenciasByResidenciaId(residencia.getId());
		if (incidencias == 0) {
			res = buenasAcciones;
		} else {
			res = buenasAcciones / incidencias;
		}
		return res;
	}

	@Transactional
	public List<Residencia> findTop(final int nResults) {
		List<Residencia> list = new ArrayList<>();
		Iterable<Residencia> resIterable = this.residenciaRepository.findAll();
		for (Residencia res : resIterable) {
			list.add(res);
		}
		List<Residencia> ordenada = this.ordenaPorRatio(list, new ArrayList<>(), 0.);

		List<Residencia> top = new ArrayList<>();
		int i = 0;
		for (Residencia r : ordenada) {
			if (i < nResults) {
				top.add(r);
				i++;
			}
		}

		return top;

		//		List<Residencia> list = new ArrayList<>();
		//		Iterator<Object[]> lt = this.residenciaRepository.findTop().iterator();
		//		int i = 0;
		//		while (i < nResults) {
		//			if (lt.hasNext()) {
		//				list.add((Residencia) lt.next()[0]);
		//			}
		//			i++;
		//		}
		//		return list;
	}

	@Transactional
	public Iterable<Residencia> findResidenciasNoParticipantes(final Organizador organizador) {
		return this.residenciaRepository.findResidenciasSinParticipar(organizador.getId());
	}
	
	@Transactional
	public Residencia findResidenciaByAnciano(final Anciano anciano) {
		return this.residenciaRepository.findResidenciaByAncianoId(anciano.getId());
	}

	@Transactional
	public Long countResidencias() {
		return this.residenciaRepository.count();
	}

	@Transactional
	public Long countResidenciasCompletas() {
		return this.residenciaRepository.countResidenciasCompletas();
	}

	

	private List<Residencia> ordenaPorRatio(final List<Residencia> listaPorOrdenar, final List<Residencia> listaYaOrdenada, final Double ratioInicial) {
		List<Residencia> res = listaYaOrdenada;
		Double ratioActual = ratioInicial;
		Residencia siguiente = new Residencia();
		if (!listaPorOrdenar.isEmpty()) {
			for (Residencia i : listaPorOrdenar) {
				if (this.getRatio(i) >= ratioActual) {
					siguiente = i;
					ratioActual = this.getRatio(i);
				}
			}
			res.add(siguiente);
			listaPorOrdenar.remove(siguiente);
			this.ordenaPorRatio(listaPorOrdenar, res, 0.);
		}
		return res;
	}


}
