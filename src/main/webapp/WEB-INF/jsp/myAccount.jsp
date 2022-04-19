<%--
  Created by IntelliJ IDEA.
  User: yrkwoun
  Date: 2022-04-18
  Time: 오후 2:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.springframework.boot.configurationprocessor.json.JSONArray" %>
<%@ page import="org.springframework.boot.configurationprocessor.json.JSONObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
  <head>
    <title>내 계좌</title>
    <%@ include file="header.jsp" %>
    <%
      JSONArray result = (JSONArray) request.getAttribute("result");
      JSONObject myAccount = (JSONObject) result.get(0);
      request.setAttribute("myAccount",myAccount.get("balance"));
      request.setAttribute("result",myAccount.get("result"));
    %>
    <script>
      var jsonList = <%=result%>;
      console.log(jsonList);
    </script>
  </head>
<body>
  <%@ include file="menu.jsp" %>
  <table>
    <tr>
      <td><div><h3 class='titleAc'>내 보유자산</h3></div><td>
    </tr>
    <tr>
      <td>
        <div><h1>보유 KRW</h1></div>
        <div><h3 class="myAccount"><fmt:formatNumber value="${ myAccount }" pattern="#,###.###" /> 원</h3></div>
      </td>
      <td>
        <div><h1>총 보유자산</h1></div>
        <div><h3 class="myAccount">계산 해야함 원</h3></div>
      </td>
    </tr>
  </table>
<br>
<br>
  <table>
    <tr>
      <td><div><h2>내가 산 코인 목록</h2></div><td>
    </tr>
    <tr>
<%--      <c:foreach items="${result}" var="jsonObject" varStatus="status">--%>
<%--        <td>--%>
<%--          <c:out value="${jsonObject}" />--%>
<%--&lt;%&ndash;          <div><h1>보유 KRW</h1></div>&ndash;%&gt;--%>
<%--        </td>--%>
<%--      </c:foreach>--%>
    </tr>
  </table>
</body>
</html>
