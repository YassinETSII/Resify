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

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Queja;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class QuejaServiceTests {

	@Autowired
	protected QuejaService	quejaService;

	@Autowired
	protected ManagerService	managerService;

	@Autowired
	protected AncianoService	ancianoService;


	@Test
	void debeEncontrarQuejaConIdCorrecto() {
		Queja queja = this.quejaService.findQuejaById(1);
		Assertions.assertTrue(queja.getTitulo().equals("Titulo Prueba 1"));
		Assertions.assertTrue(queja.getAnonimo() == false);
		Assertions.assertTrue(queja.getAnciano().getId().equals(7));
		Assertions.assertTrue(queja.getDescripcion().equals("Descripcion Prueba 1"));
		Assertions.assertTrue(queja.getFecha().compareTo(java.sql.Date.valueOf(LocalDate.of(2020, 3, 2))) == 0);
		
	}

	@Test
	void debeEncontrarTodasLasQuejasPorManager() {
		Manager manager = this.managerService.findManagerById(3);
		Iterable<Queja> qs = this.quejaService.findQuejasByManager(manager);

		ArrayList<Queja> quejas = new ArrayList<Queja>();
		for (Queja q : qs) {
			quejas.add(q);
		}
		Queja queja = quejas.get(0);
		Assertions.assertTrue(queja.getTitulo().equals("Titulo Prueba 1"));
		Assertions.assertTrue(queja.getAnonimo() == false);
		Assertions.assertTrue(queja.getAnciano().getId().equals(7));
		Assertions.assertTrue(queja.getDescripcion().equals("Descripcion Prueba 1"));
		Assertions.assertTrue(queja.getFecha().compareTo(java.sql.Date.valueOf(LocalDate.of(2020, 3, 2))) == 0);

	}

	@Test
	void debeContarTodasLasQuejasPorAnciano() {
		Anciano a = this.ancianoService.findAncianoById(7);
		Double quejas = this.quejaService.countQuejasHoyByAnciano(a);

		Assertions.assertTrue(quejas.equals(0.));
	}

	@Test
	@Transactional
	public void debeCrearQueja() {
		Manager manager = this.managerService.findManagerById(3);
		Iterable<Queja> qs = this.quejaService.findQuejasByManager(manager);
		ArrayList<Queja> quejas1 = new ArrayList<Queja>();
		for (Queja q : qs) {
			quejas1.add(q);
		}

		int total = quejas1.size();

		Anciano anciano = this.ancianoService.findAncianoById(7);
		
		Queja queja = new Queja();
		queja.setTitulo("Prueba");
		queja.setDescripcion("Prueba desc");
		queja.setFecha(Date.from(Instant.now().minusMillis(1)));
		queja.setAnciano(anciano);
		queja.setAnonimo(true);

		this.quejaService.saveQueja(queja);

		Iterable<Queja> qs2 = this.quejaService.findQuejasByManager(manager);
		ArrayList<Queja> quejas2 = new ArrayList<Queja>();
		for (Queja q2 : qs2) {
			quejas2.add(q2);
		}

		//Comprueba que se ha añadido a las quejas del manager
		Assertions.assertTrue(quejas2.size() == total + 1);

		//Comprueba que su id ya no es nulo
		Assertions.assertTrue(queja.getId() != null);
		
		Assertions.assertTrue(quejas2.contains(queja));
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoQuejaEnBlanco() {
		
		Anciano anciano = this.ancianoService.findAncianoById(7);
		
		Queja queja = new Queja();
		queja.setFecha(Date.from(Instant.now().minusMillis(1)));
		queja.setAnciano(anciano);
		queja.setAnonimo(false);


		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.quejaService.saveQueja(queja);
		});
	}


}
