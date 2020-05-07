
package org.springframework.samples.petclinic.web;

import java.time.LocalTime;
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
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = ResidenciaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ResidenciaControllerTest {

	private static final int		TEST_RESIDENCIA_ID	= 1;

	private static final String		TEST_MANAGER_NOMBRE		= "manager";
	private static final String		TEST_ORGANIZADOR_NOMBRE		= "organizador";
	private static final String		TEST_ANCIANO_NOMBRE		= "anciano";


	@MockBean
	private ResidenciaService		residenciaService;

	@MockBean
	private ManagerService			managerService;

	@MockBean
	private AncianoService		ancianoService;

	@MockBean
	private InscripcionService	inscripcionService;

	@MockBean
	private OrganizadorService	organizadorService;
	
	@MockBean
	private AuthoritiesService			authoritiesService;

	@Autowired
	private MockMvc					mockMvc;

	private Residencia				resi;
	Manager							man						= new Manager();
	Organizador 					organ 					= new Organizador();
	Anciano 						anc 					= new Anciano();
	User							user					= new User();
	User							userOrganiza			= new User();
	User							userAnciano				= new User();
	


	@BeforeEach
	void setup() {
		this.user.setUsername(ResidenciaControllerTest.TEST_MANAGER_NOMBRE);
		this.userOrganiza.setUsername(ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE);
		this.userAnciano.setUsername(ResidenciaControllerTest.TEST_ANCIANO_NOMBRE);
		
		this.man.setUser(this.user);
		this.anc.setUser(this.userAnciano);
		this.organ.setUser(this.userOrganiza);
		
		this.resi = new Residencia();
		this.resi.setId(ResidenciaControllerTest.TEST_RESIDENCIA_ID);
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		this.resi.setManager(this.man);
		this.resi.setAceptaDependenciaGrave(false);
		this.resi.setAforo(100);
		this.resi.setCorreo("residencia1@mail.es");
		this.resi.setDescripcion("Descripcion de prueba");
		this.resi.setDireccion("Direccion");
		this.resi.setEdadMaxima(76);
		this.resi.setHoraApertura(horaApertura);
		this.resi.setHoraCierre(horaCierre);
		this.resi.setMasInfo("http://www.resi1.com");
		this.resi.setNombre("Reidencia 1");
		this.resi.setTelefono("987654321");
		this.residenciaService.saveResidencia(this.resi);
		BDDMockito.given(this.residenciaService.findResidenciaById(ResidenciaControllerTest.TEST_RESIDENCIA_ID)).willReturn(this.resi);
		BDDMockito.given(this.managerService.findManagerByUsername(ResidenciaControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.man);
		BDDMockito.given(this.ancianoService.findAncianoByUsername(ResidenciaControllerTest.TEST_ANCIANO_NOMBRE)).willReturn(this.anc);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.organ);

		BDDMockito.given(this.authoritiesService.findAuthority(ResidenciaControllerTest.TEST_MANAGER_NOMBRE)).willReturn("manager");
		BDDMockito.given(this.authoritiesService.findAuthority(ResidenciaControllerTest.TEST_ANCIANO_NOMBRE)).willReturn("anciano");
		BDDMockito.given(this.authoritiesService.findAuthority(ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");

	}
	
	  @WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccess() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("residencias/residenciasDetails")); 
	  }
	  
	  @WithMockUser(username = ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccessOrganizador() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("residencias/residenciasList")); 
	  }
	  

	/*
	 * @WithMockUser(roles = "manager")
	 * 
	 * @Test void testProcessCreationFormSuccess() throws Exception {
	 * this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/new").param(
	 * "aceptaDependenciaGrave", "false").param("descripcion",
	 * "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf()).param(
	 * "fecha", "2020/01/01"))
	 * .andExpect(MockMvcResultMatchers.status().is3xxRedirection()); }
	 * 
	 * @WithMockUser(authorities = "manager")
	 * 
	 * @Test void testProcessCreationFormHasErrors() throws Exception {
	 * this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/new").param(
	 * "titulo",
	 * "Prueba").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha",
	 * "2020/01/01").param("descripcion", ""))
	 * .andExpect(MockMvcResultMatchers.model().attributeHasErrors("residencia")).
	 * andExpect(MockMvcResultMatchers.status().isOk()).andExpect(
	 * MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm")
	 * ); }
	 * 
	 * @WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
	 * 
	 * @Test void testShowResidencia() throws Exception {
	 * this.mockMvc.perform(MockMvcRequestBuilders.get(
	 * "/residencias/{residenciaId}",
	 * ResidenciaControllerTest.TEST_RESIDENCIA_ID)).andExpect(MockMvcResultMatchers
	 * .status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists(
	 * "residencia"))
	 * .andExpect(MockMvcResultMatchers.model().attribute("residencia",
	 * Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
	 * .andExpect(MockMvcResultMatchers.model().attribute("residencia",
	 * Matchers.hasProperty("titulo",
	 * Matchers.is("Prueba")))).andExpect(MockMvcResultMatchers.model().attribute(
	 * "residencia", Matchers.hasProperty("fecha", Matchers.is(this.hoy))))
	 * .andExpect(MockMvcResultMatchers.view().name("residencias/residenciasDetails"
	 * )); }
	 */

}
