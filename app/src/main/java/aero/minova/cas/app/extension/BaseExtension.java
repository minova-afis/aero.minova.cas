package aero.minova.cas.app.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.XTable;
import aero.minova.cas.app.util.GsonUtil;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.app.util.TableDeserializer;
import aero.minova.cas.app.util.TableSerializer;
import aero.minova.cas.app.util.TableUtil;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.service.AuthorizationService;
import aero.minova.cas.service.BaseService;
import aero.minova.cas.service.model.DataEntity;
import jakarta.annotation.PostConstruct;

public abstract class BaseExtension<E extends DataEntity> {

	@Autowired
	protected SqlProcedureController sqlProcedureController;

	@Autowired
	protected CustomLogger logger;

	@Autowired
	protected AuthorizationService authorizationService;

	protected String procedurePrefix = "xpcor";
	protected String viewPrefix = "xvcor";

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

		authorizationService.createDefaultPrivilegesForMask(formName, procedurePrefix, viewPrefix);
		authorizationService.findOrCreateUserPrivilege("v" + formName.toLowerCase() + "Index");
		authorizationService.findOrCreateUserPrivilege("v" + formName.toLowerCase());
		authorizationService.findOrCreateUserPrivilege("t" + formName.toLowerCase());
		authorizationService.findOrCreateUserPrivilege("xtcas" + formName.toLowerCase());
		authorizationService.findOrCreateUserPrivilege(formName.toLowerCase());
	}

	public ResponseEntity<SqlProcedureResult> insert(Table inputTable) {
		try {
			E entity = TABLE_CONVERSION_GSON.fromJson(TABLE_CONVERSION_GSON.toJsonTree(inputTable), entityClass);

			E response = service.save(entity);

			JsonElement json = TABLE_CONVERSION_GSON.toJsonTree(response);
			Table jsonTable = TABLE_CONVERSION_GSON.fromJson(json, Table.class);
			Table resultTable = TableUtil.addDataTypeToTable(jsonTable, inputTable);
			return ResponseEntityUtil.createResponseEntity(resultTable, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> update(Table inputTable) {
		try {
			E entity = TABLE_CONVERSION_GSON.fromJson(TABLE_CONVERSION_GSON.toJsonTree(inputTable), entityClass);
			service.save(entity);
			return ResponseEntityUtil.createResponseEntity(null, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> delete(Table inputTable) {
		try {

			service.deleteById(inputTable.getValue("KeyLong", 0).getIntegerValue());

			return ResponseEntityUtil.createResponseEntity(null, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> read(Table inputTable) {
		try {

			E findEntityById = service.findEntityById(inputTable.getValue("KeyLong", 0).getIntegerValue());

			JsonElement json = TABLE_CONVERSION_GSON.toJsonTree(findEntityById);
			Table jsonTable = TABLE_CONVERSION_GSON.fromJson(json, Table.class);
			Table resultTable = TableUtil.addDataTypeToTable(jsonTable, inputTable);
			logger.logger.info("Result: {}", resultTable);
			return ResponseEntityUtil.createResponseEntity(resultTable, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	// TODO: Do anything fancy or remove
	public static RuntimeException handleError(Exception e) {

		return new RuntimeException(e);
	}

}