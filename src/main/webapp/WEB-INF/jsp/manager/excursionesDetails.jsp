<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="excursiones">

    <h2>Informacion de la excursion</h2>


    <table class="table table-striped" >
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${excursion.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Descripcion</th>
            <td><c:out value="${excursion.descripcion}"/></td>
        </tr>
        <tr>
            <th>Fecha Inicio</th>
            <td><c:out value="${excursion.fechaInicio}"/></td>
        </tr>
        <tr>
            <th>Hora Inicio</th>
            <td><c:out value="${excursion.horaInicio}"/></td>
        </tr>
        <tr>
            <th>Fecha Fin</th>
            <td><c:out value="${excursion.fechaFin}"/></td>
        </tr>
        <tr>
            <th>Hora Fin</th>
            <td><c:out value="${excursion.horaFin}"/></td>
        </tr>
        <tr>
            <th>Ratio de aceptacion</th>
            <td><c:out value="${excursion.ratioAceptacion}"/></td>
        </tr>
        <tr>
            <th>Aforo</th>
            <td><c:out value="${excursion.aforo}"/></td>
        </tr>
    </table>
	
    <spring:url value="/manager/excursion/{excursionId}/inscripcion/new" var="editUrl">
        <spring:param name="excursionId" value="${excursion.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Inscribirse</a>

</petclinic:layout>
