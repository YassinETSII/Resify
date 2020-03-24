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

package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.ResidenciaValidator;
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
@RequestMapping("/residencias")
public class ResidenciaController {

	private static final String	VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM	= "residencias/createOrUpdateResidenciaForm";

	@Autowired
	private ResidenciaService	residenciaService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private AncianoService		ancianoService;


	@InitBinder("manager")
	public void initManagerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("residencia")
	public void initResidenciaBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new ResidenciaValidator());
	}

	@GetMapping()
	public String listResidencias(final Map<String, Object> model, final Principal p) {
		Iterable<Residencia> residencias = this.residenciaService.findAll();
		model.put("residencias", residencias);
		return "residencias/residenciasList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Map<String, Object> model, final Principal p) {
		Residencia residencia = new Residencia();
		residencia.setAceptaDependenciaGrave(false);
		residencia.setAforo(10);
		model.put("residencia", residencia);
		return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Residencia residencia, final BindingResult result, final Map<String, Object> model, final Principal p) {
		if (result.hasErrors()) {
			model.put("residencia", residencia);
			return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
		} else {
			Manager manager = this.managerService.findManagerByUsername(p.getName());
			residencia.setManager(manager);
			this.residenciaService.saveResidencia(residencia);
			model.put("message", "Se ha registrado la residencia correctamente");
			return "redirect:/residencias";
		}
	}

	@GetMapping(value = "/{residenciaId}/edit")
	public String initUpdateResidenciaForm(@PathVariable("residenciaId") final int residenciaId, final Model model, final Principal p) {
		Residencia residencia = this.residenciaService.findResidenciaById(residenciaId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (!residencia.getManager().equals(manager)) {
			return "exception";
		}
		model.addAttribute(residencia);
		return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{residenciaId}/edit")
	public String processUpdateResidenciaForm(@Valid final Residencia residencia, final BindingResult result, @PathVariable("residenciaId") final int residenciaId, final ModelMap model, final Principal p) {
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (!residencia.getManager().equals(manager)) {
			return "exception";
		}
		if (result.hasErrors()) {
			model.put("residencia", residencia);
			return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
		} else {
			Residencia residenciaToUpdate = this.residenciaService.findResidenciaById(residenciaId);
			BeanUtils.copyProperties(residencia, residenciaToUpdate, "id", "manager");
			this.residenciaService.saveResidencia(residenciaToUpdate);
			return "redirect:/residencias";
		}
	}

	@GetMapping("/{residenciaId}")
	public ModelAndView showResidencia(@PathVariable("residenciaId") final int residenciaId, final Principal p) {
		Residencia residencia = this.residenciaService.findResidenciaById(residenciaId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
		ModelAndView mav = new ModelAndView("residencias/residenciasDetails");
		if (anciano == null && !residencia.getManager().equals(manager)) {
			mav = new ModelAndView("exception");
		}
		mav.addObject(this.residenciaService.findResidenciaById(residenciaId));
		return mav;
	}

}
