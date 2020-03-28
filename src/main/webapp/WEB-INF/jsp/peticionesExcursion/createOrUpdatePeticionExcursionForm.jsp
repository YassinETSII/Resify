<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<resify:layout pageName="peticionesExcursion">
    <jsp:body>
        <h2>
            Petici�n de Excursion
        </h2>
        <security:authorize access="hasAuthority('manager')">
        <c:if test="${!hasPeticion && hasResidencia}">
        <form:form modelAttribute="peticionExcursion"
                   class="form-horizontal">
			<input type="hidden" name="estado" value="${peticionExcursion.estado}"/>
           
            <div class="form-group has-feedback">
                <resify:inputField label="Declaracion" name="declaracion"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Enviar Petici�n</button>
                </div>
            </div>
        </form:form>
        </c:if>
    <c:if test="${hasPeticion && hasResidencia}">
    <td><c:out value="YA LE HA ENVIADO UNA PECICION"/></td>
    </c:if>
    
    <c:if test="${!hasResidencia}">
    <td><c:out value="NO TIENE UNA RESIDENCIA QUE INSCRIBIR"/></td>
    </c:if>
    </security:authorize>
        
    <security:authorize access="hasAuthority('organizador')">
     <form:form modelAttribute="peticionExcursion" class="form-horizontal">
        <input type="hidden" name="id" value="${peticionExcursion.id}"/>
     
        <input type="hidden" name="fecha" value="${peticionExcursion.fecha}"/>
        <input type="hidden" name="declaracion" value="${peticionExcursion.declaracion}"/>

    	<div class="form-group has-feedback">
			<resify:selectField label="Estado" name="estado" size="3" names="${estados}"/>
		</div>
		<div class="form-group has-feedback">
			<resify:inputField label="Justificaci�n" name="justificacion"/>
		</div>
		<button class="btn btn-default" type="submit">Actualizar Petici�n</button>
		
		
	</form:form>
    </security:authorize>
    </jsp:body>
</resify:layout>