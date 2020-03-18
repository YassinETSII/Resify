<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="excursiones">
    <h2>Excursiones</h2>

    <table id="excursionesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Titulo</th>
            <th style="width: 200px;">Fecha inicio</th>
            <th style="width: 120px">Hora inicio</th>
            <th style="width: 200px;">Fecha fin</th>
            <th style="width: 120px">Hora fin<th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${excursiones}" var="excursion">
            <tr>
                <td>
                    <c:if test="${isOrganizador}">
               			<spring:url value="/organizador/excursiones/{excursionId}" var="excursionUrl">
                        	<spring:param name="excursionId" value="${excursion.id}"/>
                    	</spring:url>
                    </c:if>
                    <c:if test="${isManager}">
               			<spring:url value="/manager/excursiones/{excursionId}" var="excursionUrl">
                        	<spring:param name="excursionId" value="${excursion.id}"/>
                    	</spring:url>
                    </c:if>

                    <a href="${fn:escapeXml(excursionUrl)}"><c:out value="${excursion.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${excursion.fechaInicio}"/>
                </td>
                <td>
                    <c:out value="${excursion.horaInicio}"/>
                </td>
                <td>
                    <c:out value="${excursion.fechaFin}"/>
                </td>
                <td>
                    <c:out value="${excursion.horaFin}"/>
                </td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <c:if test="${isOrganizador}">
    	<spring:url value="/organizador/excursiones/new" var="addUrl">
    	</spring:url>
    	<a href="${fn:escapeXml(addUrl)}" class="btn btn-default">A�adir nueva excursion</a>
    </c:if>
</petclinic:layout>
