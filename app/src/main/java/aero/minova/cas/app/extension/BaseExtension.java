package aero.minova.cas.app.extension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.api.domain.ValueDeserializer;
import aero.minova.cas.api.domain.ValueSerializer;
import aero.minova.cas.api.domain.XTable;
import aero.minova.cas.app.util.GsonUtil;
import aero.minova.cas.app.util.ResponseEntityUtil;
import aero.minova.cas.app.util.TableDeserializer;
import aero.minova.cas.app.util.TableSerializer;
import aero.minova.cas.app.util.TableUtil;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.controller.SqlViewController;
import aero.minova.cas.service.AuthorizationService;
import aero.minova.cas.service.model.DataEntity;
import aero.minova.cas.service.repository.DataEntityRepository;
import jakarta.persistence.EntityNotFoundException;

public class BaseExtension<E extends DataEntity> {

	@Autowired
	protected SqlViewController sqlViewController;

	@Autowired
	protected SqlProcedureController sqlProcedureController;

	@Autowired
	protected CustomLogger logger;

	@Autowired
	protected AuthorizationService authorizationService;

	public static final Gson TABLE_CONVERSION_GSON = GsonUtil.getGsonBuilder() //
			.registerTypeAdapter(Table.class, new TableDeserializer()) //
			.registerTypeAdapter(Table.class, new TableSerializer()) //
			.registerTypeAdapter(XTable.class, new TableDeserializer()) //
			.registerTypeAdapter(XTable.class, new TableSerializer()) //
			.setDateFormat("yyyyMMddHHmmss")//
			.setPrettyPrinting() //
			.create();

	public static final Gson GSON = GsonUtil.getGsonBuilder() //
			.registerTypeAdapter(Value.class, new ValueDeserializer()) //
			.registerTypeAdapter(Value.class, new ValueSerializer()) //
			.setDateFormat("yyyyMMddHHmmss")//
			.create();

	@Autowired
	protected RestTemplate restTemplate;

	private Class<E> entityClass;

	@Autowired
	protected DataEntityRepository<E> repository;

	protected boolean allowDuplicateMatchcodes = false;

	public void setup(Class<E> entityClass) {
		setup(entityClass, entityClass.getSimpleName());
	}

	public void setup(Class<E> entityClass, String formName) {
		this.entityClass = entityClass;

		sqlProcedureController.registerExtension("xpcasInsert" + formName, this::insert);
		sqlProcedureController.registerExtension("xpcasUpdate" + formName, this::update);
		sqlProcedureController.registerExtension("xpcasDelete" + formName, this::delete);
		sqlProcedureController.registerExtension("xpcasRead" + formName, this::read);
		authorizationService.createDefaultPrivilegesForMask(formName, "xpcas", "xvcas");

		authorizationService.findOrCreateUserPrivilege("v" + formName.toLowerCase() + "Index");
		authorizationService.findOrCreateUserPrivilege("v" + formName.toLowerCase());
		authorizationService.findOrCreateUserPrivilege("xtcas" + formName.toLowerCase());
		authorizationService.findOrCreateUserPrivilege(formName.toLowerCase());
	}

	public ResponseEntity<SqlProcedureResult> insert(Table inputTable) {
		try {
			E entity = TABLE_CONVERSION_GSON.fromJson(TABLE_CONVERSION_GSON.toJsonTree(inputTable), entityClass);

			E response = save(entity);

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
			entity.setLastAction(2);
			save(entity);
			return ResponseEntityUtil.createResponseEntity(null, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	public E save(E entity) {

		if (!allowDuplicateMatchcodes) {// Gibt es diesen KeyText schon (nicht gelöscht)?
			Optional<E> findByKeyText = repository.findByKeyText(entity.getKeyText());
			if (findByKeyText.isPresent() && !Objects.equals(findByKeyText.get().getKeyLong(), entity.getKeyLong())) {
				throw new RuntimeException("@msg.DuplicateMatchcodeNotAllowed");
			}
		}

		entity.setLastDate(Timestamp.from(Instant.now()));
		entity.setLastUser(getCurrentUser());
		entity = repository.save(entity);

		return entity;
	}

	public ResponseEntity<SqlProcedureResult> delete(Table inputTable) {
		try {

			Optional<E> optional = repository.findById(inputTable.getValue("KeyLong", 0).getIntegerValue());
			if (optional.isEmpty()) {
				throw new EntityNotFoundException("@msg.EntityNotFound");
			}

			E entity = optional.get();

			entity.setLastAction(-1);
			entity.setLastDate(Timestamp.from(Instant.now()));
			entity.setLastUser(getCurrentUser());
			repository.save(entity);

			return ResponseEntityUtil.createResponseEntity(null, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	public ResponseEntity<SqlProcedureResult> read(Table inputTable) {
		try {

			Optional<E> optional = repository.findById(inputTable.getValue("KeyLong", 0).getIntegerValue());
			if (optional.isEmpty()) {
				throw new EntityNotFoundException("@msg.EntityNotFound");
			}

			JsonElement json = TABLE_CONVERSION_GSON.toJsonTree(optional.get());
			Table jsonTable = TABLE_CONVERSION_GSON.fromJson(json, Table.class);
			Table resultTable = TableUtil.addDataTypeToTable(jsonTable, inputTable);
			logger.logger.info("Result: {}", resultTable);
			return ResponseEntityUtil.createResponseEntity(resultTable, false);
		} catch (Exception e) {
			throw handleError(e);
		}
	}

	// TODO: Do anything fancy or remove
	public RuntimeException handleError(Exception e) {

		return new RuntimeException(e);
	}

	public String getCurrentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}