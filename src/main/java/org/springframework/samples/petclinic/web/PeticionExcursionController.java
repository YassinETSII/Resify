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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.PeticionExcursionValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	private static final String VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM = "peticionesExcursion/createOrUpdatePeticionExcursionForm";

	@Autowired
	private PeticionExcursionService peticionExcursionService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ExcursionService excursionService;

	@Autowired
	private OrganizadorService organizadorService;

	@Autowired
	private ResidenciaService residenciaService;

	@ModelAttribute("estados")
	public Collection<String> getEstados() {
		Collection<String> estados = new ArrayList<String>();
		estados.add("pendiente");
		estados.add("aceptada");
		estados.add("rechazada");
		return estados;
	}
	
	@InitBinder("peticionExcursion")
	public void initInscripcionBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("excursion");
		dataBinder.setDisallowedFields("fecha");
		dataBinder.setValidator(new PeticionExcursionValidator());
	}
	
	@GetMapping(value = "/peticiones-excursion")
	public String listPeticionesExcursion(final Map<String, Object> model, final Principal p) {
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		if (manager != null) {
			Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());
			Iterable<PeticionExcursion> peticionesExcursion = this.peticionExcursionService.findAllMineResidencia(residencia);
			model.put("peticionesExcursion", peticionesExcursion);
		} else if (organizador != null) {
			Iterable<PeticionExcursion> peticionesExcursion = this.peticionExcursionService.findAllMineOrganizador(organizador);
			model.put("peticionesExcursion", peticionesExcursion);
		} else {
			return "exception";
		}

		return "peticionesExcursion/peticionExcursionList";
	}


	@GetMapping(value = "/excursiones/{excursionId}/peticiones-excursion/new")
	public String initCreationForm(@PathVariable("excursionId") final int excursionId, final ModelMap model, final Principal p) {
		
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Manager manager = this.managerService.findManagerByUserName(p.getName());
		Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());
		Integer peticiones = this.managerService.countPeticionesByExcursion(excursion, manager);
		PeticionExcursion peticionExcursion = new PeticionExcursion();

		peticionExcursion.setEstado("pendiente");
		peticionExcursion.setExcursion(excursion);
		Date fecha = new Date(System.currentTimeMillis() - 1);
		peticionExcursion.setFecha(fecha);
		peticionExcursion.setResidencia(residencia);

		if (!(peticionExcursion.getExcursion().isFinalMode()) || peticionExcursion.getExcursion().getFechaInicio().isBefore(new Date(peticionExcursion.getFecha().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
			return "exception";
		}
		
		model.put("hasPeticion", peticiones != 0);
		model.put("hasResidencia", residencia != null);

		model.put("peticionExcursion", peticionExcursion);
		return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/excursiones/{excursionId}/peticiones-excursion/new")
	public String processCreationForm(@PathVariable("excursionId") final int excursionId,
			@Valid final PeticionExcursion peticionExcursion, 
			final BindingResult result, 
			final Map<String, Object> model, 
			final Principal p) {
		Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());

		Date fecha = new Date(System.currentTimeMillis() - 1);
		peticionExcursion.setFecha(fecha);
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		peticionExcursion.setExcursion(excursion);
		peticionExcursion.setResidencia(residencia);
		
		if (!(peticionExcursion.getExcursion().isFinalMode()) || peticionExcursion.getExcursion().getFechaInicio().isBefore(new Date(peticionExcursion.getFecha().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
			return "exception";
			
		} else if (result.hasErrors()) {
			Manager manager = this.managerService.findManagerByUserName(p.getName());
			Integer peticiones = this.managerService.countPeticionesByExcursion(excursion, manager);

			model.put("hasPeticion", peticiones != 0);
			model.put("hasResidencia", residencia != null);

			model.put("peticionExcursion", peticionExcursion);
			return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
	
		} else {
			peticionExcursionService.save(peticionExcursion);
			model.put("message", "Se ha enviado la peticion correctamente");
			return "redirect:/excursiones/{excursionId}";
		}
	}
	
	@GetMapping(value = "/peticiones-excursion/{peticionExcursionId}/edit")
	public String initUpdatePeticionExcursionForm(@PathVariable("peticionExcursionId") final int peticionExcursionId, final ModelMap model, final Principal p) {
		PeticionExcursion peticionExcursion = this.peticionExcursionService.findPeticionExcursionById(peticionExcursionId);
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		if (!(peticionExcursion.getExcursion().getOrganizador() == organizador) || peticionExcursion.getExcursion().getFechaInicio().isBefore(new Date(peticionExcursion.getFecha().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
			return "exception";
		}
		model.addAttribute(peticionExcursion);
		return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/peticiones-excursion/{peticionExcursionId}/edit")
	public String processUpdatePeticionExcursionForm(@Valid final PeticionExcursion peticionExcursion, final BindingResult result, @PathVariable("peticionExcursionId") final int peticionExcursionId, final ModelMap model, final Principal p) {
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		PeticionExcursion peticionExcursionToUpdate = this.peticionExcursionService.findPeticionExcursionById(peticionExcursionId);
		
		peticionExcursion.setExcursion(peticionExcursionToUpdate.getExcursion());
		if (!peticionExcursion.getExcursion().getOrganizador().equals(organizador) || peticionExcursionToUpdate.getExcursion().getFechaInicio().isBefore(new Date(peticionExcursionToUpdate.getFecha().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
			return "exception";
		}
		if (peticionExcursion.getEstado().equals("aceptada")) {
			if (residenciaService.getRatio(peticionExcursionToUpdate.getResidencia()) < peticionExcursionToUpdate.getExcursion().getRatioAceptacion()) {
				result.rejectValue("estado", "la excursion no acepta residencias con un ratio menor de " + peticionExcursionToUpdate.getExcursion().getRatioAceptacion() , "la excursion no acepta residencias con un ratio menor de " + peticionExcursionToUpdate.getExcursion().getRatioAceptacion());
			}
			if (peticionExcursionService.countPeticionExcursionAceptadaByExcursion(peticionExcursion.getExcursion())>=peticionExcursion.getExcursion().getNumeroResidencias()) {
				result.rejectValue("estado", "la excursión no acepta más residencias, número máximo de residencias alcanzado", "la excursión no acepta más residencias, número máximo de residencias alcanzado");
			}
		}
		if (result.hasErrors()) {
			model.put("peticionExcursion", peticionExcursion);
			return VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			BeanUtils.copyProperties(peticionExcursion, peticionExcursionToUpdate, "id", "residencia", "excursion", "fecha");
			
			this.peticionExcursionService.save(peticionExcursionToUpdate);
			return "redirect:/peticiones-excursion/";
		}
	}

}