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
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;


@MappedSuperclass
public class RequestEntity extends BaseEntity {

	@Column(name = "fecha")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	private LocalDate fecha;
	
	@Column(name = "estado")
	@NotBlank
	@Pattern(regexp = "^pendiente|aceptada|rechazada$")
	private String estado;
	
	@Column(name = "declaracion")
	@NotBlank
	private String declaracion;

	@Column(name = "justificacion")
	private String justificacion;
	
	
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDeclaracion() {
		return declaracion;
	}

	public void setDeclaracion(String declaracion) {
		this.declaracion = declaracion;
	}

	public String getJustificacion() {
		return justificacion;
	}

	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", this.getId()).append("new", this.isNew()).append("fecha", this.fecha).append("estado", this.estado)
				.append("declaracion", this.declaracion).append("justificacion", this.justificacion).toString();
	}


}
