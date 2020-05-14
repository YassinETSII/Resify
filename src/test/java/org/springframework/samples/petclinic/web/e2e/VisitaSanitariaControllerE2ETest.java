
package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalTime;

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
public class VisitaSanitariaControllerE2ETest {

	private static final int TEST_VS_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "manager4", authorities = { "manager" })
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/visitasSanitariasList"));
	}

	@WithMockUser(username = "manager4", authorities = { "manager" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/createOrUpdateVisitaSanitariaForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("visitaSanitaria"));
	}

	@WithMockUser(username = "manager4", authorities = { "manager" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "Motivo prueba")
						.param("descripcion", "Prueba descrip").param("sanitario", "sanitario prueba")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01")
						.param("horaInicio", "10:00").param("horaFin", "12:00").param("anciano.id", String.valueOf(22)))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager4", authorities = { "manager" })
	@Test
	void testProcessCreationFormHasErrorsBlank() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "")
						.param("sanitario", "").param("descripcion", "Prueba descrip").param("horaInicio", "")
						.param("horaFin", "").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("fecha", "2020/01/01").param("anciano.id", "22"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("visitaSanitaria"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/createOrUpdateVisitaSanitariaForm"));
	}

	@WithMockUser(username = "manager4", authorities = { "manager" })
	@Test
	void testProcessCreationFormHasErrorsAncianoSinDependencia() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "Motivo prueba")
						.param("sanitario", "sanitario prueba").param("descripcion", "Prueba descrip")
						.param("horaInicio", "10:00").param("horaFin", "12:00")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01")
						.param("anciano.id", "20"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/createOrUpdateVisitaSanitariaForm"));
	}

	@WithMockUser(username = "manager4", authorities = { "manager" })
	@Test
	void testShowVisitaSanitaria() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/{visitaSanitariaId}",
				TEST_VS_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("visitaSanitaria"))
		.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
				Matchers.hasProperty("descripcion", Matchers.is("visita sanitaria"))))
		.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
				Matchers.hasProperty("motivo", Matchers.is("Ejemplo"))))
		.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
				Matchers.hasProperty("sanitario", Matchers.is("Sanitario prueba"))))
		.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
				Matchers.hasProperty("horaInicio", Matchers.is(LocalTime.of(17, 00)))))
		.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
				Matchers.hasProperty("horaFin", Matchers.is(LocalTime.of(22, 00)))))
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/visitasSanitariasDetails"));
	}
	
}
