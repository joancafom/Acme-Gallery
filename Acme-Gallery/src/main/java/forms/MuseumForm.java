
package forms;

import java.util.List;

import domain.Museum;

public class MuseumForm {

	private List<String>	guides;

	private Museum			museum;


	public List<String> getGuides() {
		return this.guides;
	}

	public void setGuides(final List<String> guides) {
		this.guides = guides;
	}

	public Museum getMuseum() {
		return this.museum;
	}

	public void setMuseum(final Museum museum) {
		this.museum = museum;
	}

}
