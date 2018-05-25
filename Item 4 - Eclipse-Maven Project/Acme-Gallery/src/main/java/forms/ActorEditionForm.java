
package forms;

public class ActorEditionForm {

	//Actor
	private String	name;
	private String	surnames;
	private String	email;
	private String	phoneNumber;
	private String	address;
	private String	gender;


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

}
