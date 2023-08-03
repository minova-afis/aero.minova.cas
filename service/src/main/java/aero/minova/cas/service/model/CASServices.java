package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasCASServices")
public class CASServices extends DataEntity {

	@NotNull
	@Size(max = 250)
	@Column(name = "ServiceURL", length = 250)
	public String serviceUrl;

	@NotNull
	@Column(name = "Port")
	public int port;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ServiceMessageReceiverLoginTypeKey", nullable = true)
	public ServiceMessageReceiverLoginType receiverLoginType;

	@Size(max = 50)
	@Column(name = "Username", length = 50)
	public String username;

	@Size(max = 50)
	@Column(name = "Password", length = 50)
	public String password;

	@Size(max = 50)
	@Column(name = "ClientID", length = 50)
	public String clientId;

	@Size(max = 50)
	@Column(name = "ClientSecret", length = 50)
	public String clientSecret;

	@Size(max = 50)
	@Column(name = "TokenURL", length = 50)
	public String tokenURL;
}