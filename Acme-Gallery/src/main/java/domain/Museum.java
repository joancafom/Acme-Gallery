
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Museum extends DomainEntity {

	private String			name;
	private String			address;
	private GPSCoordinates	coordinates;
	private String			email;
	private String			phoneNumber;
	private String			title;
	private String			identifier;
	private String			banner;
	private Double			price;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	@NotNull
	@Valid
	public GPSCoordinates getCoordinates() {
		return this.coordinates;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	@NotBlank
	@Pattern(regexp = "^\\+?[0-9]{9,}$")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^([A-Za-z]){1,4}$")
	public String getIdentifier() {
		return this.identifier;
	}

	@URL
	public String getBanner() {
		return this.banner;
	}

	@NotNull
	@Digits(integer = 4, fraction = 2)
	@DecimalMin("1.0")
	public Double getPrice() {
		return this.price;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setCoordinates(final GPSCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}


	// Relationships ----------------------------------------------------------------------------------

	private Director			director;
	private Collection<Group>	groups;
	private Collection<Review>	reviews;
	private Collection<DayPass>	dayPasses;
	private Collection<Room>	rooms;
	private Store				store;
	private Collection<Guide>	guides;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Director getDirector() {
		return this.director;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "museum")
	public Collection<Group> getGroups() {
		return this.groups;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "museum")
	public Collection<Review> getReviews() {
		return this.reviews;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "museum")
	public Collection<DayPass> getDayPasses() {
		return this.dayPasses;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "museum")
	public Collection<Room> getRooms() {
		return this.rooms;
	}

	@Valid
	@OneToOne(optional = true)
	public Store getStore() {
		return this.store;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<Guide> getGuides() {
		return this.guides;
	}

	public void setDirector(final Director director) {
		this.director = director;
	}

	public void setGroups(final Collection<Group> groups) {
		this.groups = groups;
	}

	public void setReviews(final Collection<Review> reviews) {
		this.reviews = reviews;
	}

	public void setDayPasses(final Collection<DayPass> dayPasses) {
		this.dayPasses = dayPasses;
	}

	public void setRooms(final Collection<Room> rooms) {
		this.rooms = rooms;
	}

	public void setStore(final Store store) {
		this.store = store;
	}

	public void setGuides(final Collection<Guide> guides) {
		this.guides = guides;
	}

}
