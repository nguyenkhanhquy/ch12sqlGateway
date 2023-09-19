<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Murach's Java Servlets and JSP</title>
<link rel="stylesheet" href="styles/main.css" type="text/css" />
</head>
<body>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<c:if test="${sqlStatement == null}">
		<c:set var="sqlStatement" value="SELECT * FROM User" />
	</c:if>
	<h1>The SQL Gateway</h1>
	<p>Enter an SQL statement and click the Execute button.</p>
	<p><b>SQL statement:</b></p>
	<form action="sqlGateway" method="post">
		<textarea name="sqlStatement" cols="60" rows="8">${sqlStatement}</textarea><br>
		<input type="submit" value="Execute" id="submit">
	</form>
	<p><b>SQL result:</b></p>
	${sqlResult}
</body>
</html>
