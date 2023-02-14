package kr.edu.mit;

import java.util.List;
import java.util.Scanner;

public class StoreMain {

	public static void main(String[] args) {
		// 1. 과일 입고, 2. 재고 파악하기, 3. 판매하기, 4. 매출 확인
		Scanner in = new Scanner(System.in);
		FruitStoreDAO dao = new FruitStoreDAOImpl();
		int menuNum;
		do {
			// 메뉴 출력하기
			System.out.println("[메인 메뉴]");
			System.out.println("1. 과일 입력");
			System.out.println("2. 재고 파악하기");
			System.out.println("3. 판매하기");
			System.out.println("4. 매출 확인");
			System.out.print("메뉴를 선택해주세요(0은 종료): ");
			// 사용자로부터 입력받고 해당 메뉴 실행하기
			menuNum = in.nextInt();
			if (menuNum == 1) {
				System.out.println("과일 입력 기능입니다.");
				// 1. 과일 목록 보여주기
				// 2. 선택/추가 여부를 입력받고
				// 3-1. 선택일 경우
				//    	입고 수량을 받고 DB 처리(업데이트) → 보내줘야 할 내용인 과일 코드, 수량
				// 3-2. 추가일 경우
				//    	과일 이름, 가격, 수량을 받고 DB 처리(삽입) 
			} else if (menuNum == 2) {
				System.out.println("재고 파악하기 기능입니다.");
			} else if (menuNum == 3) {
				// 1. 과일 목록 보여주기 - (DB) 과일 목록 보여주기 
				List<FruitVO> list = dao.listFruit();
				for(FruitVO vo: list) {
					System.out.println(vo);
				}
				System.out.println("과일 목록입니다. 과일 번호를 선택해주세요: ");
				// 2. 사용자가 선택(코드, 개수)
				int fruit_code = in.nextInt(); // 과일 번호 입력
				System.out.println("구매할 수량을 입력해 주세요: ");
				int fruit_quantity = in.nextInt(); // 과일 수량		
				// 3. 지불 금액 안내 - (DB) 과일별 총 가격 알려주기
				FruitVO vo = new FruitVO();
				vo.setFruit_code(fruit_code);
				vo.setFruit_quantity(fruit_quantity);
				System.out.println("총 구매 금액은 " + dao.totalFruit(vo) + "입니다.");
				System.out.println("구매하시겠습니까?(1: 구매, 2: 취소)");
				if (in.nextInt() == 1) {
					// 4. 판매 완료 - (DB) 판매 처리
					System.out.println("판매 DB 작성중");
					dao.insertSales(fruit_code, fruit_quantity);
				}
			} else if (menuNum == 4) {
				System.out.println("매출 확인 기능입니다.");
			} else if (menuNum == 0) {
				System.out.println("이용해주셔서 감사합니다.");
			}
		} while (menuNum != 0);
		in.close();
	}

}
