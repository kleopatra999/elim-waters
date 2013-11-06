<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title th:substituteby="index::title"></title>
	<meta name="description" th:substituteby="index::metaDescription" />
	<meta name="author" th:substituteby="index::metaAuthor"/>
	<meta name="viewport" content="width=device-width,initial-scale=1" />
</head>

<body>
	<form class="clearfix form-signin" id="login-form" action="<c:url value="/j_spring_security_check"/>" method="post">
		<div id="login-error" th:text="${error}"></div>
		<p>
			<label for="j_username">User:</label>
			<input id="j_username" class="input-block-level" name="j_username" type="text" />
		</p>
		<p>
			<label for="j_password" >Password:</label>
			<input id="j_password" name="j_password" type="password" />
		</p>
		<input  type="submit" class="btn btn-large btn-primary" value="Login"/>
	</form>
</body>
</html>
