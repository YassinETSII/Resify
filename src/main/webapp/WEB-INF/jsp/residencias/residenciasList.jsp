<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="residencias">
    <h2>Residencias</h2>

    <table id="residenciasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
            <th style="width: 200px;">Hora apertura</th>
            <th style="width: 200px;">Hora cierre</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${residencias}" var="residencia">
            <tr>
                <td>
               		<spring:url value="/residencias/{residenciaId}" var="residenciaUrl">
                        <spring:param name="residenciaId" value="${residencia.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(residenciaUrl)}"><c:out value="${residencia.nombre}"/></a>
                </td>
                <td>
                    <c:out value="${residencia.horaApertura}"/>
                </td>
                <td>
                    <c:out value="${residencia.horaCierre}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <spring:url value="residencias/new" var="addUrl">
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Añadir nueva residencia</a>
    
</resify:layout>
