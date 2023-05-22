package aero.minova.cas.app.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.osgi.service.component.annotations.Component;

import aero.minova.rcp.constants.Constants;
import aero.minova.rcp.dataservice.IDataService;
import aero.minova.rcp.model.DataType;
import aero.minova.rcp.model.OutputType;
import aero.minova.rcp.model.Row;
import aero.minova.rcp.model.SqlProcedureResult;
import aero.minova.rcp.model.Table;
import aero.minova.rcp.model.TransactionEntry;
import aero.minova.rcp.model.TransactionResultEntry;
import aero.minova.rcp.model.Value;
import aero.minova.rcp.model.builder.RowBuilder;
import aero.minova.rcp.model.builder.TableBuilder;
import aero.minova.rcp.model.form.MDetail;
import aero.minova.rcp.model.form.MField;
import aero.minova.rcp.model.helper.ActionCode;
import aero.minova.rcp.model.helper.IHelper;
import aero.minova.rcp.rcp.util.WFCDetailCASRequestsUtil;

@Component
public class CASUsersHelper implements IHelper {

	@Inject
	MPerspective mPerspective;

	@Inject
	IDataService dataService;

	@Inject
	Logger logger;

	@Inject
	IEventBroker eventBroker;

	@Inject
	@Optional
	WFCDetailCASRequestsUtil detailUtil;

	private MField password;

	@Override
	public void setControls(MDetail mDetail) {
		password = mDetail.getField("Password");
	}

	@Override
	public void handleDetailAction(ActionCode code) {
		switch (code) {
		case BEFORESAVE:
			if (password.getValue() != null) {
				encryptPassword(password.getValue());
			}
			break;
		case BEFOREREAD, AFTERREAD:
			password.setRequired(false);
			break;
		default:
			password.setRequired(true);
			break;
		}

	}

	private void encryptPassword(Value stringValue) {
		Table requestTable = TableBuilder.newTable("xpcasEncodePassword") //
				.withColumn("Password", DataType.STRING, OutputType.INPUT)//
				.withColumn("EncodedPassword", DataType.STRING, OutputType.OUTPUT)//
				.create();
		Row row = RowBuilder.newRow() //
				.withValue(stringValue) //
				.withValue(null)//
				.create();
		requestTable.addRow(row);

		List<TransactionEntry> procedureList = new ArrayList<>();
		procedureList.add(new TransactionEntry(Constants.TRANSACTION_PARENT, requestTable));
		CompletableFuture<List<TransactionResultEntry>> callTransactionAsync = dataService.callTransactionAsync(procedureList);

		try {
			SqlProcedureResult results = callTransactionAsync.get().get(0).getSQLProcedureResult(); // Warten bis Aufruf fertig ist
			if (results != null && results.getResultSet() != null) {
				Row resultSet = results.getResultSet().getRows().get(0);
				if (resultSet.getValue(1) != null) {
					password.setValue(new Value(resultSet.getValue(1).getStringValue()), false);
					return;
				}
			}

			// Falls das Passwort nicht verschlüsselt werden konnte, Fehler melden.
			eventBroker.send(Constants.BROKER_SHOWERRORMESSAGE,
					"Passwort konnte nicht verschlüsselt werden. Bitte versuchen Sie es erneut oder mit einem anderen Passwort.");

		} catch (Exception e) {
			logger.error(e);
			eventBroker.send(Constants.BROKER_SHOWERRORMESSAGE, e.getMessage()); // Fehlermeldung
		}
	}

}
