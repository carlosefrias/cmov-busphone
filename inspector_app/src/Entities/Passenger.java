package Entities;

public class Passenger {
	private String login, password, fullName, creditCardType,
			creditCardValidity;
	private long creditCardNumber;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCardValidity() {
		return creditCardValidity;
	}

	public void setCreditCardValidity(String creditCardValidity) {
		this.creditCardValidity = creditCardValidity;
	}

	public long getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(int creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public Passenger() {
		super();
	}

	public Passenger(String l, String p, String fN, String cCT, long cCN,
			String cCV) {
		login = l;
		password = p;
		fullName = fN;
		creditCardType = cCT;
		creditCardNumber = cCN;
		creditCardValidity = cCV;
	}

	@Override
	public String toString() {
		return "login: " + login + ";\nName: " + fullName
				+ ";\nCreditcard number: " + creditCardNumber
				+ ";\nCreditcard type: " + creditCardType
				+ ";\nCreditcard validity: " + creditCardValidity;
	}
}
