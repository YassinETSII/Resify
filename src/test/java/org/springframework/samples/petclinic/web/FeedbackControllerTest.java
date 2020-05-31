
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Feedback;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.FeedbackService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = FeedbackController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class FeedbackControllerTest {

	private static final int		TEST_FEEDBACK_ID	= 1;
	private static final int		TEST_EXCURSION_ID	= 8;
	private static final int		TEST_PETICION_EXCURSION_ID	= 6;
	private static final int		TEST_RESIDENCIA_ID = 1;


	private static final String		TEST_MANAGER_NOMBRE		= "manager";
	private static final String		TEST_ORGANIZADOR_NOMBRE		= "organizador";

	@MockBean
	private FeedbackService		feedbackService;

	@MockBean
	private ManagerService			managerService;

	@MockBean
	private ExcursionService		excursionesService;

	@MockBean
	private InscripcionService	inscripcionService;

	@MockBean
	private OrganizadorService	organizadorService;
	
	@MockBean
	private AuthoritiesService			authoritiesService;
	
	@MockBean
	private PeticionExcursionService peticionExcursionService;
	
	@MockBean
	private ResidenciaService	residenciaService;

	@Autowired
	private MockMvc					mockMvc;

	private Feedback				fe;
	private Excursion				ex;
	private PeticionExcursion		pet;
	private Residencia				re;
	Manager							man						= new Manager();
	Organizador 					organ 					= new Organizador();
	Anciano 						anc 					= new Anciano();
	User							user					= new User();
	User							userOrganiza			= new User();
	

	@BeforeEach
	void setup() {
		this.user.setUsername(FeedbackControllerTest.TEST_MANAGER_NOMBRE);
		this.userOrganiza.setUsername(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE);
		
		this.man.setUser(this.user);
		this.organ.setUser(this.userOrganiza);
		
		//Residencia
		this.re = new Residencia();
		this.re.setId(FeedbackControllerTest.TEST_RESIDENCIA_ID);
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		this.re.setAceptaDependenciaGrave(false);
		this.re.setAforo(100);
		this.re.setCorreo("residencia1@mail.es");
		this.re.setDescripcion("Descripcion de prueba");
		this.re.setDireccion("Direccion");
		this.re.setEdadMaxima(70);
		this.re.setHoraApertura(horaApertura);
		this.re.setHoraCierre(horaCierre);
		this.re.setMasInfo("http://www.resi1.com");
		this.re.setNombre("Reidencia 1");
		this.re.setTelefono("987654321");
		this.re.setManager(this.man);
		this.residenciaService.saveResidencia(this.re);
		
		//Excursion
		this.ex = new Excursion();
		this.ex.setId(FeedbackControllerTest.TEST_EXCURSION_ID);
		this.ex.setTitulo("Prueba");
		this.ex.setDescripcion("Prueba desc");
		this.ex.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(9)));
		this.ex.setFechaFin(java.sql.Date.valueOf(LocalDate.now().minusDays(10)));
		this.ex.setHoraInicio(LocalTime.of(9, 0));
		this.ex.setHoraFin(LocalTime.of(20, 0));
		this.ex.setRatioAceptacion(0.1);
		this.ex.setNumeroResidencias(15);
		this.ex.setOrganizador(this.organ);
		this.ex.setFinalMode(true);
		this.excursionesService.saveExcursion(this.ex);
		
		//PeticionExcursion
		this.pet = new PeticionExcursion();
		this.pet.setId(FeedbackControllerTest.TEST_PETICION_EXCURSION_ID);
		this.pet.setDeclaracion("Declaracion de prueba");
		this.pet.setEstado("aceptada");
		this.pet.setFecha(java.sql.Date.valueOf(LocalDate.now().minusDays(12)));
		this.pet.setExcursion(this.ex);
		this.pet.setResidencia(this.re);
		this.pet.setJustificacion(null);
		this.peticionExcursionService.save(this.pet);
		
		//Feedback
		this.fe = new Feedback();
		this.fe.setId(FeedbackControllerTest.TEST_FEEDBACK_ID);
		this.fe.setDescripcion("Descripcion de prueba");
		this.fe.setValoracion(2);
		this.fe.setDescartaFeedback(false);
		this.fe.setExcursion(this.ex);
		this.fe.setResidencia(this.re);
		this.feedbackService.save(this.fe);
		
		BDDMockito.given(this.feedbackService.findFeedbackById(FeedbackControllerTest.TEST_FEEDBACK_ID)).willReturn(this.fe);
		BDDMockito.given(this.excursionesService.findExcursionById(FeedbackControllerTest.TEST_EXCURSION_ID)).willReturn(this.ex);

		BDDMockito.given(this.peticionExcursionService.findPeticionExcursionById(FeedbackControllerTest.TEST_PETICION_EXCURSION_ID)).willReturn(this.pet);

		BDDMockito.given(this.managerService.findManagerByUserName(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.man);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.organ);
		
		BDDMockito.given(this.residenciaService.findResidenciaById(FeedbackControllerTest.TEST_RESIDENCIA_ID)).willReturn(this.re);
		BDDMockito.given(this.managerService.findResidenciaByManagerUsername(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.re);

		BDDMockito.given(this.authoritiesService.findAuthority(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn("manager");
		BDDMockito.given(this.authoritiesService.findAuthority(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");

		BDDMockito.given(this.feedbackService.countFeedbacksByExcursion(this.ex,this.man)).willReturn(0);
		BDDMockito.given(this.peticionExcursionService.countPeticionesByExcursionAceptada(this.ex,this.man)).willReturn(1);
		}
	
	  @WithMockUser(username = FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccess() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("feedbacks/feedbackList")); 
	  }
	  
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test 
	  void testProcessFindFormError() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("exception")); 
	  }
	  
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("feedbacks/createOrUpdateFeedbackForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("feedback"));
		}

}
