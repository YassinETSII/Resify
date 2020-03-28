<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>

<resify:layout pageName="usuarios">


	<c:if test="${not empty anciano }">
		<h2>Información del anciano</h2>

		<table class="table table-striped">
			<tr>
				<th>Nombre Completo</th>
				<td><b><c:out
							value="${anciano.nombre} ${anciano.apellidos }" /></b></td>
			</tr>
			<tr>
				<th>Edad</th>
				<td><c:out value="${anciano.edad}" /></td>
			</tr>
			<tr>
				<th>Carta de presentacion</th>
				<td><c:out value="${anciano.cartaPresentacion}" /></td>
			</tr>
			<tr>
				<th>¿Dependencia grave?</th>
				<td><c:out value="${anciano.tieneDependenciaGrave}" /></td>
			</tr>
			<tr>
				<th>Usuario</th>
				<td><c:out value="${anciano.user.username}" /></td>
			</tr>
			<tr>
				<th>Contrasenya</th>
				<td><c:out value="${anciano.user.password}" /></td>
			</tr>
		</table>

		<spring:url value="{ancianoId}/edit" var="editUrl">
			<spring:param name="ancianoId" value="${anciano.id}" />
		</spring:url>

	</c:if>

	<c:if test="${not empty organizador }">
		<h2>Información del organizador</h2>

		<table class="table table-striped">
			<tr>
				<th>Nombre Completo</th>
				<td><b><c:out
							value="${organizador.nombre} ${organizador.apellidos }" /></b></td>
			</tr>
			<tr>
				<th>Companya</th>
				<td><c:out value="${organizador.companya}" /></td>
			</tr>
			<tr>
				<th>Sector</th>
				<td><c:out value="${organizador.sector}" /></td>
			</tr>
			<tr>
				<th>Usuario</th>
				<td><c:out value="${organizador.user.username}" /></td>
			</tr>
			<tr>
				<th>Contrasenya</th>
				<td><c:out value="${organizador.user.password}" /></td>
			</tr>
		</table>

		<spring:url value="{organizadorId}/edit" var="editUrl">
			<spring:param name="organizadorId" value="${organizador.id}" />
		</spring:url>

	</c:if>

	<c:if test="${not empty manager }">
		<h2>Información del manager</h2>

		<table class="table table-striped">
			<tr>
				<th>Nombre Completo</th>
				<td><b><c:out
							value="${manager.nombre} ${manager.apellidos }" /></b></td>
			</tr>
			<tr>
				<th>Edad</th>
				<td><c:out value="${manager.firma}" /></td>
			</tr>
			<tr>
				<th>Carta de presentacion</th>
				<td><c:out value="${manager.declaracionResponsabilidad}" /></td>
			</tr>
			<tr>
				<th>Usuario</th>
				<td><c:out value="${manager.user.username}" /></td>
			</tr>
			<tr>
				<th>Contrasenya</th>
				<td><c:out value="${manager.user.password}" /></td>
			</tr>
		</table>

		<spring:url value="{managerId}/edit" var="editUrl">
			<spring:param name="managerId" value="${manager.id}" />
		</spring:url>

	</c:if>

	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar
		usuario</a>

</resify:layout>
