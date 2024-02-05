package aero.minova.cas.app.extension;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.service.LuUserPrivilegeUserGroupService;
import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;
import jakarta.annotation.PostConstruct;

@Component
public class LuUserPrivilegeUserGroupExtension extends BaseGridExtension<LuUserPrivilegeUserGroup> {

	@PostConstruct
	void setPrefix() {
		viewPrefix = "xvcas";
		procedurePrefix = "xpcas";
		tablePrefix = "xtcas";
		super.basicSetup();
	}

	@Override
	public ResponseEntity<SqlProcedureResult> read(Table inputTable) {

		List<LuUserPrivilegeUserGroup> lus;

		if (inputTable.getValue("UserPrivilege.KeyLong", 0) != null) {
			lus = ((LuUserPrivilegeUserGroupService) service).findByUserPrivilegeKey(inputTable.getValue("UserPrivilege.KeyLong", 0).getIntegerValue());
		} else {
			lus = ((LuUserPrivilegeUserGroupService) service).findByUserGroupKey(inputTable.getValue("UserGroup.KeyLong", 0).getIntegerValue());
		}

		return ResponseEntityUtil.createResponseEntity(getResponseTable(lus, inputTable.getName()), true);

	}

	private Table getResponseTable(List<LuUserPrivilegeUserGroup> lus, String tableName) {

		Table res = new Table();
		res.setName(tableName);
		res.addColumn(new Column("KeyLong", DataType.INTEGER));
		res.addColumn(new Column("UserPrivilege.KeyLong", DataType.INTEGER));
		res.addColumn(new Column("UserGroup.KeyLong", DataType.INTEGER));
		res.addColumn(new Column("RowLevelSecurity", DataType.BOOLEAN));

		for (LuUserPrivilegeUserGroup lu : lus) {
			Row r = new Row();
			r.addValue(new Value(lu.getKeyLong()));
			r.addValue(new Value(lu.getUserPrivilege().getKeyLong()));
			r.addValue(new Value(lu.getUserGroup().getKeyLong()));
			r.addValue(new Value(lu.isRowLevelSecurity()));

			res.addRow(r);
		}

		return res;
	}

}