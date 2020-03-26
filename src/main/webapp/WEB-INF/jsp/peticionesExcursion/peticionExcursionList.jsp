<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="inscripciones">
    <h2>
        <c:if test="${peticionExcursion['new']}">Nueva </c:if>Petición De Excursion
    </h2>

    <table id="residenciasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Residencia</th>
             <th style="width: 200px;">Estado</th>
            <th style="width: 200px;">Excursion</th>
            <th style="width: 200px;">Fecha</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${peticionesExcursion}" var="peticionExcursion">
            <tr>
                <td>
               		<spring:url value="/peticiones-excursion/{peticionExcursionId}/edit" var="peticionExcursionUrl">
                        <spring:param name="peticionExcursionId" value="${peticionExcursion.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(peticionExcursionUrl)}"><c:out value="${peticionExcursion.residencia.nombre} ${inscripcion.anciano.apellidos}"/></a>
                </td>
                <td>
                    <c:out value="${peticionExcursion.estado}"/>
                </td>
                <td>
                    <c:out value="${peticionExcursion.excursion.titulo}"/>
                </td>
                <td>
                    <fmt:formatDate value="${peticionExcursion.fecha}" pattern="yyyy/MM/dd"/>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>    
</resify:layout>
