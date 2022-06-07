package com.cloudera.demo.model;

import java.util.Collection;
import java.util.Date;

import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName = "getInstance")
public class Claim {

	@SuppressWarnings("unused")
	@NonNull
	private String claimId;

	@SuppressWarnings("unused")
	@NonNull
	private String carImageBased64encoding;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@NonNull
	private transient Date claimDate;

	@Setter
	private String customerId;

	@Setter
	private String customerLastName;

	@Setter
	private String customerFirstName;

	@Setter
	private String customerEmail;

	@Setter
	private Double latitude;

	@Setter
	private Double longitude;

	public void setLocation(Metadata metadata) {
		Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);

		for (GpsDirectory gpsDirectory : gpsDirectories) {
			// Try to read out the location, making sure it's non-zero
			GeoLocation geoLocation = gpsDirectory.getGeoLocation();
			if (geoLocation != null && !geoLocation.isZero()) {
				latitude = geoLocation.getLatitude();
				longitude = geoLocation.getLongitude();
				break;
			}
		}
	}
}
