package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalTime;

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
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = ExcursionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ExcursionControllerTests {

	private static final int		TEST_EXCURSION_ID		= 4;

	private static final String		TEST_ORGANIZADOR_NOMBRE	= "organizador4";

	@Autowired
	private ExcursionController		excursionController;

	@MockBean
	private ExcursionService		excursionService;

	@MockBean
	private OrganizadorService		organizadorService;
	
	@MockBean
	private ManagerService 			managerService;

	@MockBean
	private AuthoritiesService 		authoritiesService;


	@Autowired
	private MockMvc					mockMvc;

	private Excursion				exc;
	private LocalDate				diaini					= LocalDate.now().plusDays(9);
	private LocalDate				diafin   				= LocalDate.now().plusDays(10);
	private LocalTime				horini   				= LocalTime.of(9, 0);
	private LocalTime				horfin   				= LocalTime.of(20, 0);
	Organizador						org						= new Organizador();
	User							user					= new User();


	@BeforeEach
	void setup() {
		this.user.setUsername(ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE);
		this.org.setUser(this.user);
		this.exc = new Excursion();
		this.exc.setId(ExcursionControllerTests.TEST_EXCURSION_ID);
		this.exc.setDescripcion("Prueba desc");
		this.exc.setTitulo("Prueba");
		this.exc.setFechaInicio(diaini);
		this.exc.setFechaFin(diafin);
		this.exc.setFinalMode(true);
		this.exc.setHoraInicio(horini);
		this.exc.setHoraFin(horfin);
		this.exc.setNumeroResidencias(6);
		this.exc.setOrganizador(this.org);
		this.exc.setRatioAceptacion(1.0);
		this.excursionService.saveExcursion(this.exc);
		BDDMockito.given(this.excursionService.findExcursionById(ExcursionControllerTests.TEST_EXCURSION_ID)).willReturn(this.exc);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.org);
		BDDMockito.given(this.authoritiesService.findAuthority(ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");
	}

	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("excursiones/excursionesList"));
	}

	@WithMockUser(roles = "organizador")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/new"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("excursion"));
	}

	@WithMockUser(roles = "organizador")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/new")
				.param("titulo", "Prueba")
				.param("descripcion", "Prueba descrip")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(authorities = "organizador")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/new")
				.param("titulo", "Prueba")
				.param("descripcion", "")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("excursion"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"));
	}

	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testShowExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}", ExcursionControllerTests.TEST_EXCURSION_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("excursion"))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("titulo", Matchers.is("Prueba"))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("fechaInicio", Matchers.is(this.diaini))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("fechaFin", Matchers.is(this.diafin))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("finalMode", Matchers.is(true))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("horaInicio", Matchers.is(this.horini))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("horaFin", Matchers.is(this.horfin))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("numeroResidencias", Matchers.is(6))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("ratioAceptacion", Matchers.is(1.0))))
			.andExpect(MockMvcResultMatchers.view().name("excursiones/excursionesDetails"));
	}

}