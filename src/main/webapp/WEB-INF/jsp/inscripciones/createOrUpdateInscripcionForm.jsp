<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="inscripciones">
    <jsp:body>
        <h2>
            <c:if test="${inscripcion['new']}">Nueva </c:if> Inscripci�n
        </h2>
        <form:form modelAttribute="inscripcion"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${inscripcion.id}"/>
            <input type="hidden" name="fecha" value="${inscripcion.fecha}"/>
            <input type="hidden" name="residencia" value="${inscripcion.residencia}"/>
            <c:choose>
				<c:when test="${inscripcion['new']}">
					<input type="hidden" name="estado" value="${inscripcion.estado}"/>
				</c:when>
				<c:otherwise>
					<div class="form-group has-feedback">
						<resify:selectField label="Estado" name="estado" size="3" names="${estados}"/>
					</div>
				</c:otherwise>
            </c:choose>
            <c:choose>
				<c:when test="${inscripcion['new']}">
					<input type="hidden" name="justificacion" value="${inscripcion.justificacion}"/>
				</c:when>
				<c:otherwise>
					<div class="form-group has-feedback">
						<resify:inputField label="Justificaci�n" name="justificacion"/>
					</div>
				</c:otherwise>
            </c:choose>
            <c:choose>
				<c:when test="${inscripcion['new']}">
					<div class="form-group has-feedback">
                		<resify:inputField label="Declaraci�n" name="declaracion"/>
            		</div>
				</c:when>
				<c:otherwise>
					<input type="hidden" name="declaracion" value="${inscripcion.declaracion}"/>
				</c:otherwise>
            </c:choose>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${inscripcion['new']}">
                            <button class="btn btn-default" type="submit">Registrar Inscripci�n</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Inscripci�n</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!residencia['new']}">
        </c:if>
    </jsp:body>
</resify:layout>
