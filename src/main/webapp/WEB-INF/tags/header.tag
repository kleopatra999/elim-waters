<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Header tag" pageEncoding="UTF-8" %>
<%@attribute name="header" fragment="true" required="false" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600,700,900" rel="stylesheet" />
    <link href="<c:url value="/css/bootstrap/bootstrap.css"/>" media="screen" rel="stylesheet" type="text/css">
    <link href="<c:url value="/css/bootstrap/bootstrap-responsive.css"/>" media="screen" rel="stylesheet" type="text/css">
    <link href="<c:url value="/css/default.css"/>" media="screen" rel="stylesheet" type="text/css" media="all">
    <link href="<c:url value="/css/fonts.css"/>" media="screen" rel="stylesheet" type="text/css" media="all">
    <script type="text/javascript" src="<c:url value="/js/libs/jquery-1.7.2.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery-ui-1.8.19.custom.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/modernizr-2.0.6.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery.i18n.properties-min-1.0.9.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery.validate.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/bootstrap/bootstrap.js"/>"></script>
    <jsp:invoke fragment="header"/>
</head>

