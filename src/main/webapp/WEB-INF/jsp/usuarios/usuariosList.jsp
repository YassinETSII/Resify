<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>

<resify:layout pageName="usuarios">

	<c:if test="${not empty ancianos }">
		<h2>Ancianos</h2>
		<table id="ancianosTable" class="table table-striped">
			<thead>
				<tr>
					<th style="width: 70px;">Nombre</th>
					<th style="width: 200px;">Apellidos</th>
					<th style="width: 200px;">Edad</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${ancianos}" var="anciano">
					<tr>
						<td><spring:url value="/ancianos/{ancianoId}"
								var="ancianoUrl">
								<spring:param name="ancianoId" value="${anciano.id}" />
							</spring:url> <a href="${fn:escapeXml(ancianoUrl)}"><c:out
									value="${anciano.nombre}" /></a></td>
						<td><c:out value="${anciano.apellidos }" />
						<td><c:out value="${anciano.edad}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<spring:url value="ancianos/new" var="addUrl" />
	</c:if>

	<c:if test="${not empty organizadores }">
		<h2>Organizadores</h2>
		<table id="organizadoresTable" class="table table-striped">
			<thead>
				<tr>
					<th style="width: 70px;">Nombre</th>
					<th style="width: 200px;">Apellidos</th>
					<th style="width: 200px;">Companya</th>
					<th style="width: 200px;">Sector</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${organizadores}" var="organizador">
					<tr>
						<td><spring:url value="/organizadores/{organizadorId}"
								var="organizadorUrl">
								<spring:param name="organizadorId" value="${organizador.id}" />
							</spring:url> <a href="${fn:escapeXml(organizadorUrl)}"><c:out
									value="${organizador.nombre}" /></a></td>
						<td><c:out value="${organizador.apellidos }" />
						<td><c:out value="${organizador.companya}" /></td>
						<td><c:out value="${organizador.sector}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<spring:url value="organizadores/new" var="addUrl" />
	</c:if>


	<c:if test="${not empty managers }">
		<h2>Managers</h2>
		<table id="managersTable" class="table table-striped">
			<thead>
				<tr>
					<th style="width: 70px;">Nombre</th>
					<th style="width: 200px;">Apellidos</th>
					<th style="width: 200px;">Firma</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${managers}" var="manager">
					<tr>
						<td><spring:url value="/managers/{managerId}"
								var="managerUrl">
								<spring:param name="managerId" value="${manager.id}" />
							</spring:url> <a href="${fn:escapeXml(managerUrl)}"><c:out
									value="${manager.nombre}" /></a></td>
						<td><c:out value="${manager.apellidos }" />
						<td><c:out value="${manager.firma}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<spring:url value="managers/new" var="addUrl" />
	</c:if>

	<a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Añadir
		nuevo usuario</a>


</resify:layout>
