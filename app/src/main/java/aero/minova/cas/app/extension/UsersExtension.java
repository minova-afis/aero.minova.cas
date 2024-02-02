package aero.minova.cas.app.extension;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;

import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.app.util.TableUtil;
import aero.minova.cas.service.model.Users;
import jakarta.annotation.PostConstruct;

@Component
public class UsersExtension extends BaseExtension<Users> {

	@PostConstruct
	void setPrefix() {
		viewPrefix = "xvcas";
		procedurePrefix = "xpcas";
		super.basicSetup();
	}

	@Override
	public ResponseEntity<SqlProcedureResult> read(Table inputTable) {
		try {

			Users findEntityById = service.findEntityById(inputTable.getValue("KeyLong", 0).getIntegerValue());

			// Passwort soll nicht ausgelesen werden
			findEntityById.setPassword(null);

			JsonElement json = TABLE_CONVERSION_GSON.toJsonTree(findEntityById);
			Table jsonTable = TABLE_CONVERSION_GSON.fromJson(json, Table.class);
			Table resultTable = TableUtil.addDataTypeToTable(jsonTable, inputTable);
			logger.logger.info("Result: {}", resultTable);
			return ResponseEntityUtil.createResponseEntity(resultTable, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}
}