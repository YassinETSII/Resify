<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="actividades">

    <h2>Informacion de la actividad</h2>


    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${actividad.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Descripcion</th>
            <td><c:out value="${actividad.descripcion}"/></td>
        </tr>
        <tr>
            <th>Fecha Inicio</th>
            <td><c:out value="${actividad.fechaInicio}"/></td>
        </tr>
        <tr>
            <th>Hora Inicio</th>
            <td><c:out value="${actividad.horaInicio}"/></td>
        </tr>
        <tr>
            <th>Hora Fin</th>
            <td><c:out value="${actividad.horaFin}"/></td>
        </tr>
    </table>
    
</resify:layout>
