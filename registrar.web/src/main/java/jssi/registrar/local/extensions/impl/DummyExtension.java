/*
 *
 *  * Copyright 2021 UBICUA.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
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
