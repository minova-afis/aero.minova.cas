package aero.minova.cas.app.helper;

import javax.inject.Inject;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.component.annotations.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import aero.minova.rcp.dataservice.IDataService;
import aero.minova.rcp.model.Value;
import aero.minova.rcp.model.form.MDetail;
import aero.minova.rcp.model.form.MField;
import aero.minova.rcp.model.helper.ActionCode;
import aero.minova.rcp.model.helper.IHelper;
import aero.minova.rcp.rcp.util.WFCDetailCASRequestsUtil;

@Component
public class CASUsersHelper implements IHelper {

	@Inject
	IDataService dataService;

	ILog logger = Platform.getLog(this.getClass());

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
		String encoded = new BCryptPasswordEncoder().encode(stringValue.getStringValue());
		password.setValue(new Value(encoded), false);
	}

}
