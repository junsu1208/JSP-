package kr.edu.mit;

public class FruitVO {
	
	// 코드
	private int fruit_code;
	
	// 이름
	private String fruit_name;
	
	// 가격
	private int fruit_price;
	
	// 수량
	private int fruit_quantity;

	public int getFruit_code() {
		return fruit_code;
	}

	public void setFruit_code(int fruit_code) {
		this.fruit_code = fruit_code;
	}

	public String getFruit_name() {
		return fruit_name;
	}

	public void setFruit_name(String fruit_name) {
		this.fruit_name = fruit_name;
	}

	public int getFruit_price() {
		return fruit_price;
	}

	public void setFruit_price(int fruit_price) {
		this.fruit_price = fruit_price;
	}

	public int getFruit_quantity() {
		return fruit_quantity;
	}

	public void setFruit_quantity(int fruit_quantity) {
		this.fruit_quantity = fruit_quantity;
	}

	@Override
	public String toString() {
		return "FruitVO [fruit_code =" + fruit_code + ", fruit_name = " + fruit_name + ", fruit_price = " + fruit_price
				+ ", fruit_quantity =" + fruit_quantity + "]";
	}

}
