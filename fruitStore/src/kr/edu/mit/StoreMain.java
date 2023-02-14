package kr.edu.mit;

import java.util.List;
import java.util.Scanner;

public class StoreMain {

	public static void main(String[] args) {
		// 1. ���� �԰�, 2. ��� �ľ��ϱ�, 3. �Ǹ��ϱ�, 4. ���� Ȯ��
		Scanner in = new Scanner(System.in);
		FruitStoreDAO dao = new FruitStoreDAOImpl();
		int menuNum;
		do {
			// �޴� ����ϱ�
			System.out.println("[���� �޴�]");
			System.out.println("1. ���� �Է�");
			System.out.println("2. ��� �ľ��ϱ�");
			System.out.println("3. �Ǹ��ϱ�");
			System.out.println("4. ���� Ȯ��");
			System.out.print("�޴��� �������ּ���(0�� ����): ");
			// ����ڷκ��� �Է¹ް� �ش� �޴� �����ϱ�
			menuNum = in.nextInt();
			if (menuNum == 1) {
				System.out.println("���� �Է� ����Դϴ�.");
				// 1. ���� ��� �����ֱ�
				// 2. ����/�߰� ���θ� �Է¹ް�
				// 3-1. ������ ���
				//    	�԰� ������ �ް� DB ó��(������Ʈ) �� ������� �� ������ ���� �ڵ�, ����
				// 3-2. �߰��� ���
				//    	���� �̸�, ����, ������ �ް� DB ó��(����) 
			} else if (menuNum == 2) {
				System.out.println("��� �ľ��ϱ� ����Դϴ�.");
			} else if (menuNum == 3) {
				// 1. ���� ��� �����ֱ� - (DB) ���� ��� �����ֱ� 
				List<FruitVO> list = dao.listFruit();
				for(FruitVO vo: list) {
					System.out.println(vo);
				}
				System.out.println("���� ����Դϴ�. ���� ��ȣ�� �������ּ���: ");
				// 2. ����ڰ� ����(�ڵ�, ����)
				int fruit_code = in.nextInt(); // ���� ��ȣ �Է�
				System.out.println("������ ������ �Է��� �ּ���: ");
				int fruit_quantity = in.nextInt(); // ���� ����		
				// 3. ���� �ݾ� �ȳ� - (DB) ���Ϻ� �� ���� �˷��ֱ�
				FruitVO vo = new FruitVO();
				vo.setFruit_code(fruit_code);
				vo.setFruit_quantity(fruit_quantity);
				System.out.println("�� ���� �ݾ��� " + dao.totalFruit(vo) + "�Դϴ�.");
				System.out.println("�����Ͻðڽ��ϱ�?(1: ����, 2: ���)");
				if (in.nextInt() == 1) {
					// 4. �Ǹ� �Ϸ� - (DB) �Ǹ� ó��
					System.out.println("�Ǹ� DB �ۼ���");
					dao.insertSales(fruit_code, fruit_quantity);
				}
			} else if (menuNum == 4) {
				System.out.println("���� Ȯ�� ����Դϴ�.");
			} else if (menuNum == 0) {
				System.out.println("�̿����ּż� �����մϴ�.");
			}
		} while (menuNum != 0);
		in.close();
	}

}
