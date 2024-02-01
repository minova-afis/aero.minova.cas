package aero.minova.cas.app.extension;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.service.AuthoritiesService;
import aero.minova.cas.service.UserGroupService;
import aero.minova.cas.service.model.Authorities;
import aero.minova.cas.service.model.UserGroup;
import jakarta.annotation.PostConstruct;

@Component
public class AuthoritiesExtension extends BaseGridExtension<Authorities> {

	@Autowired
	UserGroupService userGroupService;

	@PostConstruct
	void setPrefix() {
		viewPrefix = "xvcas";
		procedurePrefix = "xpcas";
		super.basicSetup();
	}

	@Override
	protected ResponseEntity<SqlProcedureResult> insertOrUpdate(Table inputTable) {
		try {

			List<Authorities> l = getEntitiesList(inputTable);

			for (Authorities entity : l) {
				entity = fillAuthority(entity);

				service.save(entity);
			}

			return ResponseEntityUtil.createResponseEntity(null, true);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	@Override
	public ResponseEntity<SqlProcedureResult> read(Table inputTable) {
		try {
			List<Authorities> findEntityById = ((AuthoritiesService) service).findByUsername(inputTable.getValue("Username", 0).getStringValue());
			return ResponseEntityUtil.createResponseEntity(getResponseTable(findEntityById, inputTable.getName()), true);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	private Table getResponseTable(List<Authorities> authorities, String tableName) {

		Table res = new Table();
		res.setName(tableName);
		res.addColumn(new Column("KeyLong", DataType.INTEGER));
		res.addColumn(new Column("Username", DataType.STRING));
		res.addColumn(new Column("Authority", DataType.INTEGER));

		for (Authorities a : authorities) {
			Row r = new Row();
			r.addValue(new Value(a.getKeyLong()));
			r.addValue(new Value(a.getUsername()));
			r.addValue(new Value(userGroupService.findEntitiesByKeyText(a.getAuthority()).get(0).getKeyLong()));
			res.addRow(r);
		}

		return res;
	}

	private Authorities fillAuthority(Authorities entity) {

		// In der Maske ist Authorities.op.xml ist das Feld `Authority` als Lookup definiert -> entity.getAuthority ist der KeyLong der Authority
		UserGroup userGroup = userGroupService.findEntityById(Integer.valueOf(entity.getAuthority()));
		entity.setAuthority(userGroup.getKeyText());

		return entity;
	}

}
