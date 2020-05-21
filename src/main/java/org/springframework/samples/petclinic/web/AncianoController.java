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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@RequestMapping("/ancianos")
public class AncianoController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuarioForm";

	private final AncianoService ancianoService;

	private final AuthoritiesService authService;

	@Autowired
	public AncianoController(AncianoService ancianoService, AuthoritiesService authoritiesService) {
		this.ancianoService = ancianoService;
		this.authService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

//	@GetMapping()
//	public String listAncianos(Map<String, Object> model) {
//		Iterable<Anciano> ancianos = ancianoService.findAncianos();
//		model.put("ancianos", ancianos);
//		return "usuarios/usuariosList";
//	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		Anciano anciano = new Anciano();
		model.put("anciano", anciano);
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Anciano anciano, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			model.put("anciano", anciano);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		} else {
			authService.saveAuthorities(anciano.getUser().getUsername(), "anciano");
			ancianoService.saveAnciano(anciano);
			return "redirect:/";
		}
	}

//	@GetMapping(value = "/{ancianoId}/edit")
//	public String initUpdateAncianoForm(@PathVariable("ancianoId") int ancianoId, Model model) {
//		Anciano anciano = this.ancianoService.findAncianoById(ancianoId);
//		model.addAttribute(anciano);
//		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
//	}
//
//	@PostMapping(value = "/{ancianoId}/edit")
//	public String processUpdateAncianoForm(@Valid Anciano anciano, BindingResult result,
//			@PathVariable("ancianoId") int ancianoId, final ModelMap model) {
//		if (result.hasErrors()) {
//			model.put("anciano", anciano);
//			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
//		} else {
//			Anciano ancianoToUpdate = this.ancianoService.findAncianoById(ancianoId);
//			BeanUtils.copyProperties(anciano, ancianoToUpdate, "id", "user");
//			this.ancianoService.saveAnciano(ancianoToUpdate);
//			return "redirect:/ancianos/{ancianoId}";
//		}
//	}
//
//	@GetMapping("/{ancianoId}")
//	public ModelAndView showActividad(@PathVariable("ancianoId") int ancianoId) {
//		Anciano anciano = this.ancianoService.findAncianoById(ancianoId);
//		ModelAndView mav = new ModelAndView("usuarios/usuariosDetails");
//		mav.addObject(anciano);
//		return mav;
//	}

}
