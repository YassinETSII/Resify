
package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
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
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = PeticionExcursionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class PeticionExcursionControllerTests {

	private static final int TEST_MANAGER_ID = 1;

	private static final int TEST_PETICION_EXCURSION_ID = 1;

	private static final int TEST_RESIDENCIA_ID = 1;

	private static final int TEST_EXCURSION_ID = 1;

	private static final String TEST_MANAGER_NOMBRE = "manager_test";

	private static final String TEST_ORGANIZADOR_NOMBRE = "organizador_test";

	@Autowired
	private PeticionExcursionController peticionExcursionController;

	@MockBean
	private PeticionExcursionService peticionExcursionService;

	@MockBean
	private ManagerService managerService;

	@MockBean
	private ExcursionService excursionService;

	@MockBean
	private OrganizadorService organizadorService;

	@MockBean
	private ResidenciaService residenciaService;

	@Autowired
	private MockMvc mockMvc;

	private PeticionExcursion pe;
	private Date hoy = new Date(System.currentTimeMillis() - 1);
	private LocalDate diapost = LocalDate.now().plusDays(10);
	private Residencia resi = new Residencia();
	private Excursion exc = new Excursion();
	Manager man = new Manager();
	Organizador org = new Organizador();
	User userM = new User();
	User userO = new User();

	@BeforeEach
	void setup() {
		this.userM.setUsername(PeticionExcursionControllerTests.TEST_MANAGER_NOMBRE);
		this.userO.setUsername(PeticionExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE);
		this.man.setUser(this.userM);
		this.org.setUser(this.userO);
		this.resi.setManager(this.man);
		this.exc.setOrganizador(this.org);
		this.exc.setFinalMode(true);
		this.exc.setFechaInicio(diapost);
		this.pe = new PeticionExcursion();
		this.pe.setId(PeticionExcursionControllerTests.TEST_PETICION_EXCURSION_ID);
		this.pe.setDeclaracion("Prueba dec");
		this.pe.setEstado("pendiente");
		this.pe.setFecha(this.hoy);
		this.pe.setJustificacion(null);
		this.pe.setResidencia(this.resi);
		this.pe.setExcursion(this.exc);
		this.peticionExcursionService.save(this.pe);
		BDDMockito
				.given(this.peticionExcursionService
						.findPeticionExcursionById(PeticionExcursionControllerTests.TEST_PETICION_EXCURSION_ID))
				.willReturn(this.pe);
		BDDMockito
				.given(this.managerService.findManagerByUsername(PeticionExcursionControllerTests.TEST_MANAGER_NOMBRE))
				.willReturn(this.man);
		BDDMockito
				.given(this.organizadorService
						.findOrganizadorByUsername(PeticionExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE))
				.willReturn(this.org);
		BDDMockito.given(this.excursionService.findExcursionById(PeticionExcursionControllerTests.TEST_EXCURSION_ID))
				.willReturn(this.exc);
		BDDMockito.given(this.residenciaService.findResidenciaById(PeticionExcursionControllerTests.TEST_RESIDENCIA_ID))
				.willReturn(this.resi);
	}

	@WithMockUser(username = PeticionExcursionControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessFindFormSuccessManager() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/peticionExcursionList"));
	}
	
	@WithMockUser(username = PeticionExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessFindFormSuccessOrganizador() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/peticionExcursionList"));
	}

	@WithMockUser(roles = "manager_test")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/peticiones-excursion/new",
						TEST_EXCURSION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/createOrUpdatePeticionExcursionForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("peticionExcursion"));
	}

//	@WithMockUser(roles = "manager_test")
//	@Test
//	void testProcessCreationFormSuccess() throws Exception {
//		this.mockMvc
//				.perform(MockMvcRequestBuilders
//						.post("/excursiones/{excursionId}/peticiones-excursion/new", TEST_EXCURSION_ID)
//						.param("declaracion", "Prueba declaracion"))
//				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//	}
	
	@WithMockUser(username = PeticionExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/peticiones-excursion/{peticionExcursionId}/edit", TEST_PETICION_EXCURSION_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists("peticionExcursion"))
				.andExpect(view().name("peticionesExcursion/createOrUpdatePeticionExcursionForm"));
	}
	
//	@WithMockUser(username = PeticionExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
//	@Test
//	void testProcessUpdateFormSuccess() throws Exception {
//		mockMvc.perform(post("/peticiones-excursion/{peticionExcursionId}/edit", TEST_PETICION_EXCURSION_ID)
//							.with(csrf())
//							.param("estado", "aceptada"))
//				.andExpect(status().is3xxRedirection())
//				.andExpect(view().name("redirect:/peticiones-excursion/"));
//	}

//	@WithMockUser(authorities = "manager_test")
//	@Test
//	void testProcessCreationFormHasErrors() throws Exception {
//		this.mockMvc
//				.perform(MockMvcRequestBuilders
//						.post("/excursiones/{excursionId}/peticiones-excursion/new", TEST_EXCURSION_ID)
//						.param("declaracion", "").param("estado", "pendiente"))
//				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("peticionExcursion"))
//				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(
//						MockMvcResultMatchers.view().name("peticionesExcursion/createOrUpdatePeticionExcursionForm"));
//	}

//	@WithMockUser(username = PeticionExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
//	@Test
//	void testShowBuenaAccion() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion/{buenaId}", PeticionExcursionControllerTests.TEST_BUENA_ACCION_ID)).andExpect(MockMvcResultMatchers.status().isOk())
//			.andExpect(MockMvcResultMatchers.model().attributeExists("buenaAccion")).andExpect(MockMvcResultMatchers.model().attribute("buenaAccion", Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
//			.andExpect(MockMvcResultMatchers.model().attribute("buenaAccion", Matchers.hasProperty("titulo", Matchers.is("Prueba")))).andExpect(MockMvcResultMatchers.model().attribute("buenaAccion", Matchers.hasProperty("fecha", Matchers.is(this.hoy))))
//			.andExpect(MockMvcResultMatchers.view().name("buenasAcciones/buenasAccionesDetails"));
//	}

}
