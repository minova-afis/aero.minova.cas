package aero.minova.cas.app.extension;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.service.model.DataEntity;

public abstract class BaseGridExtension<E extends DataEntity> extends BaseExtension<E> {

	@Override
	public ResponseEntity<SqlProcedureResult> insert(Table inputTable) {
		return insertOrUpdate(inputTable);
	}

	@Override
	public ResponseEntity<SqlProcedureResult> update(Table inputTable) {
		return insertOrUpdate(inputTable);
	}

	protected ResponseEntity<SqlProcedureResult> insertOrUpdate(Table inputTable) {

		List<E> l = getEntitiesList(inputTable);

		for (E entity : l) {
			service.save(entity);
		}

		return ResponseEntityUtil.createResponseEntity(null, true);

	}

	@Override
	public ResponseEntity<SqlProcedureResult> delete(Table inputTable) {

		for (Row r : inputTable.getRows()) {
			service.deleteById(inputTable.getValue("KeyLong", r).getIntegerValue());
		}

		return ResponseEntityUtil.createResponseEntity(null, false);

	}

	protected List<E> getEntitiesList(Table inputTable) {
		List<E> l = new ArrayList<>();
		if (inputTable.getRows().size() > 1) {
			@SuppressWarnings("unchecked")
			E[] fromJson = (E[]) TABLE_CONVERSION_GSON.fromJson(TABLE_CONVERSION_GSON.toJsonTree(inputTable), entityClass.arrayType());
			l = List.of(fromJson);
		} else {
			E entity = TABLE_CONVERSION_GSON.fromJson(TABLE_CONVERSION_GSON.toJsonTree(inputTable), entityClass);
			l.add(entity);
		}
		return l;
	}

	@Override
	public abstract ResponseEntity<SqlProcedureResult> read(Table inputTable);

}