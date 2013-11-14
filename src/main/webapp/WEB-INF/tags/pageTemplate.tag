<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Header tag" pageEncoding="UTF-8" %>
<%@attribute name="head" fragment="true" required="false" %>
<%@attribute name="content" fragment="true" required="false" %>
<%@attribute name="footer" fragment="true" required="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--
Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License

Name       : Mongoose
Description: A two-column, fixed-width design with dark color scheme.
Version    : 1.0
Released   : 20130920

-->
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" hcontent="text/html; charset=utf-8"/>
    <title></title>
    <base href="<c:url value="/"></c:url>">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600,700,900" rel="stylesheet"/>
    <link href="<c:url value="/css/bootstrap/bootstrap.css"/>" media="screen" rel="stylesheet" type="text/css">
    <link href="<c:url value="/css/bootstrap/bootstrap-responsive.css"/>" media="screen" rel="stylesheet"
          type="text/css">
    <link href="<c:url value="/css/default.css"/>" media="screen" rel="stylesheet" type="text/css" media="all">
    <link href="<c:url value="/css/fonts.css"/>" media="screen" rel="stylesheet" type="text/css" media="all">
    <script type="text/javascript" src="<c:url value="/js/libs/jquery-1.7.2.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery-ui-1.8.19.custom.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/modernizr-2.0.6.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery.i18n.properties-min-1.0.9.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/jquery.validate.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/libs/bootstrap/bootstrap.js"/>"></script>
    <jsp:invoke fragment="head"/>
</head>

<body>
<div id="header-wrapper">
    <div id="header" class="container">
        <div id="logo">
            <h1><a href="#"><strong>Mongoose</strong></a></h1>
        </div>
        <div id="menu">
            <ul>
                <li class="current_page_item"><a href="#" accesskey="1" title="">Homepage</a></li>
                <li><a href="parishioners/list" accesskey="2" title="">Our Clients</a></li>
                <li><a href="#" accesskey="3" title="">About Us</a></li>
                <li><a href="#" accesskey="4" title="">Careers</a></li>
                <li><a href="#" accesskey="5" title="">Contact Us</a></li>
            </ul>
        </div>
    </div>
</div>
<div id="page" class="container">
    <jsp:invoke fragment="content"/>
</div>
</body>

<div id="copyright">
    <p>Copyright (c) 2013 Sitename.com. All rights reserved. | Photos by <a href="http://fotogrph.com/">Fotograph</a> |
        Design by <a href="http://www.freecsstemplates.org/" rel="nofollow">FreeCSSTemplates.org</a>.</p>
</div>
<jsp:invoke fragment="footer"/>
</html>
