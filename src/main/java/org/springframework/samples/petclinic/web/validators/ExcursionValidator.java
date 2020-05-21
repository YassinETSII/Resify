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

import java.util.Date;

import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExcursionValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		Excursion excursion = (Excursion) obj;
		Date currentDate = new Date();

		if (excursion.getFechaInicio() != null && !excursion.getFechaInicio().after(currentDate)) {
			errors.rejectValue("fechaInicio", "la fecha de inicio debe ser futura",
					"la fecha de inicio debe ser futura");
		}

		if (excursion.getFechaFin() != null && excursion.getFechaInicio() != null && excursion.getHoraInicio() != null
				&& excursion.getHoraFin() != null) {
			if (excursion.getFechaInicio().after(excursion.getFechaFin())) {
				errors.rejectValue("fechaFin", "debe ser igual o posterior a la fecha inicio",
						"debe ser igual o posterior a la fecha inicio");
			} else if (excursion.getFechaInicio().equals(excursion.getFechaFin())) {
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
