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

import java.time.LocalTime;
import java.util.ArrayList;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ResidenciaServiceTests {

	@Autowired
	protected ResidenciaService	residenciaService;

	@Autowired
	protected ManagerService	managerService;


	@Test
	void debeEncontrarResidenciaConIdCorrecto() {
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		Residencia re = this.residenciaService.findResidenciaById(1);
		Assertions.assertTrue(re.getAceptaDependenciaGrave() == false);
		Assertions.assertTrue(re.getAforo().equals(100));
		Assertions.assertTrue(re.getCorreo().equals("residencia1@mail.es"));
		Assertions.assertTrue(re.getDescripcion().equals("Descripcion de prueba"));
		Assertions.assertTrue(re.getDireccion().equals("Direccion"));
		Assertions.assertTrue(re.getEdadMaxima().equals(70));
		Assertions.assertTrue(re.getHoraApertura().equals(horaApertura));
		Assertions.assertTrue(re.getHoraCierre().equals(horaCierre));
	}

	@Test
	void debeEncontrarTodasLasResidencias() {
		Iterable<Residencia> res = this.residenciaService.findAll();

		ArrayList<Residencia> resis = new ArrayList<Residencia>();
		for (Residencia r : res) {
			resis.add(r);
		}
		Residencia re2 = resis.get(0);
		Assertions.assertTrue(re2.getCorreo().equals("residencia1@mail.es"));
	}

	@Test
	void debeEncontrarLaResidenciaPorManager() {
		Manager m = this.managerService.findManagerById(3);
		Residencia res = this.residenciaService.findMine(m);
		Assertions.assertTrue(res.getCorreo().equals("residencia1@mail.es"));
	}

	@Test
	@Transactional
	public void debeCrearResidencia() {
		Manager m = this.managerService.findManagerById(7);
		
		Residencia ra = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		ra.setAceptaDependenciaGrave(false);
		ra.setAforo(100);
		ra.setCorreo("residencia1@mail.es");
		ra.setDescripcion("Descripcion de prueba");
		ra.setDireccion("Direccion");
		ra.setEdadMaxima(70);
		ra.setHoraApertura(horaApertura);
		ra.setHoraCierre(horaCierre);
		ra.setMasInfo("http://www.resi1.com");
		ra.setNombre("Reidencia 1");
		ra.setTelefono("987654321");
		ra.setManager(m);

		this.residenciaService.saveResidencia(ra);

		Residencia rres2 = this.residenciaService.findMine(m);
		//Comprueba que se ha añadido a las residencias del manager (revisar esto, no debería dejar crear más de una)
		Assertions.assertTrue(rres2 != null);
		//Comprueba que su id ya no es nulo
		Assertions.assertTrue(ra.getId() != null);
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoResidenciaEnBlanco() {

		Residencia ra = new Residencia();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.residenciaService.saveResidencia(ra);
		});
	}

	/*
	 * @Test
	 *
	 * @Transactional
	 * public void debeCalcularRatio() {
	 *
	 * Residencia ra = new Residencia();
	 * LocalTime horaApertura = LocalTime.of(07, 00);
	 * LocalTime horaCierre = LocalTime.of(21, 00);
	 * ra.setAceptaDependenciaGrave(false);
	 * ra.setAforo(100);
	 * ra.setCorreo("residencia1@mail.es");
	 * ra.setDescripcion("Descripcion de prueba");
	 * ra.setDireccion("Direccion");
	 * ra.setEdadMaxima(70);
	 * ra.setHoraApertura(horaApertura);
	 * ra.setHoraCierre(horaCierre);
	 * ra.setMasInfo("http://www.resi1.com");
	 * ra.setNombre("Reidencia 1");
	 * ra.setTelefono("987654321");
	 *
	 * Incidencia inci = new Incidencia();
	 * Date fecha = new Date("2003-12-12");
	 * inci.setDescripcion("descripcion inci");
	 * inci.setFecha(fecha);
	 * //INSERT INTO incidencias VALUES (1, 'Descripcion de incidencia1', '2010-09-07', 'titulo1', 1);
	 *
	 * Double m = this.residenciaService.getRatio(ra);
	 *
	 * this.residenciaService.saveResidencia(ra);
	 *
	 * }
	 */

}
