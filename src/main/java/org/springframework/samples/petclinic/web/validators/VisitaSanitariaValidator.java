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

import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VisitaSanitariaValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		VisitaSanitaria visitaSanitaria = (VisitaSanitaria) obj;

		if (visitaSanitaria.getHoraFin() != null && visitaSanitaria.getHoraInicio() != null
				&& visitaSanitaria.getHoraInicio().isAfter(visitaSanitaria.getHoraFin())) {
			errors.rejectValue("horaFin", "debe ser igual o posterior a la hora inicio",
					"debe ser igual o posterior a la hora inicio");
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return VisitaSanitaria.class.isAssignableFrom(clazz);
	}

}