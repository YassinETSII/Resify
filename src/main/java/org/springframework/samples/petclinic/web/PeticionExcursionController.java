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
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class PeticionExcursionController {

	private static final String VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM = "manager/createPeticionExcursionForm";

	@Autowired
	private PeticionExcursionService peticionExcursionService;

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private ExcursionService excursionService;

	@InitBinder("manager")
	public void initmanagerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/manager/excursiones/{excursionId}/peticiones-excursion/new")
	public String initCreationForm(Map<String, Object> model, Principal p) {
		
		PeticionExcursion peticionExcursion = new PeticionExcursion();
		
		peticionExcursion.setFecha(LocalDate.now());
		peticionExcursion.setEstado("pendiente");

		model.put("peticionExcursion", peticionExcursion);
		return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/manager/excursiones/{excursionId}/peticiones-excursion/new")
	public String processCreationForm(@PathVariable("excursionId") int excursionId, @Valid PeticionExcursion peticionExcursion, BindingResult result, Map<String, Object> model, Principal p) {

		if (result.hasErrors()){
			model.put("peticionExcursion", peticionExcursion);
			return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
		}
		else {
			Residencia residencia = managerService.findresidenciaByManagerUsername(p.getName());
			Excursion excursion = excursionService.findExcursionById(excursionId);
			peticionExcursion.setResidencia(residencia);
			peticionExcursion.setExcursion(excursion);
			peticionExcursion.setEstado("pendiente");
			peticionExcursionService.save(peticionExcursion);
			model.put("message", "Se ha enviado la peticion correctamente");
			return "redirect:manager/excursiones";
		}
	}
}
