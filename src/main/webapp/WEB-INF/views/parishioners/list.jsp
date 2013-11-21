<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html>
<t:pageTemplate>
    <jsp:attribute name="head">
        <script type="text/javascript" src="<c:url value="/js/controls/RichTableModel.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/parishioner/list.js"/>"></script>
<link href="<c:url value="/css/elim/tables.css"/>" media="screen" rel="stylesheet" type="text/css">
    </jsp:attribute>
    <jsp:attribute name="content">
        <t:table columns="${table.columns}" records="${table.records}">
        </t:table>
    </jsp:attribute>
</t:pageTemplate>
