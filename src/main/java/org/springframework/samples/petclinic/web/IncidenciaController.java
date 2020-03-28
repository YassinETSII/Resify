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
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Incidencia;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.IncidenciaService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.web.validators.IncidenciaValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaController {

	private static final String VIEWS_INCIDENCIA_CREATE_OR_UPDATE_FORM = "incidencias/createOrUpdateIncidenciaForm";

	@Autowired
	private IncidenciaService incidenciaService;

	@Autowired
	private ManagerService managerService;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("manager")
	public void initManagerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("incidencia")
	public void initIncidenciaBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new IncidenciaValidator());
	}

	@GetMapping()
	public String listIncidencias(Map<String, Object> model, Principal p) {
		Manager manager = managerService.findManagerByUsername(p.getName());
		Iterable<Incidencia> incidencias = incidenciaService.findAllMine(manager);
		model.put("incidencias", incidencias);
		return "incidencias/incidenciasList";
	}
	
	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model, Principal p) {
		Incidencia incidencia = new Incidencia();
		model.put("incidencia", incidencia);
		return VIEWS_INCIDENCIA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Incidencia incidencia, BindingResult result, Map<String, Object> model, Principal p) {
		if (result.hasErrors()) {
			model.put("incidencia", incidencia);
			return VIEWS_INCIDENCIA_CREATE_OR_UPDATE_FORM;
		}
		else {
			Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());
			incidencia.setResidencia(residencia);
			incidenciaService.saveIncidencia(incidencia);
			model.put("message", "Se ha registrado la incidencia correctamente");
			return "redirect:/incidencias";
		}
	}

	@GetMapping("/{incidenciaId}")
	public ModelAndView showIncidencia(@PathVariable("incidenciaId") int incidenciaId, Principal p) {
		Incidencia incidencia = this.incidenciaService.findIncidenciaById(incidenciaId);
		Manager manager = managerService.findManagerByUsername(p.getName());
		ModelAndView mav = new ModelAndView("incidencias/incidenciasDetails");
		mav.addObject(incidencia);
		if(!incidencia.getResidencia().getManager().equals(manager)) {
			mav = new ModelAndView("exception");
		}
		return mav;
	}

}
