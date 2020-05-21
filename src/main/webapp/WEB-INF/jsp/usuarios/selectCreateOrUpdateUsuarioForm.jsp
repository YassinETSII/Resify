<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="usuarios">
	<h2>Nuevo Usuario</h2>

	<spring:url value="/ancianos/new" var="addAncianoUrl" />
	<a href="${fn:escapeXml(addAncianoUrl)}" class="btn btn-default">Añadir
		nuevo anciano</a>

	<spring:url value="/managers/new" var="addManagerUrl" />
	<a href="${fn:escapeXml(addManagerUrl)}" class="btn btn-default">Añadir
		nuevo manager</a>

	<spring:url value="/organizadores/new" var="addOrganizadorUrl" />
	<a href="${fn:escapeXml(addOrganizadorUrl)}" class="btn btn-default">Añadir
		nuevo organizador</a>

</petclinic:layout>
