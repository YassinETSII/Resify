
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
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ActividadService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = ActividadController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ActividadControllerTests {

	private static final int TEST_MANAGER_ID = 1;

	private static final int TEST_ACTIVIDAD_ID = 5;

	private static final String TEST_MANAGER_NOMBRE = "manager_test";

	@Autowired
	private ActividadController actividadController;

	@MockBean
	private ActividadService actividadService;

	@MockBean
	private ManagerService managerService;

	@Autowired
	private MockMvc mockMvc;

	private Actividad a;
//	private Date hoy = new Date(System.currentTimeMillis() - 1);
	private Residencia resi = new Residencia();
	private LocalDate diaini = LocalDate.now().plusDays(9);
	private LocalTime horini = LocalTime.of(9, 0);
	private LocalTime horfin = LocalTime.of(20, 0);
	Manager man = new Manager();
	User user = new User();

	@BeforeEach
	void setup() {
		this.user.setUsername(ActividadControllerTests.TEST_MANAGER_NOMBRE);
		this.man.setUser(this.user);
		this.resi.setManager(this.man);
		this.a = new Actividad();
		this.a.setId(ActividadControllerTests.TEST_ACTIVIDAD_ID);
		this.a.setTitulo("Prueba");
		this.a.setDescripcion("Prueba desc");
		this.a.setFechaInicio(this.diaini);
		this.a.setHoraInicio(horini);
		this.a.setHoraFin(horfin);
		this.a.setResidencia(this.resi);
		this.actividadService.saveActividad(this.a);
		BDDMockito.given(this.actividadService.findActividadById(ActividadControllerTests.TEST_ACTIVIDAD_ID))
				.willReturn(this.a);
		BDDMockito.given(this.managerService.findManagerByUsername(ActividadControllerTests.TEST_MANAGER_NOMBRE))
				.willReturn(this.man);
	}

	@WithMockUser(username = ActividadControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/actividades"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("actividades/actividadesList"));
	}

	@WithMockUser(roles = "manager")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/actividades/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("actividades/createOrUpdateActividadForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("actividad"));
	}

	@WithMockUser(roles = "manager")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/actividades/new").param("titulo", "Prueba")
						.param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("fechaInicio", "2030/01/01").param("horaInicio", "10:00").param("horaFin", "20:00"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(authorities = "manager")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/actividades/new")
				.param("titulo", "Prueba")
				.param("descripcion", "")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.param("horaFin", "20:00"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("actividad"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("actividades/createOrUpdateActividadForm"));
	}

	@WithMockUser(username = ActividadControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testShowBuenaAccion() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/actividades/{actividadId}",
						ActividadControllerTests.TEST_ACTIVIDAD_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("actividad"))
				.andExpect(MockMvcResultMatchers.model().attribute("actividad",
						Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
				.andExpect(MockMvcResultMatchers.model().attribute("actividad",
						Matchers.hasProperty("titulo", Matchers.is("Prueba"))))
				.andExpect(MockMvcResultMatchers.model().attribute("actividad",
						Matchers.hasProperty("fechaInicio", Matchers.is(this.diaini))))
				.andExpect(MockMvcResultMatchers.model().attribute("actividad",
						Matchers.hasProperty("horaInicio", Matchers.is(this.horini))))
				.andExpect(MockMvcResultMatchers.model().attribute("actividad",
						Matchers.hasProperty("horaFin", Matchers.is(this.horfin))))
				.andExpect(MockMvcResultMatchers.view().name("actividades/actividadesDetails"));
	}

}
