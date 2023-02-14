package kr.edu.mit;

import java.util.ArrayList;
import java.util.List;

public interface FruitStoreDAO {
	
	// [�������� ����]
	// ���� ���
	void insertFruit(FruitVO vo);
	// ���� ��� �����ֱ�
	ArrayList<FruitVO> listFruit();
	// ���� ������Ʈ
	void updateQuantityFruit(FruitVO vo);
	// ���Ϻ� �� ���� �˷��ֱ�
	int totalFruit(FruitVO vo);
	// �Ǹ� ���� �߰�, �Ǹ� ���� �߰� Ű�� �˾� ����, ���� ���̺� ����(�߰�), ��� ���� ó��
	void insertSales(int fruit_quantity, int fruit_code); // Ű���� ����
	// �� �Ǹ� �ݾ�
	long totalPrice();
	// ���� ���� ����
	List<SalesVO> listSales();
	
}
