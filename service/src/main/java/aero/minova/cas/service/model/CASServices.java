package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasCASServices")
public class CASServices {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@Size(max = 50)
	@Column(name = "KeyText")
	public String keytext;

	@NotNull
	@Size(max = 50)
	@Column(name = "ServiceURL")
	public String serviceurl;

	@NotNull
	@Column(name = "Port")
	public int port;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ServiceMessageReceiverLoginTypeKey", nullable = true)
	public ServiceMessageReceiverLoginType receiverLoginType;

	@Size(max = 50)
	@Column(name = "Username")
	public String username;

	@Size(max = 50)
	@Column(name = "Password")
	public String password;

	@Size(max = 50)
	@Column(name = "ClientID")
	public String clientId;

	@Size(max = 50)
	@Column(name = "ClientSecret")
	public String clientSecret;

	@Size(max = 50)
	@Column(name = "TokenURL")
	public String tokenURL;

	@NotNull
	@Size(max = 50)
	@Column(name = "LastUser")
	public String lastuser = "CAS_JPA";

	@NotNull
	@Column(name = "LastDate")
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}