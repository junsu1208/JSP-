package kr.edu.mit;

import java.util.ArrayList;
import java.util.List;

public interface FruitStoreDAO {
	
	// [쿼리별로 구현]
	// 과일 등록
	void insertFruit(FruitVO vo);
	// 과일 목록 보여주기
	ArrayList<FruitVO> listFruit();
	// 수량 업데이트
	void updateQuantityFruit(FruitVO vo);
	// 과일별 총 가격 알려주기
	int totalFruit(FruitVO vo);
	// 판매 내용 추가, 판매 내용 추가 키값 알아 오기, 교차 테이블 갱신(추가), 재고 수정 처리
	void insertSales(int fruit_quantity, int fruit_code); // 키값을 리턴
	// 총 판매 금액
	long totalPrice();
	// 매출 내역 보기
	List<SalesVO> listSales();
	
}
