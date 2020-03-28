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

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OrganizadorService;
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
@RequestMapping("/organizadores")
public class OrganizadorController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuarioForm";

	private final OrganizadorService organizadorService;

	private final AuthoritiesService authService;

	@Autowired
	public OrganizadorController(OrganizadorService organizadorService, AuthoritiesService authoritiesService) {
		this.organizadorService = organizadorService;
		this.authService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping()
	public String listOrganizadors(Map<String, Object> model) {
		Iterable<Organizador> organizadores = organizadorService.findOrganizadores();
		model.put("organizadores", organizadores);
		return "usuarios/usuariosList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		Organizador organizador = new Organizador();
		model.put("organizador", organizador);
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Organizador organizador, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			model.put("organizador", organizador);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		} else {
			authService.saveAuthorities(organizador.getUser().getUsername(), "organizador");
			organizadorService.saveOrganizador(organizador);
			model.put("message", "Se ha registrado el organizador correctamente");
			return "redirect:/organizadores";
		}
	}

	@GetMapping(value = "/{organizadorId}/edit")
	public String initUpdateOrganizadorForm(@PathVariable("organizadorId") int organizadorId, Model model) {
		Organizador organizador = this.organizadorService.findOrganizadorById(organizadorId);
		model.addAttribute(organizador);
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{organizadorId}/edit")
	public String processUpdateOrganizadorForm(@Valid Organizador organizador, BindingResult result,
			@PathVariable("organizadorId") int organizadorId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("organizador", organizador);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		} else {
			Organizador organizadorToUpdate = this.organizadorService.findOrganizadorById(organizadorId);
			BeanUtils.copyProperties(organizador, organizadorToUpdate, "id", "user");
			this.organizadorService.saveOrganizador(organizadorToUpdate);
			return "redirect:/organizadores/{organizadorId}";
		}
	}

	@GetMapping("/{organizadorId}")
	public ModelAndView showActividad(@PathVariable("organizadorId") int organizadorId) {
		Organizador organizador = this.organizadorService.findOrganizadorById(organizadorId);
		ModelAndView mav = new ModelAndView("usuarios/usuariosDetails");
		mav.addObject(organizador);
		return mav;
	}

}
