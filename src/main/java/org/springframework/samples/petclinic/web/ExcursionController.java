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
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.service.ExcursionService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/excursiones")
public class ExcursionController {

	private static final String	VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM	= "excursiones/createOrUpdateExcursionForm";

	@Autowired
	private ExcursionService	excursionService;

	@Autowired
	private OrganizadorService	organizadorService;


	@InitBinder("organizador")
	public void initOrganizadorBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("excursion")
	public void initExcursionBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new ExcursionValidator());
	}

	@GetMapping()
	public String listExcursiones(final Map<String, Object> model, final Principal p) {
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		Iterable<Excursion> excursiones = this.excursionService.findAllMine(organizador);
		model.put("excursiones", excursiones);
		return "excursiones/excursionesList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Map<String, Object> model, final Principal p) {
		Excursion excursion = new Excursion();
		model.put("excursion", excursion);
		return ExcursionController.VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Excursion excursion, final BindingResult result, final Map<String, Object> model, final Principal p) {
		if (result.hasErrors()) {
			model.put("excursion", excursion);
			return ExcursionController.VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
			excursion.setOrganizador(organizador);
			excursion.setFinalMode(false);
			this.excursionService.saveExcursion(excursion);
			model.put("message", "Se ha registrado la excursion correctamente");
			return "redirect:/excursiones";
		}
	}

	@GetMapping(value = "/{excursionId}/edit")
	public String initUpdateExcursionForm(@PathVariable("excursionId") final int excursionId, final Model model) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		model.addAttribute(excursion);
		return ExcursionController.VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{excursionId}/edit")
	public String processUpdateExcursionForm(@Valid final Excursion excursion, final BindingResult result, @PathVariable("excursionId") final int excursionId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("excursion", excursion);
			return ExcursionController.VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			Excursion excursionToUpdate = this.excursionService.findExcursionById(excursionId);
			BeanUtils.copyProperties(excursion, excursionToUpdate, "id", "organizador");
			this.excursionService.saveExcursion(excursionToUpdate);
			return "redirect:/excursiones";
		}
	}

	@GetMapping("/{excursionId}")
	public ModelAndView showExcursion(@PathVariable("excursionId") final int excursionId) {
		ModelAndView mav = new ModelAndView("excursiones/excursionesDetails");
		mav.addObject(this.excursionService.findExcursionById(excursionId));
		return mav;
	}

}
