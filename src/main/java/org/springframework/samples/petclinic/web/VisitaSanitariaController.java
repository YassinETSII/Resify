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
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.service.VisitaSanitariaService;
import org.springframework.samples.petclinic.web.validators.VisitaSanitariaValidator;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/visitas-sanitarias")
public class VisitaSanitariaController {

	private static final String VIEWS_VISITA_SANITARIA_CREATE_OR_UPDATE_FORM = "visitasSanitarias/createOrUpdateVisitaSanitariaForm";

	@Autowired
	private VisitaSanitariaService visitaSanitariaService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ResidenciaService residenciaService;

	@Autowired
	private AncianoService ancianoService;

	@InitBinder("visitaSanitaria")
	public void initInscripcionBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("residencia");
		dataBinder.setDisallowedFields("fecha");
		dataBinder.addValidators(new VisitaSanitariaValidator());
	}

	@GetMapping()
	public String listVisitaSanitarias(Map<String, Object> model, Principal p) {
		Manager manager = managerService.findManagerByUsername(p.getName());
		if (residenciaService.findMine(manager) == null) {
			return "redirect:residencias/new";
		}
		Iterable<VisitaSanitaria> visitasSanitarias = visitaSanitariaService.findAllMine(manager);
		model.put("visitasSanitarias", visitasSanitarias);
		return "visitasSanitarias/visitasSanitariasList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model, Principal p) {
		VisitaSanitaria visitaSanitaria = new VisitaSanitaria();
		model.put("visitaSanitaria", visitaSanitaria);

		Residencia residencia = residenciaService.findMine(managerService.findManagerByUsername(p.getName()));
		Iterable<Anciano> misAncianos = ancianoService.findAncianosMiResidenciaConDependencia(residencia);
		if (!misAncianos.iterator().hasNext())
			model.put("noAncianosConDependencia", "* Su residencia no posee ningun anciano con dependencia grave");
		model.put("ancianos", misAncianos);
		return VIEWS_VISITA_SANITARIA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid VisitaSanitaria visitaSanitaria, BindingResult result,
			Map<String, Object> model, Principal p) {
		Residencia residencia = residenciaService.findMine(managerService.findManagerByUsername(p.getName()));
		Date hoy = new Date(System.currentTimeMillis() - 1);
		Anciano anciano = ancianoService.findAncianoById(visitaSanitaria.getAnciano().getId());

		visitaSanitaria.setResidencia(residencia);
		visitaSanitaria.setAnciano(anciano);
		visitaSanitaria.setFecha(hoy);

		if (!tienePermiso(p, visitaSanitaria) || !visitaSanitaria.getAnciano().getTieneDependenciaGrave())
			return "exception";

		if (result.hasErrors()) {
			Iterable<Anciano> misAncianos = ancianoService.findAncianosMiResidenciaConDependencia(residencia);

			model.put("ancianos", misAncianos);
			model.put("visitaSanitaria", visitaSanitaria);
			return VIEWS_VISITA_SANITARIA_CREATE_OR_UPDATE_FORM;
		} else {
			visitaSanitariaService.saveVisitaSanitaria(visitaSanitaria);
			model.put("message", "Se ha registrado la visita sanitaria correctamente");
			return "redirect:/visitas-sanitarias";
		}
	}

	@GetMapping("/{visitaSanitariaId}")
	public ModelAndView showVisitaSanitariaOrganizador(@PathVariable("visitaSanitariaId") int visitaSanitariaId,
			Principal p) {
		VisitaSanitaria visitaSanitaria = this.visitaSanitariaService.findVisitaSanitariaById(visitaSanitariaId);
		ModelAndView mav = new ModelAndView("visitasSanitarias/visitasSanitariasDetails");
		mav.addObject(visitaSanitaria);

		if (!tienePermiso(p, visitaSanitaria))
			return new ModelAndView("exception");

		return mav;
	}

	@GetMapping("/{visitaSanitariaId}/delete")
	public String deleteVisitaSanitaria(@PathVariable("visitaSanitariaId") int visitaSanitariaId, Principal p) {
		VisitaSanitaria visitaSanitaria = this.visitaSanitariaService.findVisitaSanitariaById(visitaSanitariaId);

		if (!tienePermiso(p, visitaSanitaria))
			return "exception";

		this.visitaSanitariaService.deleteVisitaSanitaria(visitaSanitaria);
		return "redirect:/visitas-sanitarias";
	}

	public boolean tienePermiso(Principal p, VisitaSanitaria v) {
		Residencia residenciaPrincipal = residenciaService.findMine(managerService.findManagerByUsername(p.getName()));
		Iterable<Anciano> misAncianos = ancianoService.findAncianosMiResidenciaConDependencia(residenciaPrincipal);
		boolean esMiAnciano = StreamSupport.stream(misAncianos.spliterator(), false)
				.anyMatch(anciano -> anciano.equals(v.getAnciano()));
		return v.getResidencia().equals(residenciaPrincipal) && esMiAnciano;
	}

}