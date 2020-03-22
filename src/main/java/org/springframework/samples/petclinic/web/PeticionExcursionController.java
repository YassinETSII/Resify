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
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
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
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/excursiones/{excursionId}/peticiones-excursion")
public class PeticionExcursionController {

	private static final String VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM = "peticionesExcursion/createPeticionExcursionForm";

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

	@GetMapping(value = "/new")
	public String initCreationForm(@PathVariable("excursionId") int excursionId, Map<String, Object> model,
			Principal p) {

		PeticionExcursion peticionExcursion = new PeticionExcursion();

		peticionExcursion.setFecha(new Date());
		peticionExcursion.setEstado("pendiente");

		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Manager manager = this.managerService.findManagerByUserName(p.getName());
		Integer peticiones = this.managerService.countPeticionesByExcursion(excursion, manager);
		Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());

		model.put("hasPeticion", peticiones != 0);
		model.put("hasResidencia", residencia != null);

		model.put("peticionExcursion", peticionExcursion);
		return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@PathVariable("excursionId") int excursionId,
			@Valid PeticionExcursion peticionExcursion, BindingResult result, Map<String, Object> model, Principal p) {

		if (result.hasErrors()) {
			model.put("peticionExcursion", peticionExcursion);
			return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());
			Excursion excursion = excursionService.findExcursionById(excursionId);
			Date fecha = new Date();
			peticionExcursion.setResidencia(residencia);
			peticionExcursion.setExcursion(excursion);
			peticionExcursion.setEstado("pendiente");
			peticionExcursion.setFecha(fecha);
			peticionExcursionService.save(peticionExcursion);
			model.put("message", "Se ha enviado la peticion correctamente");
			return "redirect:/excursiones/{excursionId}";
		}
	}
}