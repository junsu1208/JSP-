<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재고 파악하기</title>
</head>
<body>
<%
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	ArrayList<FruitVO> list = dao.listFruit();
%>
	<table border="1">
		<tr> <th>과일 코드</th> <th>이름</th> <th>가격</th> <th>재고</th>
		</tr>
<%
	for (FruitVO vo : list) { %>
		<tr> 
			<td><%= vo.getFruit_code() %></td>
			<td><%= vo.getFruit_name() %></td>
			<td><%= vo.getFruit_price() %></td>
			<td><%= vo.getFruit_quantity() %></td>
		</tr>
<%	} 
%>
	</table>
	<button type="button" onclick="goTitle()">메뉴로 돌아가기</button>
	<script>
		function goTitle() {
			location.href = "index.html"
		}
	</script>
</html>