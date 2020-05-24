<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="usuarios">
	<c:if test="${not empty anciano }">
		<h2>
			<c:if test="${anciano['new']}">Nuevo </c:if>
			Anciano
		</h2>
		<form:form modelAttribute="anciano" class="form-horizontal"
			id="add-anciano-form">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Nombre" name="nombre" />
				<petclinic:inputField label="Apellidos" name="apellidos" />
				<petclinic:inputField label="Edad" name="edad" />
				<petclinic:inputField label="Carta de Presentacion"
					name="cartaPresentacion" />
				<c:if test="${anciano['new']}">
					<petclinic:inputField label="Username" name="user.username" />
					<petclinic:inputField label="Password" name="user.password" />
				</c:if>
				<label class="col-sm-2 control-label"><input type="checkbox"
					name="tieneDependenciaGrave" /> ¿Tiene dependencia grave?</label>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
						<c:when test="${anciano['new']}">
							<button class="btn btn-default" type="submit">Añadir
								Anciano</button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-default" type="submit">Actualizar
								Anciano</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	</c:if>

	<c:if test="${not empty organizador }">
		<h2>
			<c:if test="${organizador['new']}">Nuevo </c:if>
			Organizador
		</h2>
		<form:form modelAttribute="organizador" class="form-horizontal"
			id="add-organizador-form">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Nombre" name="nombre" />
				<petclinic:inputField label="Apellidos" name="apellidos" />
				<petclinic:inputField label="Companya" name="companya" />
				<petclinic:inputField label="Sector" name="sector" />
				<c:if test="${organizador['new']}">
					<petclinic:inputField label="Username" name="user.username" />
					<petclinic:inputField label="Password" name="user.password" />
				</c:if>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
						<c:when test="${organizador['new']}">
							<button class="btn btn-default" type="submit">Añadir
								Organizador</button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-default" type="submit">Actualizar
								Organizador</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	</c:if>

	<c:if test="${not empty manager }">
		<h2>
			<c:if test="${manager['new']}">Nuevo </c:if>
			Manager
		</h2>
		<form:form modelAttribute="manager" class="form-horizontal"
			id="add-manager-form">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Nombre" name="nombre" />
				<petclinic:inputField label="Apellidos" name="apellidos" />
				<petclinic:inputField label="Firma" name="firma" />
				<petclinic:inputField label="Declaracion de Responsabilidad"
					name="declaracionResponsabilidad" />
				<c:if test="${manager['new']}">
					<petclinic:inputField label="Username" name="user.username" />
					<petclinic:inputField label="Password" name="user.password" />
				</c:if>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
						<c:when test="${manager['new']}">
							<button class="btn btn-default" type="submit">Añadir
								Manager</button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-default" type="submit">Actualizar
								Manager</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	</c:if>

</petclinic:layout>
