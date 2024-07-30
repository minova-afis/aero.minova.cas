package aero.minova.cas.app.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.api.domain.XTable;
import aero.minova.cas.app.util.GsonUtil;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.app.util.TableDeserializer;
import aero.minova.cas.app.util.TableSerializer;
import aero.minova.cas.app.util.TableUtil;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.controller.SqlViewController;
import aero.minova.cas.service.AuthorizationService;
import aero.minova.cas.service.BaseService;
import aero.minova.cas.service.model.DataEntity;
import jakarta.annotation.PostConstruct;

public abstract class BaseExtension<E extends DataEntity> {

	@Autowired
	protected SqlProcedureController sqlProcedureController;

	@Autowired
	protected SqlViewController sqlViewController;

	@Autowired
	protected CustomLogger logger;

	@Autowired
	protected AuthorizationService authorizationService;

	protected String procedurePrefix = "xpcor";
	protected String viewPrefix = "xvcor";
	protected String tablePrefix = "";
	protected boolean filterLastAction = true;

	public static final Gson TABLE_CONVERSION_GSON = GsonUtil.getGsonBuilder() //
			.registerTypeAdapter(Table.class, new TableDeserializer()) //
			.registerTypeAdapter(Table.class, new TableSerializer()) //
			.registerTypeAdapter(XTable.class, new TableDeserializer()) //
			.registerTypeAdapter(XTable.class, new TableSerializer()) //
			.setDateFormat("yyyyMMddHHmmss")//
			.setPrettyPrinting() //
			.create();

	protected Class<E> entityClass;

	@Autowired
	protected BaseService<E> service;

	@SuppressWarnings("unchecked")
	public BaseExtension() {
		Class<?>[] resolveTypeArguments = GenericTypeResolver.resolveTypeArguments(getClass(), BaseExtension.class);
		if (resolveTypeArguments != null) {
			this.entityClass = (Class<E>) resolveTypeArguments[0];
		} else {
			throw new RuntimeException("No TypeArguments found");
		}
	}

	@PostConstruct
	public void basicSetup() {
		setup(entityClass.getSimpleName());
	}

	public void setup(String formName) {
		sqlProcedureController.registerExtension(procedurePrefix + "Insert" + formName, this::insert);
		sqlProcedureController.registerExtension(procedurePrefix + "Update" + formName, this::update);
		sqlProcedureController.registerExtension(procedurePrefix + "Delete" + formName, this::delete);
		sqlProcedureController.registerExtension(procedurePrefix + "Read" + formName, this::read);

		sqlViewController.registerExtension(viewPrefix + formName.toLowerCase() + "Index", this::readIndex);
		sqlViewController.registerExtension(viewPrefix + formName.toLowerCase(), this::readIndex);
		sqlViewController.registerExtension(tablePrefix + formName.toLowerCase(), this::readIndex);

		authorizationService.findOrCreateUserPrivilege(viewPrefix + formName.toLowerCase() + "Index");
		authorizationService.findOrCreateUserPrivilege(viewPrefix + formName.toLowerCase());
		authorizationService.findOrCreateUserPrivilege(tablePrefix + formName.toLowerCase());

		authorizationService.createDefaultPrivilegesForMask(formName, procedurePrefix, viewPrefix);
	}

	public Table readIndex(Table inputTable) {
		try {

			if (filterLastAction && inputTable.findColumnPosition("LastAction") == -1) {

				// LastAction > 0 an die erste Zeile und jede Zeile mit "echtem" Filter anhängen, damit gelöschte Einträge nicht angezeigt werden
				Table withFilter = new Table();
				withFilter.setName(inputTable.getName());

				for (Column c : inputTable.getColumns()) {
					withFilter.addColumn(c);
				}
				withFilter.addColumn(new Column("LastAction", DataType.INTEGER));

				boolean firstRow = true;
				for (Row r : inputTable.getRows()) {
					boolean allNull = true;
					Row newRow = new Row();
					for (int i = 0; i < r.getValues().size(); i++) {
						Value v = r.getValues().get(i);
						newRow.addValue(v);
						if (v != null && !inputTable.getColumns().get(i).getName().equals(Column.AND_FIELD_NAME)) {
							allNull = false;
						}
					}

					if (firstRow || !allNull) {
						newRow.addValue(new Value(0, ">"));
					} else {
						newRow.addValue(null);
					}

					withFilter.addRow(newRow);
					firstRow = false;
				}

				return sqlViewController.getIndexView(withFilter, false);
			} else {
				return sqlViewController.getIndexView(inputTable, false);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> insert(Table inputTable) {

		E entity = TABLE_CONVERSION_GSON.fromJson(TABLE_CONVERSION_GSON.toJsonTree(inputTable), entityClass);

		E response = service.save(entity);

		JsonElement json = TABLE_CONVERSION_GSON.toJsonTree(response);
		Table jsonTable = TABLE_CONVERSION_GSON.fromJson(json, Table.class);
		Table resultTable = TableUtil.addDataTypeToTable(jsonTable, inputTable);
		return ResponseEntityUtil.createResponseEntity(resultTable, false);
	}

	public ResponseEntity<SqlProcedureResult> update(Table inputTable) {

		E entity = TABLE_CONVERSION_GSON.fromJson(TABLE_CONVERSION_GSON.toJsonTree(inputTable), entityClass);
		service.save(entity);

		return ResponseEntityUtil.createResponseEntity(null, false);
	}

	public ResponseEntity<SqlProcedureResult> delete(Table inputTable) {
		service.deleteById(inputTable.getValue("KeyLong", 0).getIntegerValue());
		return ResponseEntityUtil.createResponseEntity(null, false);
	}

	public ResponseEntity<SqlProcedureResult> read(Table inputTable) {

		E findEntityById = service.findEntityById(inputTable.getValue("KeyLong", 0).getIntegerValue());

		JsonElement json = TABLE_CONVERSION_GSON.toJsonTree(findEntityById);
		Table jsonTable = TABLE_CONVERSION_GSON.fromJson(json, Table.class);
		Table resultTable = TableUtil.addDataTypeToTable(jsonTable, inputTable);
		logger.logger.info("Result: {}", resultTable);
		return ResponseEntityUtil.createResponseEntity(resultTable, false);
	}
}