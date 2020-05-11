
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.FeedbackService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = FeedbackController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class FeedbackControllerTest {

	private static final int		TEST_FEEDBACK_ID	= 1;
	private static final int		TEST_EXCURSION_ID	= 6;


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

	@Autowired
	private MockMvc					mockMvc;

	private Feedback				fe;
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
		
		this.fe = new Feedback();
		this.fe.setId(FeedbackControllerTest.TEST_FEEDBACK_ID);

		this.fe.setDescripcion("Descripcion de prueba");
		this.fe.setValoracion(2);
		this.fe.setDescartaFeedback(false);

		this.feedbackService.save(this.fe);
		BDDMockito.given(this.feedbackService.findFeedbackById(FeedbackControllerTest.TEST_FEEDBACK_ID)).willReturn(this.fe);
		BDDMockito.given(this.managerService.findManagerByUsername(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.man);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.organ);

		BDDMockito.given(this.authoritiesService.findAuthority(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn("manager");
		BDDMockito.given(this.authoritiesService.findAuthority(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");

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
	  
	  /*@WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("feedbacks/createOrUpdateFeedbackForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("feedback"));
		}*/
	  
}
