package aero.minova.cas.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import lombok.Getter;

@Getter
public class OAuth2Token {

	private static final DateTimeFormatter OAUTHSERVERFORMAT = DateTimeFormatter.ofPattern("ddMMMyyyyHH:mm:ssz", Locale.ENGLISH);

	String token;

	Instant expiryDate;

	public OAuth2Token(String token, String expiryDate) {
		this.token = token;
		setExpiryDate(expiryDate);
	}

	private void setExpiryDate(String expiryDate) {
		try {
			LocalDateTime ldt = LocalDateTime.parse(expiryDate, OAUTHSERVERFORMAT);
			ZonedDateTime zdt = ldt.atZone(TimeZone.getDefault().toZoneId());
			this.expiryDate = zdt.toInstant();
		} catch (Exception e) {
			throw new RuntimeException("Could not parse Expiry Date from the OAuth2 token.", e);
		}
	}

}
