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

import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BuenaAccionValidator implements Validator {

	@Override
	public void validate(Object obj, Errors errors) {
		BuenaAccion buenaAccion = (BuenaAccion) obj;
		LocalDate currentDate = LocalDate.now();

		if (buenaAccion.getDescripcion().isEmpty()) {
			errors.rejectValue("descripcion", "requerido", "requerido");
		}
		if (buenaAccion.getFecha() == null || buenaAccion.getFecha().isAfter(currentDate)) {
			errors.rejectValue("fecha", "la fecha es requerida y debe estar en el pasado", "la fecha es requerida y debe estar en el pasado");
		}

		/*if (buenaAccion.getAnciano() == null) {
			errors.rejectValue("anciano", "requerido", "requerido");
		}*/
		
		if (buenaAccion.getTitulo().isEmpty()) {
			errors.rejectValue("titulo", "requerido", "requerido");
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return BuenaAccion.class.isAssignableFrom(clazz);
	}

}
