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

import java.util.Date;

import org.springframework.samples.petclinic.model.Incidencia;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class IncidenciaValidator implements Validator {

	@Override
	public void validate(final Object obj, final Errors errors) {
		Incidencia incidencia = (Incidencia) obj;
		Date hoy = new Date(System.currentTimeMillis());

		if (incidencia.getDescripcion().isEmpty()) {
			errors.rejectValue("descripcion", "requerido", "requerido");
		}
		if (incidencia.getFecha() == null || incidencia.getFecha().after(hoy)) {
			errors.rejectValue("fecha", "la fecha es requerida y debe estar en el pasado", "la fecha es requerida y debe estar en el pasado");
		}

		/*
		 * if (incidencia.getAnciano() == null) {
		 * errors.rejectValue("anciano", "requerido", "requerido");
		 * }
		 */

		if (incidencia.getTitulo().isEmpty()) {
			errors.rejectValue("titulo", "requerido", "requerido");
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Incidencia.class.isAssignableFrom(clazz);
	}

}
