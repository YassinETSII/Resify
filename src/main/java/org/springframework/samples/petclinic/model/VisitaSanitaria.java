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

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "visitas_sanitarias")
public class VisitaSanitaria extends BaseEntity {

	@NotBlank
	@Column(name = "motivo")
	private String	motivo;
	
	@NotBlank
	@Column(name = "descripcion")
	private String	descripcion;
	
	@NotBlank
	@Column(name = "sanitario")
	private String	sanitario;

	@Column(name = "fecha")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	private Date	fecha;
	
	@NotNull
	@Column(name = "hora_inicio")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime horaInicio;
	
	@NotNull
	@Column(name = "hora_fin")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime horaFin;
	
	@ManyToOne
	@JoinColumn(name = "residencia_id")
	private Residencia residencia;
	
	@ManyToOne
	@JoinColumn(name = "anciano_id")
	private Anciano anciano;

	public Residencia getResidencia() {
		return residencia;
	}

	public void setResidencia(Residencia residencia) {
		this.residencia = residencia;
	}

	public Anciano getAnciano() {
		return anciano;
	}

	public void setAnciano(Anciano anciano) {
		this.anciano = anciano;
	}
	
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getSanitario() {
		return sanitario;
	}

	public void setSanitario(String sanitario) {
		this.sanitario = sanitario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}
	
}
