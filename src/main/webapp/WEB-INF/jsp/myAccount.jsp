<%--
  Created by IntelliJ IDEA.
  User: yrkwoun
  Date: 2022-04-18
  Time: 오후 2:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<head>
    <title>내 계좌</title>
    <%@ include file="header.jsp" %>
</head>
<body>
<%@ include file="menu.jsp" %>
<table>
    <tr>
        <td>
            <div><h3 class='titleAc'>내 보유자산</h3></div>
        <td>
    </tr>
    <tr>
        <td>
            <div><h1>보유 KRW</h1></div>
            <div>
                <h3 class="myAccount">
                    <fmt:formatNumber value="${ accountList.get(0).balance }" pattern="#,###.###"/> 원
                </h3>
            </div>
        </td>
        <td>
            <div><h1>주문 중 묶여있는 금액/수량</h1></div>
            <div>
                <h3 class="myAccount">
                    <c:if test="${ empty accountList.get(0).locked}">
                        0 원
                    </c:if>
                    <c:if test="${ !empty accountList.get(0).locked}">
                        <fmt:formatNumber value="${ accountList.get(0).locked }" pattern="#,###.###"/> 원
                    </c:if>
                </h3>
            </div>
        </td>
        <td>
            <div><h1>총 보유자산</h1></div>
            <div><h3 class="myAccount">계산 해야함 원</h3></div>
        </td>
    </tr>
</table>
<br>
<br>
<div><h2>내가 산 코인 목록</h2></div>
<table class="table table-hover">
    <thead class="thead-light">
    <tr>
        <th scope="col">no.</th>
        <th scope="col">currency</th>
        <th scope="col">balance</th>
        <th scope="col">locked</th>
        <th scope="col">avg_buy_price</th>
        <th scope="col">avg_buy_price_modified</th>
        <th scope="col">unit_currency</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="json" items="${accountList}" varStatus="status" begin="1">
    <tr>
        <th scope="row">${status.index}</th>
        <td><c:out value="${json.currency}"/></td>
        <td><c:out value="${json.balance}"/></td>
        <td><c:out value="${json.locked}"/></td>
        <td><c:out value="${json.avg_buy_price}"/></td>
        <td><c:out value="${json.avg_buy_price_modified}"/></td>
        <td><c:out value="${json.unit_currency}"/></td>
    </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
