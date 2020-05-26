
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class QuejaValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void debeValidar() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Queja queja = new Queja();
		queja.setTitulo("Titulo test");
		queja.setDescripcion("Descripcion test");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Queja>> constraintViolations = validator.validate(queja);

		//No hay campos incorrectos
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void noDebeValidarConAtributosObligatoriosVacios() {

		Queja queja = new Queja();
		queja.setTitulo("");
		queja.setDescripcion("");


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Queja>> constraintViolations = validator.validate(queja);

		//Hay 1 campo incorrecto: declaracion
		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
		List<ConstraintViolation<Queja>> errors = new ArrayList<ConstraintViolation<Queja>>();
		errors.addAll(constraintViolations);
		ConstraintViolation<Queja> violation1 = errors.get(0);
		ConstraintViolation<Queja> violation2 = errors.get(1);
		
		Assertions.assertThat(violation1.getPropertyPath().toString()).isEqualTo("descripcion");
		Assertions.assertThat(violation2.getPropertyPath().toString()).isEqualTo("titulo");
	}



}
