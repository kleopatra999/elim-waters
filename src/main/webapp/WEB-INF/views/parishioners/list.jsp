<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<html>
<t:header>
    <jsp:attribute name="header">
        <link href="<c:url value="/css/elim/tables.css"/>" media="screen" rel="stylesheet" type="text/css">
    </jsp:attribute>
</t:header>
<t:body>
    <jsp:attribute name="content">
        <t:table columns="${table.columns}" records="${table.records}">
        </t:table>
    </jsp:attribute>
</t:body>
<t:footer/>
