<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="peticionExcursion">
    <jsp:body>
        <h2>
            Petición de Excursion
        </h2>
        <h2>
            <c:out value="${manager}"/>
        </h2>
        <c:if test="${!hasPeticion && hasResidencia}">
        <form:form modelAttribute="peticionExcursion"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${peticionExcursion.id}"/>
			<input type="hidden" name="estado" value="${peticionExcursion.estado}"/>
            
            <div class="form-group has-feedback">
                <petclinic:inputField label="Declaracion" name="declaracion"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Enviar Petición</button>
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
        
    </jsp:body>
</petclinic:layout>
