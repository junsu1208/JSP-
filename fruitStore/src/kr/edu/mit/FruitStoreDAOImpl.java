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
		// DB�� �����ؼ� ���� ���
		// Java���� DB�� �����ϴ� ���: JDBC
		// 1. DB ����
		// 1-1. JDBC ����̹� �ҷ�����
		// 1-2. ���� �� Connection ��ü ����
		// 2. ���� �۾�
		// 2-1. Connection ��ü�� Statement ��ü ����
		// 2-2. Statement ��ü�� ���� �۾�(select���� ����� ResultSet ��ü�� �޾Ƽ� �۾�)
		dbConn();
		try {
			pstmt = conn.prepareStatement("insert into fruit(fruit_name, fruit_price, fruit_quantity) value(?, ?, ?)");
			pstmt.setString(1, vo.getFruit_name());  // ?(����ǥ) ä���
			pstmt.setInt(2, vo.getFruit_price()); 
			pstmt.setInt(3, vo.getFruit_quantity());
			pstmt.executeUpdate(); 
			// ����, ����, ���� �ÿ��� executeUpdate(): ��ȯ �� int ó���� ���� ����
			// ���� ��(select)���� executeQuery(): ��ȯ �� ResultSet ��ü�� �ᱣ������ �����ش�.
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��� �� DB ���� ����
		// ResultSet, Statement, Connection ��ü �ݱ�
		dbClose();	
	}

	@Override
	public ArrayList<FruitVO> listFruit() {
		ArrayList<FruitVO> list = new ArrayList<>();
		// 1. DB ����
		dbConn();
		try {
			pstmt = conn.prepareStatement("select * from fruit order by fruit_code");
			ResultSet rs = pstmt.executeQuery(); // 2. ���� �۾� �� ��� ��������(ResultSet)
			// 3. ���� Ÿ������ ��ȯ�ϱ�
			while(rs.next()) { // next(): ���� ���� ����Ŵ, ������ ���� ���� ����Ű�� �����̸� true, ������ false
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
		// 4. DB �ݱ�
		dbClose();
		// 5. ��ȯ�� ���� ����
		return list;
	}
	
	// DB ����
	void dbConn() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "aaa", "Wpqkfehlfk@0");                              
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// DB �ݱ�
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
			pstmt.setInt(2, vo.getFruit_code()); // ���� �ϼ�
			rs = pstmt.executeQuery();
			rs.next(); // ù ��° ���� ����Ŵ
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
			conn.setAutoCommit(false); // �ڵ� Ŀ�� ����
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pstmt = conn.prepareStatement("insert into sales(sales_quantity) value(?)");
			pstmt.setInt(1, fruit_quantity);
			pstmt.executeUpdate();
			rs = pstmt.executeQuery("select last_insert_id()"); // �Էµ� Ű�� Ȯ��
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
			conn.commit(); // �����̸� Ŀ��
		} catch (SQLException e) {
			System.out.println("�Ǹ� ����");
			try { conn.rollback(); } catch (SQLException e1) { e1.printStackTrace(); } // �߰��� ������ ����� �ѹ�
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
			// ������ ������ �����
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
			rs = pstmt.executeQuery(); // ������ �����ؼ� ����� ��������
			rs.next(); //����� ù��° ���� ����Ű��
			totalPrice = rs.getLong(1); // ù��° �Ӽ����� long Ÿ������ ��ȯ�ؼ� �о�´�.
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
		// 1. DB ����
		dbConn();
		try {
			// 2. ���� �۾� �� ��� ��������(ResultSet) 
			String query = "select sales_code, fruit_name, sales_quantity, sales_date, sales_quantity * fruit_price total" + 
					" from sales join" + 
					"	(select * from fruit join fruit_has_sales on(fruit.fruit_code = fruit_has_sales.fruit_fruit_code)) t1" + 
					"    on(sales.sales_code = t1.sales_sales_code)";
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(); 
			// 3. ���� Ÿ������ ��ȯ�ϱ�
			while(rs.next()) { // next(): ���� ���� ����Ŵ, ������ ���� ���� ����Ű�� �����̸� true, ������ false
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
		// 4. DB �ݱ�
		dbClose();
		// 5. ��ȯ�� ���� ����
		return list;
	}

}
