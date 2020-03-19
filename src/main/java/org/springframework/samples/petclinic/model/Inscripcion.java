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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inscripciones")
public class Inscripcion extends RequestEntity {

	@ManyToOne
	@JoinColumn(name = "anciano_id")
	private Anciano		anciano;

	@ManyToOne
	@JoinColumn(name = "residencia_id")
	private Residencia	residencia;


	public Anciano getAnciano() {
		return this.anciano;
	}

	public void setAnciano(final Anciano anciano) {
		this.anciano = anciano;
	}

	public Residencia getResidencia() {
		return this.residencia;
	}

	public void setResidencia(final Residencia residencia) {
		this.residencia = residencia;
	}

}
