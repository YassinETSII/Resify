<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="inscripciones">
    <jsp:body>
        <h2>
            <c:if test="${inscripcion['new']}">Nueva </c:if> Inscripción
        </h2>
        <h2>
            <c:out value="${anciano}"/>
        </h2>
        <form:form modelAttribute="inscripcion"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${inscripcion.id}"/>
            <input type="hidden" name="fecha" value="${inscripcion.fecha}"/>
            <input type="hidden" name="estado" value="${inscripcion.estado}"/>
            <input type="hidden" name="justificacion" value="${inscripcion.justificacion}"/>
            <div class="form-group has-feedback">
                <resify:inputField label="Declaración" name="declaracion"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${inscripcion['new']}">
                            <button class="btn btn-default" type="submit">Registrar Inscripción</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Inscripción</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!residencia['new']}">
        </c:if>
    </jsp:body>
</resify:layout>
