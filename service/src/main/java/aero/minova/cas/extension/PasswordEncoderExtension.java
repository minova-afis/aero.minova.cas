package aero.minova.cas.extension;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;
import lombok.val;

@Service
public class PasswordEncoderExtension {
	@Autowired
	SqlProcedureController spc;
	@Autowired
	PasswordEncoder passwordEncoder;

	@PostConstruct
	private void setup() {
		spc.registerExtension("xpcasEncodePassword", input -> {
			final var passwordColumn = input.getColumns().stream()//
					.filter(c -> c.getName().equals("Password"))//
					.findFirst();
			final var encodedPasswordColumn = input.getColumns().stream()//
					.filter(c -> c.getName().equals("EncodedPassword"))//
					.findFirst();
			if (passwordColumn.isEmpty()) {
				throw new IllegalArgumentException("Column `Password` is missing.");
			}
			if (encodedPasswordColumn.isEmpty()) {
				throw new IllegalArgumentException("Column `EncodedPassword` is missing.");
			}
			if (!DataType.STRING.equals(passwordColumn.get().getType())) {
				throw new IllegalArgumentException("Column `Password` has to be of type String but is `" + passwordColumn.get().getType() + "`.");
			}
			if (!DataType.STRING.equals(encodedPasswordColumn.get().getType())) {
				throw new IllegalArgumentException("Column `EncodedPassword` has to be of type String but is `" + encodedPasswordColumn.get().getType() + "`.");
			}
			val passwordIndex = input.getColumns().indexOf(passwordColumn.get());
			val encodedPasswordIndex = input.getColumns().indexOf(encodedPasswordColumn.get());
			input.getRows().forEach(row -> {
				val passwordValue = row.getValues().get(passwordIndex);
				if (passwordValue == null) {
					throw new IllegalArgumentException("Column `Password` has no value.");
				}
				row.getValues().set(encodedPasswordIndex, new Value(passwordEncoder.encode(passwordValue.getStringValue()), null));
			});

			SqlProcedureResult result = new SqlProcedureResult();
			result.setResultSet(input);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		});
	}
}
