
package org.springframework.samples.petclinic.web;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Incidencia;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.IncidenciaService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = IncidenciaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class IncidenciaControllerTest {

	private static final int		TEST_MANAGER_ID			= 1;

	private static final int		TEST_BUENA_ACCION_ID	= 1;

	private static final String		TEST_MANAGER_NOMBRE		= "manager";

	@Autowired
	private IncidenciaController	incidenciaController;

	@MockBean
	private IncidenciaService		incidenciaService;

	@MockBean
	private ManagerService			managerService;

	@Autowired
	private MockMvc					mockMvc;

	private Incidencia				ba;
	private Date					hoy						= new Date(System.currentTimeMillis() - 1);
	private Residencia				resi					= new Residencia();
	Manager							man						= new Manager();
	User							user					= new User();


	@BeforeEach
	void setup() {
		this.user.setUsername(IncidenciaControllerTest.TEST_MANAGER_NOMBRE);
		this.man.setUser(this.user);
		this.resi.setManager(this.man);
		this.ba = new Incidencia();
		this.ba.setId(IncidenciaControllerTest.TEST_BUENA_ACCION_ID);
		this.ba.setTitulo("Prueba");
		this.ba.setDescripcion("Prueba desc");
		this.ba.setFecha(this.hoy);
		this.ba.setResidencia(this.resi);
		this.incidenciaService.saveIncidencia(this.ba);
		BDDMockito.given(this.incidenciaService.findIncidenciaById(IncidenciaControllerTest.TEST_BUENA_ACCION_ID)).willReturn(this.ba);
		BDDMockito.given(this.managerService.findManagerByUsername(IncidenciaControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.man);
	}

	@WithMockUser(username = "manager1")
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("incidencias/incidenciasList"));
	}

	@WithMockUser(roles = "manager")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("incidencias/createOrUpdateIncidenciaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("incidencia"));
	}

	@WithMockUser(roles = "manager")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/incidencias/new").param("titulo", "Prueba").param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(authorities = "manager")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/incidencias/new").param("titulo", "Prueba").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01").param("descripcion", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("incidencia")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("incidencias/createOrUpdateIncidenciaForm"));
	}

	@WithMockUser(username = IncidenciaControllerTest.TEST_MANAGER_NOMBRE)
	@Test
	void testShowIncidencia() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/{incidenciaId}", IncidenciaControllerTest.TEST_BUENA_ACCION_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("incidencia"))
			.andExpect(MockMvcResultMatchers.model().attribute("incidencia", Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
			.andExpect(MockMvcResultMatchers.model().attribute("incidencia", Matchers.hasProperty("titulo", Matchers.is("Prueba")))).andExpect(MockMvcResultMatchers.model().attribute("incidencia", Matchers.hasProperty("fecha", Matchers.is(this.hoy))))
			.andExpect(MockMvcResultMatchers.view().name("incidencias/incidenciasDetails"));
	}

}
