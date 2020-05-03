
package org.springframework.samples.petclinic.web.e2e;

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
public class BuenaAccionControllerE2ETest {

	private static final int	TEST_MANAGER_ID	= 1;
	private static final int	TEST_BA_ID		= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("buenasAcciones/buenasAccionesList"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("buenasAcciones/createOrUpdateBuenaAccionForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("buenaAccion"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/buenas-acciones/new").param("titulo", "Prueba").param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/buenas-acciones/new").param("titulo", "Prueba").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01").param("descripcion", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("buenaAccion")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("buenasAcciones/createOrUpdateBuenaAccionForm"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testShowBuenaAccion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones/{buenaAccionId}", BuenaAccionControllerE2ETest.TEST_BA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("buenaAccion"))
			.andExpect(MockMvcResultMatchers.model().attribute("buenaAccion", Matchers.hasProperty("descripcion", Matchers.is("Descripcion de buena acción1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("buenaAccion", Matchers.hasProperty("titulo", Matchers.is("titulo1")))).andExpect(MockMvcResultMatchers.view().name("buenasAcciones/buenasAccionesDetails"));
	}

}
