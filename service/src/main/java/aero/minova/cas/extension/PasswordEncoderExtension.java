package aero.minova.cas.extension;

import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
                    .filter(c -> c.getName().equals("password"))//
                    .findFirst();
            final var encodedPasswordColumn = input.getColumns().stream()//
                    .filter(c -> c.getName().equals("encodedPassword"))//
                    .findFirst();
            if (passwordColumn.isEmpty()) {
                throw new IllegalArgumentException("Column `password` is missing.");
            }
            if (encodedPasswordColumn.isEmpty()) {
                throw new IllegalArgumentException("Column `encodedPassword` is missing.");
            }
            if (!DataType.STRING.equals(passwordColumn.get().getType())) {
                throw new IllegalArgumentException("Column `password` has to be of type String but is `" + passwordColumn.get().getType() + "`.");
            }
            if (!DataType.STRING.equals(encodedPasswordColumn.get().getType())) {
                throw new IllegalArgumentException("Column `encodedPassword` has to be of type String but is `" + encodedPasswordColumn.get().getType() + "`.");
            }
            val passwordIndex = input.getColumns().indexOf(passwordColumn.get());
            val encodedPasswordIndex = input.getColumns().indexOf(encodedPasswordColumn.get());
            input.getRows().forEach(row -> {
                row.getValues().set(encodedPasswordIndex, new Value(passwordEncoder.encode(row.getValues().get(passwordIndex).getStringValue()), null));
            });
            return new ResponseEntity<Table>(input, HttpStatus.ACCEPTED);
        });
    }
}
