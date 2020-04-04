/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web.validators;

import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PeticionExcursionValidator implements Validator {


	@Override
	public void validate(final Object obj, final Errors errors) {
		PeticionExcursion peticionExcursion = (PeticionExcursion) obj;

		if (peticionExcursion.getDeclaracion().isEmpty()) {
			errors.rejectValue("declaracion", "requerido", "requerido");
		}

		if (peticionExcursion.getEstado().equals("rechazada") && peticionExcursion.getJustificacion().isEmpty()) {
			errors.rejectValue("justificacion", "si rechaza la peticion, debe presentar una justificación", "si rechaza la inscripcion, debe presentar una justificación");
		}

	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return PeticionExcursion.class.isAssignableFrom(clazz);
	}

}
