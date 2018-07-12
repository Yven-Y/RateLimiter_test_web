<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>登陆</title>
</head>
<body>
	<c:if test="${!empty error}">
		<font color="red"><c:out value="${error}" /></font>
	</c:if>
	<form action="<c:url value="/loginCheck.html"/>" method="post">
		用户名： <input type="text" name="username"> <br /> 密&nbsp;&nbsp;&nbsp;码： <input
			type="password" name="password"> <br /> <input type="submit"
			value="提交"> <input type="reset" value="重置">
	</form>
</body>
</html>