package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "xtcasAuthorities")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Authorities extends DataEntity {

	public Authorities(int keyLong, String username, String authority, String lastUser, LocalDateTime lastDate, int lastAction) {

		this.username = username;
		this.authority = authority;

		this.setKeyLong(keyLong);
		this.setLastUser(lastUser);
		this.setLastDate(lastDate);
		this.setLastAction(lastAction);
	}

	@NotNull
	@Size(max = 50)
	@Column(name = "Username", length = 50)
	private String username;

	@NotNull
	@Size(max = 200)
	@Column(name = "Authority", length = 200)
	private String authority;
}