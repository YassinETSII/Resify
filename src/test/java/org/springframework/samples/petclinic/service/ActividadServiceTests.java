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

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Actividad;
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
	void debeEncontrarAcrividadConIdCorrecto() {
		Actividad act = this.actividadService.findActividadById(1);
		Assertions.assertTrue(act.getTitulo().equals("Prueba1"));
		Assertions.assertTrue(act.getResidencia().getNombre().equals("Residencia 1"));

	}

	@Test
	void debeEncontrarTodasLasBuenasAcciones() {
		Iterable<Actividad> acts = this.actividadService.findAll();

		ArrayList<Actividad> actividades = new ArrayList<Actividad>();
		for (Actividad a : acts) {
			actividades.add(a);
		}
		Actividad actividad = actividades.get(0);
		Assertions.assertTrue(actividad.getTitulo().equals("Prueba1"));
	}

	@Test
	void debeEncontrarTodasLasBuenasAccionesPorManager() {
		Manager m = this.managerService.findManagerById(3);
		Iterable<Actividad> acts = this.actividadService.findAllMine(m);

		ArrayList<Actividad> actividades = new ArrayList<Actividad>();
		for (Actividad a : acts) {
			actividades.add(a);
		}

		Actividad actividad = actividades.get(0);
		Assertions.assertTrue(actividad.getTitulo().equals("Prueba1"));
	}

	@Test
	@Transactional
	public void debeCrearBuenaAccion() {
		Manager manager = this.managerService.findManagerById(3);
		Iterable<Actividad> acts = this.actividadService.findAllMine(manager);
		ArrayList<Actividad> actividades1 = new ArrayList<Actividad>();
		for (Actividad a : acts) {
			actividades1.add(a);
		}

		int total = actividades1.size();

		Iterable<Residencia> res = this.residenciaService.findAllMine(manager);
		ArrayList<Residencia> ress = new ArrayList<Residencia>();
		for (Residencia r : res) {
			ress.add(r);
		}

		Actividad act = new Actividad();
		act.setTitulo("Prueba");
		act.setDescripcion("Prueba desc");
		act.setFechaInicio(LocalDate.now().plusDays(5));
		act.setHoraInicio(LocalTime.of(9, 0));
		act.setHoraFin(LocalTime.of(22, 0));
		act.setResidencia(ress.get(0));

		this.actividadService.saveActividad(act);

		Iterable<Actividad> bas2 = this.actividadService.findAllMine(manager);
		ArrayList<Actividad> basc2 = new ArrayList<Actividad>();
		for (Actividad b2 : bas2) {
			basc2.add(b2);
		}

		//Comprueba que se ha añadido a las buenas acciones del manager
		Assertions.assertTrue(basc2.size() == total + 1);

		//Comprueba que su id ya no es nulo
		Assertions.assertTrue(act.getId() != null);
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoBuenaAccionEnBlanco() {

		Actividad ba = new Actividad();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.actividadService.saveActividad(ba);
		});
	}

}
