<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과일 입력</title>
</head>
<body>
	<form action="inputPro.jsp" method="post">
		과일 이름: <input type="text" name="fruit_name"> <br>
		과일 가격: <input type="number" name="fruit_price"> <br>
		과일 수량: <input type="number" name="fruit_quantity"> <br>
		<input type="submit" value="입력">
		<button type="button" onclick="goTitle()">메뉴로 돌아가기</button>
	</form>
	<script>
		function goTitle() {
			location.href = "index.html"
		}
	</script>
</body>
</html>