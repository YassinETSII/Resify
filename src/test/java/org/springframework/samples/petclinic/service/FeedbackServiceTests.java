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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.Feedback;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class FeedbackServiceTests {

	@Autowired
	protected FeedbackService			feedbackService;

	@Autowired
	protected ManagerService			managerService;
	
	@Autowired
	protected ResidenciaService			residenciaService;

	@Autowired
	protected OrganizadorService		organizadorService;

	@Autowired
	protected IncidenciaService			incidenciaService;

	@Autowired
	protected BuenaAccionService		buenaAccionService;

	@Autowired
	protected ExcursionService			excursionService;

	@Autowired
	protected PeticionExcursionService	peticionExcursionService;

	@Autowired
	protected AncianoService			ancianoService;


	@Test
	void debeEncontrarFeedbackConIdCorrecto() {
		Feedback fe = this.feedbackService.findFeedbackById(1);
		Assertions.assertTrue(fe.isDescartaFeedback() == false);
		Assertions.assertTrue(fe.getDescripcion().equals("desc1"));
		Assertions.assertTrue(fe.getValoracion().equals(2));
	}

	  @Test 
	  void noDebeEncontrarFeedbackConIdInexistente() { 
		  Feedback fe = this.feedbackService.findFeedbackById(99999); 
		  Assertions.assertNull(fe); 
	}
	  
	  @Test 
	  void debeEncontrarTodosMisFeedbacks() { 
		  Organizador organizador = this.organizadorService.findOrganizadorById(1);
		  Iterable<Feedback> fes = this.feedbackService.findAllMineOrganizador(organizador);
			  
		ArrayList<Feedback> fesis = new ArrayList<Feedback>(); 
		for (Feedback r : fes){ 
			fesis.add(r); 
		} 
		Feedback fe2 = fesis.get(0);
		Assertions.assertTrue(fe2.getDescripcion().equals("desc1")); 
		}
	  
	  @Test 
	  void noDebeEncontrarFeedbacksNoEsMio() { 
		  Organizador organizador = this.organizadorService.findOrganizadorById(1);
		  Iterable<Feedback> fes = this.feedbackService.findAllMineOrganizador(organizador);
			  
		ArrayList<Feedback> fesis = new ArrayList<Feedback>(); 
		for (Feedback r : fes){ 
			fesis.add(r); 
		} 
		Feedback fe2 = fesis.get(0);
		Assertions.assertFalse(fe2.getDescripcion().equals("descNOEXISTE")); 
		}
	  
	  @Test 
	  void cuentaFeedbacksPorExcursion() { 
		  Excursion ex = this.excursionService.findExcursionById(6);
		  Double fe = this.feedbackService.countFeedbackByExcursion(ex); 
		  Assertions.assertTrue(fe==1); 
	}
	  
	  @Test 
	  void cuentaFeedbacksPorExcursionSinFeedback() { 
		  Excursion ex = this.excursionService.findExcursionById(1);
		  Double fe = this.feedbackService.countFeedbackByExcursion(ex); 
		  Assertions.assertFalse(fe>1); 
	}
	  
	  @Test 
	  void cuentaFeedbacksPorExcursionYManager() { 
		  Excursion ex = this.excursionService.findExcursionById(6);
		  Manager man = this.managerService.findManagerById(3);
		  int fe = this.feedbackService.countFeedbacksByExcursion(ex,man); 
		  Assertions.assertTrue(fe==1); 
	}
	  
}
