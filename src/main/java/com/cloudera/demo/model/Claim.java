package com.cloudera.demo.model;

import java.util.Collection;
import java.util.Date;

import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claim {

	@NonNull
	private String claimId;

	@NonNull
	private String carImageBased64encoding;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@NonNull
	private transient Date claimDate;

	private String customerId;

	private Double latitude;

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
