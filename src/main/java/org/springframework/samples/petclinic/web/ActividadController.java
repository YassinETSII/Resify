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
import java.util.ArrayList;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.ActividadService;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.web.validators.ActividadValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/actividades")
public class ActividadController {

	private static final String VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM = "actividades/createOrUpdateActividadForm";

	@Autowired
	private ActividadService actividadService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private AncianoService ancianoService;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("manager")
	public void initManagerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("actividad")
	public void initActividadBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ActividadValidator());
	}

	@GetMapping()
	public String listActividades(Map<String, Object> model, Principal p) {
		String authority = authoritiesService.findAuthority(p.getName());
		Iterable<Actividad> actividades = new ArrayList<>();
		if (authority.equals("manager")) {
			Manager manager = managerService.findManagerByUsername(p.getName());
			actividades = actividadService.findAllMine(manager);
		} else if (authority.equals("anciano")) {
			Anciano anciano = ancianoService.findAncianoByUsername(p.getName());
			actividades = actividadService.findAllMineAnciano(anciano);
		}
		model.put("actividades", actividades);
		return "actividades/actividadesList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model, Principal p) {
		Actividad actividad = new Actividad();
		model.put("actividad", actividad);
		return VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Actividad actividad, BindingResult result, Map<String, Object> model,
			Principal p) {
		if (result.hasErrors()) {
			model.put("actividad", actividad);
			return VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
		} else {
			Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());
			actividad.setResidencia(residencia);
			actividadService.saveActividad(actividad);
			model.put("message", "Se ha registrado la actividad correctamente");
			return "redirect:/actividades";
		}
	}

	@GetMapping(value = "/{actividadId}/edit")
	public String initUpdateActividadForm(@PathVariable("actividadId") int actividadId, Model model) {
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		model.addAttribute(actividad);
		return VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{actividadId}/edit")
	public String processUpdateActividadForm(@Valid Actividad actividad, BindingResult result,
			@PathVariable("actividadId") int actividadId, final ModelMap model, Principal p) {
		if (result.hasErrors()) {
			model.put("actividad", actividad);
			return VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
		} else {
			Manager manager = managerService.findManagerByUsername(p.getName());
			Actividad actividadToUpdate = this.actividadService.findActividadById(actividadId);
			if (!actividadToUpdate.getResidencia().getManager().equals(manager)) {
				return "exception";
			}
			BeanUtils.copyProperties(actividad, actividadToUpdate, "id", "residencia");
			this.actividadService.saveActividad(actividadToUpdate);
			return "redirect:/actividades/{actividadId}";
		}
	}

	@GetMapping("/{actividadId}")
	public ModelAndView showActividad(@PathVariable("actividadId") int actividadId, Principal p) {
		String authority = authoritiesService.findAuthority(p.getName());
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		ModelAndView mav = new ModelAndView("actividades/actividadesDetails");
		mav.addObject(actividad);
		
		if (authority.equals("manager")) {
			Manager manager = managerService.findManagerByUsername(p.getName());
			if (!actividad.getResidencia().getManager().equals(manager)) {
				mav = new ModelAndView("exception");
			}
		}else if(authority.equals("anciano")) {
			Anciano anciano = ancianoService.findAncianoByUsername(p.getName());
			Iterable<Actividad> actividades = actividadService.findAllMineAnciano(anciano);
			boolean suya = false;
			for (Actividad a : actividades) {
				if(actividad.getId() == a.getId()) {
					suya = true;
					break;
				}
			}
			if (suya == false) {
				mav = new ModelAndView("exception");
			}
		}
		
		return mav;
	}

	@GetMapping("/{actividadId}/delete")
	public String deleteExcursion(@PathVariable("actividadId") int actividadId, Principal p) {
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		Manager manager = managerService.findManagerByUsername(p.getName());
		if (!actividad.getResidencia().getManager().equals(manager)) {
			return "exception";
		}
		this.actividadService.deleteActividad(actividad);
		return "redirect:/actividades";
	}

}
