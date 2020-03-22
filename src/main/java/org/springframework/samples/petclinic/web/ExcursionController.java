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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.web.validators.ExcursionValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class ExcursionController {

	private static final String VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM = "excursiones/createOrUpdateExcursionForm";

	@Autowired
	private ExcursionService excursionService;

	@Autowired
	private OrganizadorService organizadorService;

	@Autowired
	private ManagerService managerService;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("organizador")
	public void initOrganizadorBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("excursion")
	public void initExcursionBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ExcursionValidator());
	}

	@GetMapping("/organizador/excursiones")
	public String listExcursionesOrganizador(Map<String, Object> model, Principal p) {
		Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
		Iterable<Excursion> excursiones = excursionService.findAllMine(organizador);
		model.put("excursiones", excursiones);
		return "excursiones/excursionesList";
	}

	@GetMapping("/manager/excursiones")
	public String listExcursionesManager(Map<String, Object> model, Principal p) {
		Iterable<Excursion> excursiones = excursionService.findAllPublished();
		model.put("excursiones", excursiones);
		return "excursiones/excursionesList";
	}

	@GetMapping(value = "/organizador/excursiones/new")
	public String initCreationForm(Map<String, Object> model, Principal p) {
		Excursion excursion = new Excursion();
		model.put("excursion", excursion);
		return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/organizador/excursiones/new")
	public String processCreationForm(@Valid Excursion excursion, BindingResult result, Map<String, Object> model,
			Principal p) {
		if (result.hasErrors()) {
			model.put("excursion", excursion);
			return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
			excursion.setOrganizador(organizador);
			excursionService.saveExcursion(excursion);
			model.put("message", "Se ha registrado la excursion correctamente");
			return "redirect:/organizador/excursiones";
		}
	}

	@GetMapping(value = "/organizador/excursiones/{excursionId}/edit")
	public String initUpdateExcursionForm(@PathVariable("excursionId") int excursionId, Model model) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		model.addAttribute(excursion);
		return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/organizador/excursiones/{excursionId}/edit")
	public String processUpdateExcursionForm(@Valid Excursion excursion, BindingResult result,
			@PathVariable("excursionId") int excursionId, final ModelMap model, Principal p) {
		if (result.hasErrors()) {
			model.put("excursion", excursion);
			return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
			Excursion excursionToUpdate = this.excursionService.findExcursionById(excursionId);
			if (!excursionToUpdate.getOrganizador().equals(organizador) || excursionToUpdate.isFinalMode()) {
				return "exception";
			}
			BeanUtils.copyProperties(excursion, excursionToUpdate, "id", "organizador");
			this.excursionService.saveExcursion(excursionToUpdate);
			return "redirect:/organizador/excursiones/{excursionId}";
		}
	}

	@GetMapping("/organizador/excursiones/{excursionId}")
	public ModelAndView showExcursionOrganizador(@PathVariable("excursionId") int excursionId, Principal p) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
		ModelAndView mav = new ModelAndView("excursiones/excursionesDetails");
		mav.addObject(excursion);
		if (!excursion.getOrganizador().equals(organizador)) {
			mav = new ModelAndView("exception");
		}
		return mav;
	}

	@GetMapping("/manager/excursiones/{excursionId}")
	public ModelAndView showExcursionManager(@PathVariable("excursionId") int excursionId, Map<String, Object> model,
			Principal p) {
		ModelAndView mav = new ModelAndView("excursiones/excursionesDetails");
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		mav.addObject(excursion);
		Manager manager = managerService.findManagerByUserName(p.getName());
		Integer peticiones = managerService.countPeticionesByExcursion(excursion, manager);
		Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());
		model.put("hasPeticion", peticiones != 0);
		model.put("hasResidencia", residencia != null);
		return mav;
	}

	@GetMapping("/organizador/{excursionId}/delete")
	public String deleteExcursion(@PathVariable("excursionId") int excursionId, Principal p) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
		if (!excursion.getOrganizador().equals(organizador) || excursion.isFinalMode()) {
			return "exception";
		}
		this.excursionService.deleteExcursion(excursion);
		return "redirect:/organizador/excursiones";
	}

}