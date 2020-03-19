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

import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InscripcionValidator implements Validator {

	public InscripcionService inscripcionService;


	@Override
	public void validate(final Object obj, final Errors errors) {
		Inscripcion inscripcion = (Inscripcion) obj;

		Residencia residencia = this.inscripcionService.findInscripcionById(inscripcion.getId()).getResidencia();
		Anciano anciano = this.inscripcionService.findInscripcionById(inscripcion.getId()).getAnciano();
		if (inscripcion.getDeclaracion().isEmpty()) {
			errors.rejectValue("declaracion", "requerido", "requerido");
		}

		if (inscripcion.getEstado().equals("rechazada") && inscripcion.getJustificacion().isEmpty()) {
			errors.rejectValue("justificacion", "si rechaza la inscripcion, debe presentar una justificación", "si rechaza la inscripcion, debe presentar una justificación");
		}

		if (inscripcion.getEstado().equals("aceptada")) {
			if (residencia.getAceptaDependenciaGrave() == false && anciano.getTieneDependenciaGrave() == true) {
				errors.rejectValue("estado", "su residencia no acepta personas con dependencia grave", "su residencia no acepta personas con dependencia grave");
			}
			if (residencia.getEdadMaxima() < anciano.getEdad()) {
				errors.rejectValue("estado", "su residencia no acepta personas mayores de " + residencia.getEdadMaxima() + " años", "su residencia no acepta personas mayores de " + residencia.getEdadMaxima() + " años");
			}
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Inscripcion.class.isAssignableFrom(clazz);
	}

}