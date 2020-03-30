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

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ExcursionServiceTests {

	@Autowired
	protected ExcursionService	excursionService;
	
	@Autowired
	protected OrganizadorService	organizadorService;

	@Test
	void debeEncontrarExcursionConIdCorrecto() {
		Excursion exc = this.excursionService.findExcursionById(1);
		Assertions.assertTrue(exc.getRatioAceptacion().equals(2.0));
		Assertions.assertTrue(exc.getNumeroResidencias() == 4);
		Assertions.assertTrue(exc.getFechaFin().equals(LocalDate.of(2020, 9, 7)));
		Assertions.assertTrue(exc.isFinalMode());
		Assertions.assertTrue(exc.getOrganizador().equals(this.organizadorService.findOrganizadorById(1)));

	}

	@Test
	void debeEncontrarTodasLasExcursiones() {
		Iterable<Excursion> exc = this.excursionService.findAll();

		ArrayList<Excursion> excursiones = new ArrayList<Excursion>();
		for (Excursion e : exc) {
			excursiones.add(e);
		}
		Excursion excursion1 = excursiones.get(0);
		Assertions.assertTrue(excursion1.getTitulo().equals("Prueba1"));
	}

	@Test
	void debeEncontrarTodasLasExcursionesPublicadasYFuturas() {
		Iterable<Excursion> exc = this.excursionService.findAllPublished();

		ArrayList<Excursion> excursiones = new ArrayList<Excursion>();
		for (Excursion e : exc) {
			excursiones.add(e);
		}

		Excursion excursion = excursiones.get(0);
		System.out.println(excursion.getTitulo());
		Assertions.assertTrue(excursion.getTitulo().equals("Prueba3"));
	}

	@Test
	void debeEncontrarTodasLasExcursionesPorOrganizador() {
		Organizador organizador = this.organizadorService.findOrganizadorById(1);
		Iterable<Excursion> exc = this.excursionService.findAllMine(organizador);

		ArrayList<Excursion> excursiones = new ArrayList<Excursion>();
		for (Excursion e : exc) {
			excursiones.add(e);
		}
		Excursion excursion1 = excursiones.get(0);
		Assertions.assertTrue(excursion1.getTitulo().equals("Prueba1"));
	}

//	@Test
//	@Transactional
//	public void debeCrearBuenaAccion() {
//		Manager m = this.managerService.findManagerById(3);
//		Iterable<Excursion> bas = this.excursionService.findAllMine(m);
//		ArrayList<Excursion> basc = new ArrayList<Excursion>();
//		for (Excursion b : bas) {
//			basc.add(b);
//		}
//
//		int total = basc.size();
//
//		Iterable<Residencia> res = this.residenciaService.findAllMine(m);
//		ArrayList<Residencia> ress = new ArrayList<Residencia>();
//		for (Residencia r : res) {
//			ress.add(r);
//		}
//
//		Excursion ba = new Excursion();
//		ba.setTitulo("Prueba");
//		ba.setFecha(new Date(System.currentTimeMillis() - 1));
//		ba.setDescripcion("Prueba descripcion");
//		ba.setResidencia(ress.get(0));
//
//		this.excursionService.saveBuenaAccion(ba);
//
//		Iterable<Excursion> bas2 = this.excursionService.findAllMine(m);
//		ArrayList<Excursion> basc2 = new ArrayList<Excursion>();
//		for (Excursion b2 : bas2) {
//			basc2.add(b2);
//		}
//
//		//Comprueba que se ha añadido a las buenas acciones del manager
//		Assertions.assertTrue(basc2.size() == total + 1);
//
//		//Comprueba que su id ya no es nulo
//		Assertions.assertTrue(ba.getId() != null);
//	}
//
//	@Test
//	@Transactional
//	public void debeLanzarExcepcionCreandoBuenaAccionEnBlanco() {
//
//		Excursion ba = new Excursion();
//
//		Assertions.assertThrows(ConstraintViolationException.class, () -> {
//			this.excursionService.saveBuenaAccion(ba);
//		});
//	}

}
