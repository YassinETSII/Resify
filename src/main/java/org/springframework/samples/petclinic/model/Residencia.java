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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;


@Entity
@Table(name = "residencias")
public class Residencia extends BaseEntity {

	@NotBlank
	@Column(name = "nombre")
	private String nombre;
	
	@NotBlank
	@Column(name = "direccion")
	private String direccion;
	
	@NotBlank
	@Column(name = "descripcion")
	private String descripcion;
	
	@NotBlank
	@Column(name = "aforo")
	private Integer aforo;
	
	@URL
	@Column(name = "mas_info")
	private String masInfo;
	
	
	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Manager manager;

	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getAforo() {
		return aforo;
	}

	public void setAforo(Integer aforo) {
		this.aforo = aforo;
	}

	public String getMasInfo() {
		return masInfo;
	}

	public void setMasInfo(String masInfo) {
		this.masInfo = masInfo;
	}
	
	public Manager getManager() {
		return manager;
	}
	
	public void setManager(Manager manager) {
		this.manager = manager;
	}

}
