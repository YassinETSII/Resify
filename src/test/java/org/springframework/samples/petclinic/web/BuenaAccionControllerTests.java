
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.BuenaAccionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = BuenaAccionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class BuenaAccionControllerTests {

	private static final int		TEST_MANAGER_ID			= 1;

	private static final int		TEST_BUENA_ACCION_ID	= 1;

	@Autowired
	private BuenaAccionController	buenaAccionController;

	@MockBean
	private BuenaAccionService		buenaAccionService;

	@MockBean
	private ManagerService			managerService;

	@Autowired
	private MockMvc					mockMvc;

	private BuenaAccion ba;
	private Date hoy = new Date(System.currentTimeMillis() - 1);
	
	@BeforeEach
	void setup() {

		ba = new BuenaAccion();
		ba.setId(TEST_BUENA_ACCION_ID);
		ba.setTitulo("Prueba");
		ba.setDescripcion("Prueba desc");
		ba.setFecha(hoy);
		ba.setResidencia(new Residencia());
		given(this.buenaAccionService.findBuenaAccionById(TEST_BUENA_ACCION_ID)).willReturn(ba);

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("buenasAcciones/createOrUpdateBuenaAccionForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("buenaAccion"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/buenas-acciones/new")
			.param("titulo", "Prueba")
			.param("descripcion", "Prueba descrip")
			.with(csrf())
			.param("fecha", "2020/01/01"))
		.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/buenas-acciones/new")
						.param("titulo", "Prueba")
						.param("fecha", "2020/01/01"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("buenaAccion"))
			.andExpect(model().attributeHasFieldErrors("buenaAccion", "descripcion"))
			.andExpect(view().name("buenasAcciones/createOrUpdateBuenaAccionForm"));
	}
	
    @WithMockUser(value = "spring")
	@Test
	void testShowBuenaAccion() throws Exception {
		mockMvc.perform(get("/buenas-acciones/{buenaAccionId}", TEST_BUENA_ACCION_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("buenaAccion", hasProperty("descripcion", is("Prueba desc"))))
				.andExpect(model().attribute("buenaAccion", hasProperty("titulo", is("Prueba"))))
				.andExpect(model().attribute("buenaAccion", hasProperty("fecha", is(hoy))))
				.andExpect(view().name("buenas-acciones/buenasAccionesDetails"));
	}

}
