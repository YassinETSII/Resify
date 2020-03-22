/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web.validators;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExcursionValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		Excursion excursion = (Excursion) obj;
		LocalDate currentDate = LocalDate.now();

		if (excursion.getTitulo().isEmpty()) {
			errors.rejectValue("titulo", "requerido", "requerido");
		}

		if (excursion.getDescripcion().isEmpty()) {
			errors.rejectValue("descripcion", "requerido", "requerido");
		}

		if (excursion.getFechaInicio() == null || !excursion.getFechaInicio().isAfter(currentDate)) {
			errors.rejectValue("fechaInicio", "debe ser fecha futura", "debe ser fecha futura");
		}

		if (excursion.getFechaFin() == null) {
			errors.rejectValue("fechaFin", "requerido", "requerido");
		}

		if (excursion.getHoraInicio() == null) {
			errors.rejectValue("horaInicio", "requerido", "requerido");
		}

		if (excursion.getHoraFin() == null) {
			errors.rejectValue("horaFin", "requerido", "requerido");
		}
		
		if(excursion.getAforo() < 10) {
			errors.rejectValue("aforo", "debe ser mayor que 10", "debe ser mayor que 10");
		}
		
		if(excursion.getRatioAceptacion() < 1) {
			errors.rejectValue("ratioAceptacion", "debe ser mayor que 1", "debe ser mayor que 1");
		}

		if (excursion.getFechaFin() != null && excursion.getFechaInicio() != null && excursion.getHoraInicio() != null
				&& excursion.getHoraFin() != null) {
			if (excursion.getFechaInicio().isAfter(excursion.getFechaFin())) {
				errors.rejectValue("fechaFin", "debe ser igual o posterior a la fecha inicio",
						"debe ser igual o posterior a la fecha inicio");
			} else if (excursion.getFechaInicio().isEqual(excursion.getFechaFin())) {
				if (excursion.getHoraInicio().isAfter(excursion.getHoraFin())) {
					errors.rejectValue("horaFin", "debe ser igual o posterior a la hora inicio",
							"debe ser igual o posterior a la hora inicio");
				}
			}
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Excursion.class.isAssignableFrom(clazz);
	}

}
