package test;

import kr.edu.mit.FruitStoreDAO;
import kr.edu.mit.FruitStoreDAOImpl;
import kr.edu.mit.FruitVO;

public class Test {

	public static void main(String[] args) {
		// 자바 클래스 테스트
		FruitStoreDAO dao = new FruitStoreDAOImpl();
		FruitVO vo = new FruitVO();
		vo.setFruit_name("Apple");
		vo.setFruit_price(999);
		vo.setFruit_quantity(99);
		dao.insertFruit(vo);
	}

}
