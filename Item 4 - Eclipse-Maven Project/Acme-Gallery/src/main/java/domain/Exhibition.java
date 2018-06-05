
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "ticker, title, description, endingDate, startingDate, isPrivate")
})
public class Exhibition extends DomainEntity {

	private String				ticker;
	private String				title;
	private String				description;
	private Date				startingDate;
	private Date				endingDate;
	private Collection<String>	websites;
	private boolean				isPrivate;
	private Double				price;


	@NotBlank
	@Pattern(regexp = "^([A-Za-z0-9]{5,}-[A-Za-z0-9]{4,})$")
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Column(columnDefinition = "VARCHAR(1000)")
	public String getDescription() {
		return this.description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getStartingDate() {
		return this.startingDate;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEndingDate() {
		return this.endingDate;
	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getWebsites() {
		return this.websites;
	}

	@NotNull
	public boolean getIsPrivate() {
		return this.isPrivate;
	}

	@NotNull
	@Digits(integer = 4, fraction = 2)
	@DecimalMin("0.0")
	public Double getPrice() {
		return this.price;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setStartingDate(final Date startingDate) {
		this.startingDate = startingDate;
	}

	public void setEndingDate(final Date endingDate) {
		this.endingDate = endingDate;
	}

	public void setWebsites(final Collection<String> websites) {
		this.websites = websites;
	}

	public void setIsPrivate(final boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Collection<DayPass>		dayPasses;
	private Collection<Sponsorship>	sponsorships;
	private Collection<Critique>	critiques;
	private Category				category;
	private Collection<Artwork>		artworks;
	private Collection<Guide>		guides;
	private Room					room;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "exhibition")
	public Collection<DayPass> getDayPasses() {
		return this.dayPasses;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "exhibition")
	public Collection<Sponsorship> getSponsorships() {
		return this.sponsorships;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "exhibition")
	public Collection<Critique> getCritiques() {
		return this.critiques;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Category getCategory() {
		return this.category;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "exhibition")
	public Collection<Artwork> getArtworks() {
		return this.artworks;
	}

	@NotNull
	@Valid
	@ManyToMany(mappedBy = "exhibitions")
	public Collection<Guide> getGuides() {
		return this.guides;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Room getRoom() {
		return this.room;
	}

	public void setDayPasses(final Collection<DayPass> dayPasses) {
		this.dayPasses = dayPasses;
	}

	public void setSponsorships(final Collection<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}

	public void setCritiques(final Collection<Critique> critiques) {
		this.critiques = critiques;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	public void setArtworks(final Collection<Artwork> artworks) {
		this.artworks = artworks;
	}

	public void setGuides(final Collection<Guide> guides) {
		this.guides = guides;
	}

	public void setRoom(final Room room) {
		this.room = room;
	}

}
