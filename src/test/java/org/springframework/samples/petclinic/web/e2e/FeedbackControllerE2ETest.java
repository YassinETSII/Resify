
package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
public class FeedbackControllerE2ETest {

	private static final int	TEST_MANAGER_ID	= 1;
	private static final int	TEST_BA_ID		= 1;
	private static final int	TEST_EX_ID		= 6;
	private static final int	TEST_EX2_ID		= 8;



	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "organizador1", authorities = {
		"organizador"
	})
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("feedbacks/feedbackList"));
	}
	
	@WithMockUser(username = "manager1", authorities = {
			"manager"
		})
		@Test
		void testInitCreationForm() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("feedbacks/createOrUpdateFeedbackForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("feedback"));
		}
	
	@WithMockUser(username = "manager1", authorities = {
			"manager"
		})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX2_ID).with(csrf()).param("valoracion", "2").param("descripcion", "Prueba descrip").param("descartaFeedbac", "false"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
