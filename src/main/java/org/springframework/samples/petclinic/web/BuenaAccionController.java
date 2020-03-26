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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.BuenaAccionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.web.validators.BuenaAccionValidator;
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
@RequestMapping("/buenas-acciones")
public class BuenaAccionController {

	private static final String	VIEWS_BUENA_ACCION_CREATE_OR_UPDATE_FORM	= "buenasAcciones/createOrUpdateBuenaAccionForm";

	@Autowired
	private BuenaAccionService	buenaAccionService;

	@Autowired
	private ManagerService		managerService;


	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("manager")
	public void initManagerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("buenaAccion")
	public void initBuenaAccionBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new BuenaAccionValidator());
	}

	@GetMapping()
	public String listBuenasAcciones(final Map<String, Object> model, final Principal p) {
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Iterable<BuenaAccion> buenasAcciones = this.buenaAccionService.findAllMine(manager);
		model.put("buenasAcciones", buenasAcciones);
		return "buenasAcciones/buenasAccionesList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Map<String, Object> model, final Principal p) {
		BuenaAccion buenaAccion = new BuenaAccion();
		model.put("buenaAccion", buenaAccion);
		return BuenaAccionController.VIEWS_BUENA_ACCION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final BuenaAccion buenaAccion, final BindingResult result, final Map<String, Object> model, final Principal p) {
		if (result.hasErrors()) {
			model.put("buenaAccion", buenaAccion);
			return BuenaAccionController.VIEWS_BUENA_ACCION_CREATE_OR_UPDATE_FORM;
		} else {
			Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());
			buenaAccion.setResidencia(residencia);
			this.buenaAccionService.saveBuenaAccion(buenaAccion);
			model.put("message", "Se ha registrado la buena acción correctamente");
			return "redirect:/buenas-acciones";
		}
	}

	@GetMapping("/{buenaAccionId}")
	public ModelAndView showBuenaAccion(@PathVariable("buenaAccionId") final int buenaAccionId, final Principal p) {
		BuenaAccion buenaAccion = this.buenaAccionService.findBuenaAccionById(buenaAccionId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		ModelAndView mav = new ModelAndView("buenasAcciones/buenasAccionesDetails");
		mav.addObject(buenaAccion);
		if (!buenaAccion.getResidencia().getManager().equals(manager)) {
			mav = new ModelAndView("exception");
		}
		return mav;
	}

}
