package jssi.registrar.local.extensions.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uniregistrar.RegistrationException;
import jssi.registrar.local.LocalUniRegistrar;
import jssi.registrar.local.extensions.Extension;
import jssi.registrar.local.extensions.ExtensionStatus;
import jssi.registrar.local.extensions.Extension.AbstractExtension;
import uniregistrar.request.RegisterRequest;
import uniregistrar.state.RegisterState;

public class DummyExtension extends AbstractExtension implements Extension {

	private static Logger log = LoggerFactory.getLogger(DummyExtension.class);

	public ExtensionStatus afterRegister(String driverId, RegisterRequest registerRequest, RegisterState registerState, LocalUniRegistrar localUniRegistrar) throws RegistrationException {

		if (log.isDebugEnabled()) log.debug("Dummy extension called!");

		return ExtensionStatus.DEFAULT;
	}
}
