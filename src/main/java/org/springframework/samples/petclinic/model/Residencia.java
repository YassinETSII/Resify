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

package org.springframework.samples.petclinic.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "residencias")
public class Residencia extends BaseEntity {

	@NotBlank
	@Column(name = "nombre")
	private String		nombre;

	@NotBlank
	@Column(name = "direccion")
	private String		direccion;

	@NotBlank
	@Column(name = "descripcion")
	private String		descripcion;

	@Column(name = "aforo")
	private int			aforo;

	@URL
	@Column(name = "mas_info")
	private String		masInfo;

	@NotBlank
	@Column(name = "telefono")
	@Digits(fraction = 0, integer = 10)
	private String		telefono;

	@NotBlank
	@Email
	@Column(name = "correo")
	private String		correo;

	@NotNull
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "hora_apertura")
	private LocalTime	horaApertura;

	@NotNull
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "hora_cierre")
	private LocalTime	horaCierre;

	@Column(name = "edad_maxima")
	@NotNull
	private Integer		edadMaxima;

	@Column(name = "acepta_dependencia_grave")
	@NotNull
	private boolean		aceptaDependenciaGrave;


	public Integer getEdadMaxima() {
		return this.edadMaxima;
	}

	public void setEdadMaxima(final Integer edadMaxima) {
		this.edadMaxima = edadMaxima;
	}

	public boolean getAceptaDependenciaGrave() {
		return this.aceptaDependenciaGrave;
	}

	public void setAceptaDependenciaGrave(final boolean bool) {
		this.aceptaDependenciaGrave = bool;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(final String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(final String correo) {
		this.correo = correo;
	}

	public LocalTime getHoraApertura() {
		return this.horaApertura;
	}

	public void setHoraApertura(final LocalTime horaApertura) {
		this.horaApertura = horaApertura;
	}

	public LocalTime getHoraCierre() {
		return this.horaCierre;
	}

	public void setHoraCierre(final LocalTime horaCierre) {
		this.horaCierre = horaCierre;
	}


	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Manager manager;


	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(final String direccion) {
		this.direccion = direccion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getAforo() {
		return this.aforo;
	}

	public void setAforo(final Integer aforo) {
		this.aforo = aforo;
	}

	public String getMasInfo() {
		return this.masInfo;
	}

	public void setMasInfo(final String masInfo) {
		this.masInfo = masInfo;
	}

	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

}
