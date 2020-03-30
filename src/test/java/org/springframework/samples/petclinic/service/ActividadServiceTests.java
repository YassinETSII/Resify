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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ActividadServiceTests {

	@Autowired
	protected ActividadService	actividadService;

	@Autowired
	protected ManagerService		managerService;

	@Autowired
	protected ResidenciaService		residenciaService;


	@Test
	void debeEncontrarActividadConIdCorrecto() {
		Actividad a = this.actividadService.findActividadById(1);
		Assertions.assertTrue(a.getTitulo().equals("Prueba1"));
		Assertions.assertTrue(a.getResidencia().getNombre().equals("Residencia 1"));

	}

	@Test
	void debeEncontrarTodasLasActividades() {
		Iterable<Actividad> acts = this.actividadService.findAll();

		ArrayList<Actividad> actsn = new ArrayList<Actividad>();
		for (Actividad a: acts) {
			actsn.add(a);
		}
		Actividad a = actsn.get(0);
		Assertions.assertTrue(a.getTitulo().equals("Prueba1"));
	}

	@Test
	void debeEncontrarTodasLasActividadesPorManager() {
		Manager m = this.managerService.findManagerById(4);
		Iterable<Actividad> acts = this.actividadService.findAllMine(m);

		ArrayList<Actividad> actsn = new ArrayList<Actividad>();
		for (Actividad a : acts) {
			actsn.add(a);
		}

		Actividad a = actsn.get(0);
		Assertions.assertTrue(a.getTitulo().equals("Prueba4"));
	}

	@Test
	@Transactional
	public void debeCrearActividadYGenerarId() {
		Manager m = this.managerService.findManagerById(4);
		Iterable<Actividad> acts = this.actividadService.findAllMine(m);
		ArrayList<Actividad> actsn = new ArrayList<Actividad>();
		for (Actividad a : acts) {
			actsn.add(a);
		}

		int total = actsn.size();

		Actividad a = new Actividad();
		a.setTitulo("Test");
		a.setDescripcion("Test descripcion");
		a.setFechaInicio(LocalDate.now().plusDays(2));
		a.setHoraFin(LocalTime.of(20, 0));
		a.setHoraInicio(LocalTime.of(17, 0));
		Residencia resi = this.residenciaService.findAllMine(m).iterator().next();
		a.setResidencia(resi);

		this.actividadService.saveActividad(a);

		Iterable<Actividad> acts2 = this.actividadService.findAllMine(m);
		ArrayList<Actividad> actsn2 = new ArrayList<Actividad>();
		for (Actividad a2 : acts2) {
			actsn2.add(a2);
		}

		//Comprueba que se ha añadido a las buenas acciones del manager
		Assertions.assertTrue(actsn2.size() == total + 1);

		//Comprueba que su id ya no es nulo
		Assertions.assertTrue(a.getId() != null);
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoActividadEnBlanco() {

		Actividad a = new Actividad();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.actividadService.saveActividad(a);
		});
	}

}
