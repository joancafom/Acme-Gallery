
package forms;

import java.util.List;

import domain.Exhibition;

public class ExhibitionForm {

	private List<String>	guides;

	private Exhibition		exhibition;


	public List<String> getGuides() {
		return this.guides;
	}

	public void setGuides(final List<String> guides) {
		this.guides = guides;
	}

	public Exhibition getExhibition() {
		return this.exhibition;
	}

	public void setExhibition(final Exhibition exhibition) {
		this.exhibition = exhibition;
	}

}
