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
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
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
@RequestMapping("/managers")
public class ManagerController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuarioForm";

	private final ManagerService managerService;

	private final AuthoritiesService authService;

	@Autowired
	public ManagerController(ManagerService managerService, AuthoritiesService authoritiesService) {
		this.managerService = managerService;
		this.authService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

//	@GetMapping()
//	public String listManagers(Map<String, Object> model) {
//		Iterable<Manager> managers = managerService.findManagers();
//		model.put("managers", managers);
//		return "usuarios/usuariosList";
//	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		Manager manager = new Manager();
		model.put("manager", manager);
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Manager manager, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			model.put("manager", manager);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		} else {
			authService.saveAuthorities(manager.getUser().getUsername(), "manager");
			managerService.saveManager(manager);
			return "redirect:/";
		}
	}

//	@GetMapping(value = "/{managerId}/edit")
//	public String initUpdateManagerForm(@PathVariable("managerId") int managerId, Model model) {
//		Manager manager = this.managerService.findManagerById(managerId);
//		model.addAttribute(manager);
//		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
//	}
//
//	@PostMapping(value = "/{managerId}/edit")
//	public String processUpdateManagerForm(@Valid Manager manager, BindingResult result,
//			@PathVariable("managerId") int managerId, final ModelMap model) {
//		if (result.hasErrors()) {
//			model.put("manager", manager);
//			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
//		} else {
//			Manager managerToUpdate = this.managerService.findManagerById(managerId);
//			BeanUtils.copyProperties(manager, managerToUpdate, "id", "user");
//			this.managerService.saveManager(managerToUpdate);
//			return "redirect:/managers/{managerId}";
//		}
//	}
//
//	@GetMapping("/{managerId}")
//	public ModelAndView showActividad(@PathVariable("managerId") int managerId) {
//		Manager manager = this.managerService.findManagerById(managerId);
//		ModelAndView mav = new ModelAndView("usuarios/usuariosDetails");
//		mav.addObject(manager);
//		return mav;
//	}

}
