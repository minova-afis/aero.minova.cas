package aero.minova.cas.app.extension;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.service.AuthorizationService;
import aero.minova.cas.service.UserService;
import aero.minova.cas.service.model.User;
import jakarta.annotation.PostConstruct;

@Component
public class UserGroupUserExtension {

	@Autowired
	protected SqlProcedureController sqlProcedureController;

	@Autowired
	protected CustomLogger logger;

	@Autowired
	protected AuthorizationService authorizationService;

	@Autowired
	protected UserService userService;

	@PostConstruct
	public void setup() {
		String formName = "UserGroupUser";
		sqlProcedureController.registerExtension("xpcasInsert" + formName, this::insert);
		sqlProcedureController.registerExtension("xpcasUpdate" + formName, this::update);
		sqlProcedureController.registerExtension("xpcasDelete" + formName, this::delete);
		sqlProcedureController.registerExtension("xpcasRead" + formName, this::read);
		authorizationService.createDefaultPrivilegesForMask(formName, "xpcas", "xvcas");

		authorizationService.findOrCreateUserPrivilege("xvcasUserIndex2");
	}

	public ResponseEntity<SqlProcedureResult> insert(Table inputTable) {
		try {
			for (Row r : inputTable.getRows()) {
				userService.addUserToUserGroup(//
						inputTable.getValue("UserKey", r).getIntegerValue(), //
						inputTable.getValue("KeyLong", r).getIntegerValue());
			}

			return ResponseEntityUtil.createResponseEntity(null, true);
		} catch (Exception e) {
			throw BaseExtension.handleError(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> update(Table inputTable) {
		try {
			// Do Nothing, throw Exception??
			return ResponseEntityUtil.createResponseEntity(null, true);
		} catch (Exception e) {
			throw BaseExtension.handleError(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> delete(Table inputTable) {
		try {
			for (Row r : inputTable.getRows()) {
				userService.removeUserFromUserGroup(//
						inputTable.getValue("UserKey", r).getIntegerValue(), //
						inputTable.getValue("KeyLong", r).getIntegerValue());
			}
			return ResponseEntityUtil.createResponseEntity(null, true);
		} catch (Exception e) {
			throw BaseExtension.handleError(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> read(Table inputTable) {
		try {

			int userGroupKey = inputTable.getValue("KeyLong", 0).getIntegerValue();
			List<User> users = userService.findUsersInUserGroup(userGroupKey);

			Table res = new Table();
			res.setName(inputTable.getName());
			res.addColumn(new Column("KeyLong", DataType.INTEGER));
			res.addColumn(new Column("UserKey", DataType.INTEGER));

			for (User u : users) {
				Row r = new Row();
				r.addValue(new Value(userGroupKey));
				r.addValue(new Value(u.getKeyLong()));
				res.addRow(r);
			}

			return ResponseEntityUtil.createResponseEntity(res, true);

		} catch (Exception e) {
			throw BaseExtension.handleError(e);
		}
	}

}
