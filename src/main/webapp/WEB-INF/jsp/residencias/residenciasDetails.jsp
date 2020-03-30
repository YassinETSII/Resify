<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="residencias">

    <h2>Informaci�n de la residencia</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${residencia.nombre}"/></b></td>
        </tr>
        <tr>
            <th>Direcci�n</th>
            <td><c:out value="${residencia.direccion}"/></td>
        </tr>
        <tr>
            <th>Descripci�n</th>
            <td><c:out value="${residencia.descripcion}"/></td>
        </tr>
        <tr>
            <th>Aforo</th>
            <td><c:out value="${residencia.aforo}"/></td>
        </tr>
        <tr>
            <th>M�s Info</th>
            <td><c:out value="${residencia.masInfo}"/></td>
        </tr>
        <tr>
            <th>Tel�fono</th>
            <td><c:out value="${residencia.telefono}"/></td>
        </tr>
        <tr>
            <th>Correo</th>
            <td><c:out value="${residencia.correo}"/></td>
        </tr>
        <tr>
            <th>Hora de apertura</th>
            <td><c:out value="${residencia.horaApertura}"/></td>
        </tr>
        <tr>
            <th>Hora de cierre</th>
            <td><c:out value="${residencia.horaCierre}"/></td>
        </tr>
        <tr>
            <th>Edad m�xima</th>
            <td><c:out value="${residencia.edadMaxima}"/></td>
        </tr>
    </table>
    
    <c:if test="${pageContext['request'].userPrincipal.name == residencia.manager.user.username}"> 
    	<spring:url value="{residenciaId}/edit" var="editUrl">
       		<spring:param name="residenciaId" value="${residencia.id}"/>
    	</spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar residencia</a>
    </c:if>
    
    <c:if test="${fn:contains(pageContext['request'].userPrincipal.authorities, 'anciano')}"> 
    	<spring:url value="../inscripciones/new/{residenciaId}" var="inscribirseUrl">
    		<spring:param name="residenciaId" value="${residencia.id}"/>
    	</spring:url>
   		<a href="${inscribirseUrl}" class="btn btn-default">Inscribirse en la residencia</a>
    </c:if>

</resify:layout>
