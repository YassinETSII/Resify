<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fecha").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${excursion['new']}">Nueva </c:if> Excursion
        </h2>
        <h2>
            <c:out value="${organizador}"/>
        </h2>
        <form:form modelAttribute="excursion"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${excursion.id}"/>
            <div class="form-group has-feedback">
                <petclinic:inputField label="Titulo" name="titulo"/>
                <petclinic:inputField label="Descripcion" name="descripcion"/>
                <petclinic:inputField label="Fecha" name="fecha"/>
                <petclinic:inputField label="Hora Inicio" name="horaInicio"/>
                <petclinic:inputField label="Hora Fin" name="horaFin"/>
                <petclinic:inputField label="Aforo" name="aforo"/>
                <petclinic:inputField label="Ratio de aceptacion" name="ratioAceptacion"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${excursion['new']}">
                            <button class="btn btn-default" type="submit">Registrar Excursion</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Excursion</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!excursion['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>
