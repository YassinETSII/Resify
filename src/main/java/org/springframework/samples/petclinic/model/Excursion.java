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
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 */
@Entity
@Table(name = "excursiones")
public class Excursion extends ActivityEntity {

	@Positive
	@Column(name = "ratio")
	private double ratioAceptacion;
	
	@Positive
	@Column(name = "numeroResidencias")
	private int numeroResidencias;

	@NotNull
	@Column(name = "fecha_fin")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate fechaFin;
	
	@Column(name = "final_mode")
	private boolean finalMode;

	@ManyToOne
	@JoinColumn(name = "organizador_id")
	private Organizador organizador;
	
	public Double getRatioAceptacion() {
		return ratioAceptacion;
	}

	public void setRatioAceptacion(Double ratio) {
		this.ratioAceptacion = ratio;
	}

	public Integer getNumeroResidencias() {
		return numeroResidencias;
	}

	public void setAforo(Integer numeroResidencias) {
		this.numeroResidencias = numeroResidencias;
	}
	
	public Organizador getOrganizador() {
		return organizador;
	}
	
	public void setOrganizador(Organizador organizador) {
		this.organizador = organizador;
	}
	
	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isFinalMode() {
		return finalMode;
	}

	public void setFinalMode(boolean finalMode) {
		this.finalMode = finalMode;
	}
	
}
