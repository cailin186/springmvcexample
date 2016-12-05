<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<form action="http://localhost:8080/talkplatform_appoint_consumer/appoint_multi/query_tea_appoint" method="POST">
First Name: <input type="text" value="8888" name="tea_id">
<br />
Last Name: <input type="text" value="12" name="course_type" />
Last Name: <input type="text" value="teacher" name="path" />
<input type="submit" value="Submit" />
<%request.setAttribute("key","aaaaaaaa"); %>
</form>
</body>
</html>