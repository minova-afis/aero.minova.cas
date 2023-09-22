package aero.minova.cas.service.model;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "xtcasAuthorities")
@AllArgsConstructor
@NoArgsConstructor
public class Authorities extends DataEntity {

	public Authorities(int keyLong, String username, String authority, String lastUser, Timestamp lastDate, int lastAction) {

		this.username = username;
		this.authority = authority;

		super.setKeyLong(keyLong);
		super.lastUser = lastUser;
		super.lastDate = lastDate;
		this.setLastAction(lastAction);
	}

	@NotNull
	@Size(max = 50)
	@Column(name = "Username", length = 50)
	public String username;

	@NotNull
	@Size(max = 200)
	@Column(name = "Authority", length = 200)
	public String authority;
}