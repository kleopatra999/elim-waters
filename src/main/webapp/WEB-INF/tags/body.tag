<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Body tag" pageEncoding="UTF-8" %>
<%@attribute name="content" fragment="true" required="false" %>

<body>
<div id="header-wrapper">
    <div id="header" class="container">
        <div id="logo">
            <h1><a href="#"><strong>Mongoose</strong></a></h1>
        </div>
        <div id="menu">
            <ul>
                <li class="current_page_item"><a href="#" accesskey="1" title="">Homepage</a></li>
                <li><a href="#" accesskey="2" title="">Our Clients</a></li>
                <li><a href="#" accesskey="3" title="">About Us</a></li>
                <li><a href="#" accesskey="4" title="">Careers</a></li>
                <li><a href="#" accesskey="5" title="">Contact Us</a></li>
            </ul>
        </div>
    </div></div>
<div id="page" class="container">
    <jsp:invoke fragment="content"/>
</div>
</body>
