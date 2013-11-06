<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title th:fragment="title">Elim</title>
<meta name="description" content="Elim pentecostal church members" th:fragment="metaDescription"/>
<meta name="author" content="wilczur" th:fragment="metaAuthor"/>

<meta name="viewport" content="width=device-width,initial-scale=1" />

<div th:fragment="config">
	<link rel="stylesheet" th:href="@{/css/jquery-ui-1.8.19.custom.css}"/>
	<link rel="stylesheet" th:href="@{/css/style.css}"/>
    <link rel="stylesheet" th:href="@{/css/bootstrap/bootstrap.css}"/>
    <link rel="stylesheet" th:href="@{/css/glance.css}"/>

	<script type="text/javascript" th:fragment="requirejsConfig"> 
		var require = {
			appDir: "../",
			baseUrl: "/elim/js",
			paths: { 
				jquery: "require-jquery",
				dataTables: "data-tables",
				ajaxFileUpload: "libs/ajaxfileupload",
                bootstrap:"libs/bootstrap/bootstrap.js"
            }
		};
	</script> 
	<script th:src="@{/js/libs/jquery-1.7.2.min.js}"></script>
	<script th:src="@{/js/libs/jquery-ui-1.8.19.custom.min.js}"></script>
	<script th:src="@{/js/libs/modernizr-2.0.6.min.js}"></script>
	<script th:src="@{/js/libs/jquery.i18n.properties-min-1.0.9.js}"></script>
	<script th:src="@{/js/libs/jquery.validate.min.js}"></script>
	<script th:src="@{/js/libs/angular/angular.min.js}"></script>
	<script th:src="@{/js/requirejs.js}"></script>
	<script th:src="@{/js/main.js}"></script>
    <script th:src="@{/js/libs/bootstrap/bootstrap.js}"></script>
</div>
</head>

<body>
	<header th:fragment="header">
            <h1>Elim pentecostal church</h1>
           	<section class="auth-menu">
           		<a th:href="@{/auth/login}" 
           		th:if="${not sec.isAuthenticated()}">Login</a>
           		<a th:href="@{/auth/logout}">Logout</a>
           	</section>
			<nav class="menu">
				<ul>
					<li><a href="/elim">Home</a></li>
					<li><a href="#">About</a></li>
					<li><a href="parishioners/list.html" 
						th:href="@{/parishioners/list}">Parishioners</a></li>
					<li><a href="caregivers/list.html" 
						th:href="@{/caregivers/list}">Caregivers</a></li>
					<li><a href="#">Contact</a></li>
					<li><a href="#">Support</a></li>
					
				</ul>
			</nav>
	</header>
	<div id="container">
        <section class="hidden">
			<a class="button" href="parishioners/create.html" th:href="@{'parishioners/create'}">Add New Member</a>
			<a class="button" href="index.html" th:href="@{'parishioners/import?excel'}">Import From Excel</a>
			<form name="form" action="rest/parishioners/upload-excel" method="POST" enctype="multipart/form-data">
				<img id="loading" src="/elim/images/loading.gif" th:src="@{/images/loading.gif}" style="display:none;"/>
				<input id="chooseExcelFile" type="file" name="fileToUpload"/>
				<button id="importFromExcelFile">Upload</button>
			</form>
		</section>
	
		<article id="home">
			<img id="homeImage" class="home" src="/elim/images/elim.jpg" th:src="@{/images/elim.jpg}"/>
		</article>
		
	</div><!-- end of #container -->
	<footer th:fragment="footer">
		<section>
			<h3>Biserica penticostala Elim</h3>
			<p>Str. Frunzisului, nr. 25 (varianta Zorilor-Manastur)</p>
			<p>Cluj-Napoca, Romania</p>
		</section>
		
		<section>
			<h3>Center Stuff</h3>
			<p>Center Text here. Proin metus odio, ultricies eu pharetra dictum, laoreet id odio. Curabitur in odio augue. Morbi congue auctor interdum.</p>
		</section>
		
		<section>
			<h3>Copyright</h3>
			<p>&copy; 2012 <a href="#" title="Elim">ec2-23-22-228-16.compute-1.amazonaws.com</a> All rights reserved.</p>
		</section>

	</footer>
	<section class="jbar jbar-bottom hidden" th:fragment="jbar">
		Your changes were sussessfully saved into the database!
	</section>
</body>
</html>
