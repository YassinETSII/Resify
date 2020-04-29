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

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class InscripcionServiceTests {

	@Autowired
	protected InscripcionService	inscripcionService;

	@Autowired
	protected ManagerService		managerService;

	@Autowired
	protected AncianoService		ancianoService;

	@Autowired
	protected ResidenciaService		residenciaService;


	@Test
	void debeEncontrarInscripcionConIdCorrecto() {
		Inscripcion i = this.inscripcionService.findInscripcionById(1);
		Assertions.assertTrue(i.getDeclaracion().equals("Declaración1"));
	}

	@Test
	void debeEncontrarTodasLasInscripciones() {
		Iterable<Inscripcion> bas = this.inscripcionService.findAll();

		ArrayList<Inscripcion> basc = new ArrayList<Inscripcion>();
		for (Inscripcion b : bas) {
			basc.add(b);
		}
		Inscripcion ba = basc.get(0);
		Assertions.assertTrue(ba.getDeclaracion().equals("Declaración1"));
	}

	@Test
	void debeEncontrarTodasLasInscripcionesPorManager() {
		Manager m = this.managerService.findManagerById(3);
		Iterable<Inscripcion> bas = this.inscripcionService.findAllMineManager(m);

		ArrayList<Inscripcion> basc = new ArrayList<Inscripcion>();
		for (Inscripcion b : bas) {
			basc.add(b);
		}

		Inscripcion ba = basc.get(0);
		Assertions.assertTrue(ba.getDeclaracion().equals("Declaración1"));
	}

	/*
	 * @Test
	 *
	 * @Transactional public void debeCrearInscripcion() { Anciano a =
	 * this.ancianoService.findAncianoById(5); Manager m =
	 * this.managerService.findManagerById(3);
	 *
	 * Iterable<Inscripcion> bas = this.inscripcionService.findAllMineAnciano(a);
	 * ArrayList<Inscripcion> basc = new ArrayList<Inscripcion>(); for (Inscripcion
	 * b : bas) { basc.add(b); }
	 *
	 * int total = basc.size();
	 *
	 * Iterable<Residencia> res = this.residenciaService.findAllMine(m);
	 * ArrayList<Residencia> ress = new ArrayList<Residencia>(); for (Residencia r :
	 * res) { ress.add(r); }
	 *
	 * Inscripcion ba = new Inscripcion(); ba.setDeclaracion("Prueba");
	 * ba.setFecha(new Date(System.currentTimeMillis() - 1));
	 * ba.setEstado("pendiente"); ba.setJustificacion(null);
	 * ba.setResidencia(ress.get(0));
	 *
	 * this.inscripcionService.saveInscripcion(ba);
	 *
	 * Iterable<Inscripcion> bas2 = this.inscripcionService.findAllMineAnciano(a);
	 * ArrayList<Inscripcion> basc2 = new ArrayList<Inscripcion>(); for (Inscripcion
	 * b2 : bas2) { basc2.add(b2); }
	 *
	 * //Comprueba que se ha añadido a las inscripcions del anciano
	 * Assertions.assertTrue(basc2.size() == total + 1);
	 *
	 * //Comprueba que su id ya no es nulo Assertions.assertTrue(ba.getId() !=
	 * null); }
	 */
	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoInscripcionEnBlanco() {

		Inscripcion ba = new Inscripcion();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.inscripcionService.saveInscripcion(ba);
		});
	}

}
