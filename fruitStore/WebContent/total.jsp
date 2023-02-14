<%@page import="java.util.List"%>
<%@page import="kr.edu.mit.SalesVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>매출 확인</title>
</head>
<body>
<%
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	List<SalesVO> list = dao.listSales();
%>
	<table border="1">
		<tr> <th>과일 코드</th> <th>과일 이름</th> <th>판매 수량</th> <th>판매 금액</th> <th>판매일자</th>
		</tr>
	<%	
		for (SalesVO vo : list) { %>
		<tr>
			<td><%= vo.getFruit_code() %></td>
			<td><%= vo.getFruit_name() %></td>
			<td><%= vo.getSales_quantity() %></td>
			<td><%= vo.getTotal() %></td>
			<td><%= vo.getSales_date() %></td>
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
</body>
</html>