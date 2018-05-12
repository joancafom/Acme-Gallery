
package forms;

public class ActorRegistrationForm {

	//UserAccount
	private String	username;
	private String	password;

	//Actor
	private String	name;
	private String	surnames;
	private String	email;
	private String	phoneNumber;
	private String	address;
	private String	gender;

	//Form
	private boolean	acceptedTerms;
	private String	passwordConfirmation;


	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getName() {
		return this.name;
	}

	public String getSurnames() {
		return this.surnames;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getAddress() {
		return this.address;
	}

	public String getGender() {
		return this.gender;
	}

	public boolean getAcceptedTerms() {
		return this.acceptedTerms;
	}

	public String getPasswordConfirmation() {
		return this.passwordConfirmation;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSurnames(final String surnames) {
		this.surnames = surnames;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	public void setAcceptedTerms(final boolean acceptedTerms) {
		this.acceptedTerms = acceptedTerms;
	}

	public void setPasswordConfirmation(final String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

}
