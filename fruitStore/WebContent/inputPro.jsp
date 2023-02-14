<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과일 입력 처리</title>
</head>
<body>
<% 
	// 한글 깨짐 방지를 위한 인코딩 변환(UTF-8)
	request.setCharacterEncoding("UTF-8");
	
	// 과일 입력 DB 처리
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	FruitVO vo = new FruitVO();
	vo.setFruit_name(request.getParameter("fruit_name"));
	vo.setFruit_price(Integer.parseInt(request.getParameter("fruit_price")));
	vo.setFruit_quantity(Integer.parseInt(request.getParameter("fruit_quantity")));
	dao.insertFruit(vo);
%>
	<script>
		location.href = "index.html"
	</script>
</body>
</html>