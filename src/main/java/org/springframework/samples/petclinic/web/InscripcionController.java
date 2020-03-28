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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.InscripcionValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/inscripciones")
public class InscripcionController {

	private static final String	VIEWS_INSCRIPCION_CREATE_OR_UPDATE_FORM	= "inscripciones/createOrUpdateInscripcionForm";

	@Autowired
	private InscripcionService	inscripcionService;

	@Autowired
	private ResidenciaService	residenciaService;

	@Autowired
	private AncianoService		ancianoService;

	@Autowired
	private ManagerService		managerService;

	//Objetos para el validate
	private Residencia			res;
	private Anciano				anc;


	@ModelAttribute("estados")
	public Collection<String> getEstados() {
		Collection<String> estados = new ArrayList<String>();
		estados.add("pendiente");
		estados.add("aceptada");
		estados.add("rechazada");
		return estados;
	}

	@InitBinder("inscripcion")
	public void initInscripcionBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("residencia");
		dataBinder.setDisallowedFields("fecha");
		dataBinder.setValidator(new InscripcionValidator(this.res, this.anc));
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping()
	public String listInscripciones(final Map<String, Object> model, final Principal p) {
		Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (anciano != null) {
			Iterable<Inscripcion> inscripciones = this.inscripcionService.findAllMineAnciano(anciano);
			model.put("inscripciones", inscripciones);
		} else if (manager != null) {
			Iterable<Inscripcion> inscripciones = this.inscripcionService.findAllMineManager(manager);
			model.put("inscripciones", inscripciones);
		} else {
			return "exception";
		}

		return "inscripciones/inscripcionesList";
	}

	@GetMapping(value = "/new/{residenciaId}")
	public String initCreationForm(final ModelMap model, final Principal p, @PathVariable("residenciaId") final int residenciaId) {
		Inscripcion inscripcion = new Inscripcion();
		inscripcion.setEstado("pendiente");
		Residencia residencia = this.residenciaService.findResidenciaById(residenciaId);
		inscripcion.setResidencia(residencia);
		model.put("inscripcion", inscripcion);
		return InscripcionController.VIEWS_INSCRIPCION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new/{residenciaId}")
	public String processCreationForm(@Valid final Inscripcion inscripcion, final BindingResult result, final ModelMap model, final Principal p, @PathVariable("residenciaId") final int residenciaId) {
		Date hoy = new Date(System.currentTimeMillis() - 1);
		inscripcion.setFecha(hoy);
		Residencia residencia = this.residenciaService.findResidenciaById(residenciaId);
		inscripcion.setResidencia(residencia);
		if (result.hasErrors()) {
			model.put("inscripcion", inscripcion);
			return InscripcionController.VIEWS_INSCRIPCION_CREATE_OR_UPDATE_FORM;
		} else {
			Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
			inscripcion.setAnciano(anciano);
			this.inscripcionService.saveInscripcion(inscripcion);
			model.put("message", "Se ha registrado la inscripcion correctamente");
			return "redirect:/inscripciones";
		}
	}

	@GetMapping(value = "/{inscripcionId}/edit")
	public String initUpdateInscripcionForm(@PathVariable("inscripcionId") final int inscripcionId, final ModelMap model, final Principal p) {
		Inscripcion inscripcion = this.inscripcionService.findInscripcionById(inscripcionId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (!(inscripcion.getResidencia().getManager() == manager)) {
			return "exception";
		}
		model.addAttribute(inscripcion);
		this.res = inscripcion.getResidencia();
		this.anc = inscripcion.getAnciano();
		return InscripcionController.VIEWS_INSCRIPCION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{inscripcionId}/edit")
	public String processUpdateInscripcionForm(@Valid final Inscripcion inscripcion, final BindingResult result, @PathVariable("inscripcionId") final int inscripcionId, final ModelMap model, final Principal p) {
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Inscripcion inscripcionToUpdate = this.inscripcionService.findInscripcionById(inscripcionId);
		inscripcion.setResidencia(inscripcionToUpdate.getResidencia());
		inscripcion.setFecha(inscripcionToUpdate.getFecha());
		inscripcion.setId(inscripcionToUpdate.getId());
		if (!inscripcion.getResidencia().getManager().equals(manager)) {
			return "exception";
		}
		if (result.hasErrors()) {
			model.put("inscripcion", inscripcion);
			return InscripcionController.VIEWS_INSCRIPCION_CREATE_OR_UPDATE_FORM;
		} else {
			BeanUtils.copyProperties(inscripcion, inscripcionToUpdate, "id", "anciano", "residencia", "fecha");
			this.inscripcionService.saveInscripcion(inscripcionToUpdate);
			return "redirect:/inscripciones";
		}
	}

	@GetMapping("/{inscripcionId}")
	public ModelAndView showInscripcion(@PathVariable("inscripcionId") final int inscripcionId, final Principal p) {
		Inscripcion inscripcion = this.inscripcionService.findInscripcionById(inscripcionId);
		Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		ModelAndView mav = new ModelAndView("inscripciones/inscripcionesDetails");

		if (anciano != null && !inscripcion.getAnciano().equals(anciano) || manager != null && !inscripcion.getResidencia().getManager().equals(manager)) {
			mav = new ModelAndView("exception");
		}
		mav.addObject(this.inscripcionService.findInscripcionById(inscripcionId));
		return mav;
	}

}
