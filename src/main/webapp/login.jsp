<%--
  Created by IntelliJ IDEA.
  User: yrkwoun
  Date: 2022-04-18
  Time: 오후 2:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="org.springframework.boot.configurationprocessor.json.JSONArray" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
  <head>
    <title>내 계좌</title>
  </head>
  <body>
    <%
      JSONArray result = (JSONArray) request.getAttribute("result");
      out.println(result);
    %>
  </body>
</html>
