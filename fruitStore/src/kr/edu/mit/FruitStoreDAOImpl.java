package kr.edu.mit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FruitStoreDAOImpl implements FruitStoreDAO {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	@Override
	public void insertFruit(FruitVO vo) {
		// DB에 연결해서 과일 등록
		// Java에서 DB에 연결하는 방법: JDBC
		// 1. DB 연결
		// 1-1. JDBC 드라이버 불러오기
		// 1-2. 연결 후 Connection 객체 생성
		// 2. 쿼리 작업
		// 2-1. Connection 객체로 Statement 객체 생성
		// 2-2. Statement 객체로 쿼리 작업(select문의 결과는 ResultSet 객체로 받아서 작업)
		dbConn();
		try {
			pstmt = conn.prepareStatement("insert into fruit(fruit_name, fruit_price, fruit_quantity) value(?, ?, ?)");
			pstmt.setString(1, vo.getFruit_name());  // ?(물음표) 채우기
			pstmt.setInt(2, vo.getFruit_price()); 
			pstmt.setInt(3, vo.getFruit_quantity());
			pstmt.executeUpdate(); 
			// 삽입, 삭제, 수정 시에는 executeUpdate(): 반환 값 int 처리된 행의 개수
			// 읽을 시(select)에는 executeQuery(): 반환 값 ResultSet 객체를 결괏값으로 돌려준다.
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 사용 후 DB 연결 끊기
		// ResultSet, Statement, Connection 객체 닫기
		dbClose();	
	}

	@Override
	public ArrayList<FruitVO> listFruit() {
		ArrayList<FruitVO> list = new ArrayList<>();
		// 1. DB 연결
		dbConn();
		try {
			pstmt = conn.prepareStatement("select * from fruit order by fruit_code");
			ResultSet rs = pstmt.executeQuery(); // 2. 쿼리 작업 후 결과 가져오기(ResultSet)
			// 3. 리턴 타입으로 변환하기
			while(rs.next()) { // next(): 다음 행을 가리킴, 리턴은 다음 행을 가리키며 성공이면 true, 없으면 false
				FruitVO vo = new FruitVO();				
				vo.setFruit_code(rs.getInt("fruit_code"));
				vo.setFruit_name(rs.getString("fruit_name"));
				vo.setFruit_price(rs.getInt("fruit_price"));
				vo.setFruit_quantity(rs.getInt("fruit_quantity"));
				list.add(vo);	
//				System.out.println(fruit_code + " " + fruit_name + " " + fruit_price + " " + fruit_quantity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 4. DB 닫기
		dbClose();
		// 5. 변환된 것을 리턴
		return list;
	}
	
	// DB 연결
	void dbConn() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "aaa", "Wpqkfehlfk@0");                              
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// DB 닫기
	void dbClose() {
		if (rs != null) { try { rs.close(); } catch (SQLException e) { e.printStackTrace(); } }
		if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); } }
		if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
	}

	@Override
	public void updateQuantityFruit(FruitVO vo) {
		dbConn();
		try {
			pstmt = conn.prepareStatement("update fruit set fruit_quantity = fruit_quantity + ? where fruit_code = ?");
			pstmt.setInt(1, vo.getFruit_quantity());
			pstmt.setInt(2, vo.getFruit_code());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
	}

	@Override
	public int totalFruit(FruitVO vo) {
		dbConn();
		int total = -1;
		try {
			pstmt = conn.prepareStatement("select fruit_price * ? from fruit where fruit_code = ?");
			pstmt.setInt(1, vo.getFruit_quantity());
			pstmt.setInt(2, vo.getFruit_code()); // 쿼리 완성
			rs = pstmt.executeQuery();
			rs.next(); // 첫 번째 행을 가리킴
			total = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return total;
	}

	@Override
	public void insertSales(int fruit_code, int fruit_quantity) {
		dbConn();
		try {
			conn.setAutoCommit(false); // 자동 커밋 금지
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt = conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1, fruit_quantity);
			pstmt.executeUpdate();
			rs = pstmt.executeQuery("select last_insert_id()"); // 입력된 키값 확인
			rs.next();
			int key = rs.getInt(1);		
			// pstmt.executeLargeUpdate("insert into fruit_has_sales values(" + fruit_code + ", " + key +  ")");
			// or
			pstmt.close();
			pstmt = conn.prepareStatement("insert into fruit_has_sales values(?, ?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, key);
			pstmt.executeUpdate();		
			pstmt.close();
			pstmt = conn.prepareStatement("update fruit set fruit_quantity = fruit_quantity - ? where fruit_code = ?");
			pstmt.setInt(1, fruit_quantity);
			pstmt.setInt(2, fruit_code);
			pstmt.executeUpdate();
			conn.commit(); // 정상이면 커밋
		} catch (SQLException e) {
			System.out.println("판매 실패");
			try { conn.rollback(); } catch (SQLException e1) { e1.printStackTrace(); } // 중간에 문제가 생기면 롤백
			e.printStackTrace();
		} finally {
			dbClose();
		}
	}

	@Override
	public long totalPrice() {
		dbConn();
		long totalPrice = -1;
		try {
			// 수행할 쿼리를 만들고
			String query = "select sum(fruit_price * sales_quantity)" + 
					" from fruit " + 
					"     join" + 
					"	 (select fruit_fruit_code, sales_date, sales_quantity" + 
					"	  from fruit_has_sales" + 
					"		   join " + 
					"           sales " + 
					"           on fruit_has_sales.sales_sales_code = sales.sales_code" + 
					"      ) t1" + 
					"      on fruit.fruit_code = t1.fruit_fruit_code";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery(); // 쿼리를 수행해서 결과를 가져오고
			rs.next(); //결과에 첫번째 행을 가르키고
			totalPrice = rs.getLong(1); // 첫번째 속성값을 long 타입으로 변환해서 읽어온다.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return totalPrice;
	}

	@Override
	public List<SalesVO> listSales() {
		List<SalesVO> list = new ArrayList<>();
		// 1. DB 연결
		dbConn();
		try {
			// 2. 쿼리 작업 후 결과 가져오기(ResultSet) 
			String query = "select sales_code, fruit_name, sales_quantity, sales_date, sales_quantity * fruit_price total" + 
					" from sales join" + 
					"	(select * from fruit join fruit_has_sales on(fruit.fruit_code = fruit_has_sales.fruit_fruit_code)) t1" + 
					"    on(sales.sales_code = t1.sales_sales_code)";
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(); 
			// 3. 리턴 타입으로 변환하기
			while(rs.next()) { // next(): 다음 행을 가리킴, 리턴은 다음 행을 가리키며 성공이면 true, 없으면 false
				SalesVO vo = new SalesVO();				
				vo.setFruit_code(rs.getInt("sales_code"));
				vo.setFruit_name(rs.getString("fruit_name"));
				vo.setTotal(rs.getInt("total"));
				vo.setSales_date(rs.getDate("sales_date"));
				vo.setSales_quantity(rs.getInt("sales_quantity"));
				list.add(vo);
				// System.out.println(code + " " + name + " " + fruit_price + " " + fruit_quantity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 4. DB 닫기
		dbClose();
		// 5. 변환된 것을 리턴
		return list;
	}

}
