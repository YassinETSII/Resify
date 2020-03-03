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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.core.style.ToStringCreator;


@Entity
@Table(name = "ancianos")
public class Anciano extends BaseEntity {

	@Column(name = "edad")
	@NotNull
	@Size(min=66, max=110)
	private Integer edad;

	@Column(name = "cartaPresentacion")
	@NotBlank
	private String cartaPresentacion;

	@Column(name = "tieneSancion")
	@NotNull
	private Boolean tieneSancion;
	
	@Column(name = "tipoSancion")
	private String tipoSancion;
	
	@Column(name = "tieneDependenciaSevera")
	@NotNull
	private Boolean tieneDependenciaSevera;
	
	@Column(name = "tipoDependencia")
	private String tipoDependencia;
	
	//
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	//
	

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getCartaPresentacion() {
		return cartaPresentacion;
	}

	public void setCartaPresentacion(String cartaPresentacion) {
		this.cartaPresentacion = cartaPresentacion;
	}

	public Boolean getTieneSancion() {
		return tieneSancion;
	}

	public void setTieneSancion(Boolean tieneSancion) {
		this.tieneSancion = tieneSancion;
	}

	public Boolean getTieneDependenciaSevera() {
		return tieneDependenciaSevera;
	}

	public void setTieneDependenciaSevera(Boolean tieneDependenciaSevera) {
		this.tieneDependenciaSevera = tieneDependenciaSevera;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("new", this.isNew()).append("edad", this.edad).append("cartaPresentacion", this.cartaPresentacion)
				.append("tieneSancion", this.tieneSancion).append("tipoSancion", this.tipoSancion).append("tieneDependenciaSevera", this.tieneDependenciaSevera)
				.append("tipoDependencia", this.tipoDependencia).toString();
	}

}
