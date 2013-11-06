<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>


<header>
    <link href="<c:url value="/css/bootstrap/bootstrap.css"/>" media="screen" rel="stylesheet" type="text/css">
    <link href="<c:url value="/css/bootstrap/bootstrap-responsive.css"/>" media="screen" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<c:url value="/js/libs/jquery-1.7.2.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery-ui-1.8.19.custom.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/modernizr-2.0.6.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery.i18n.properties-min-1.0.9.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery.validate.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/bootstrap/bootstrap.js"/>"></script>
</header>
<t:table columns="${table.columns}" records="${table.records}">
</t:table>
<footer></footer>