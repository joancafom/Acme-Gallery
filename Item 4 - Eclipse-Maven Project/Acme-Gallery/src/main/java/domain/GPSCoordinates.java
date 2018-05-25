
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPSCoordinates {

	private Double	latitude;
	private Double	longitude;


	@DecimalMin("-90.0")
	@DecimalMax("90.0")
	public Double getLatitude() {
		return this.latitude;
	}

	@DecimalMin("-180.0")
	@DecimalMax("180.0")
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final Double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(final Double latitude) {
		this.latitude = latitude;
	}

}
