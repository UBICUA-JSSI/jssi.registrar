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
package jssi.registrar.local.extensions;

import uniregistrar.RegistrationException;
import jssi.registrar.local.LocalUniRegistrar;
import uniregistrar.request.DeactivateRequest;
import uniregistrar.request.RegisterRequest;
import uniregistrar.request.UpdateRequest;
import uniregistrar.state.DeactivateState;
import uniregistrar.state.RegisterState;
import uniregistrar.state.UpdateState;

public interface Extension {

	public ExtensionStatus beforeRegister(String driverId, RegisterRequest registerRequest, RegisterState registerState, LocalUniRegistrar localUniRegistrar) throws RegistrationException;
	public ExtensionStatus beforeUpdate(String driverId, UpdateRequest updateRequest, UpdateState updateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException;
	public ExtensionStatus beforeDeactivate(String driverId, DeactivateRequest deactivateRequest, DeactivateState deactivateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException;
	public ExtensionStatus afterRegister(String driverId, RegisterRequest registerRequest, RegisterState registerState, LocalUniRegistrar localUniRegistrar) throws RegistrationException;
	public ExtensionStatus afterUpdate(String driverId, UpdateRequest updateRequest, UpdateState updateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException;
	public ExtensionStatus afterDeactivate(String driverId, DeactivateRequest deactivateRequest, DeactivateState deactivateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException;

	public abstract static class AbstractExtension implements Extension {

		@Override
		public ExtensionStatus beforeRegister(String driverId, RegisterRequest registerRequest, RegisterState registerState, LocalUniRegistrar localUniRegistrar) throws RegistrationException {

			return null;
		}

		@Override
		public ExtensionStatus beforeUpdate(String driverId, UpdateRequest updateRequest, UpdateState updateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException {

			return null;
		}

		@Override
		public ExtensionStatus beforeDeactivate(String driverId, DeactivateRequest deactivateRequest, DeactivateState deactivateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException {

			return null;
		}

		@Override
		public ExtensionStatus afterRegister(String driverId, RegisterRequest registerRequest, RegisterState registerState, LocalUniRegistrar localUniRegistrar) throws RegistrationException {

			return null;
		}

		@Override
		public ExtensionStatus afterUpdate(String driverId, UpdateRequest updateRequest, UpdateState updateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException {

			return null;
		}

		@Override
		public ExtensionStatus afterDeactivate(String driverId, DeactivateRequest deactivateRequest, DeactivateState deactivateState, LocalUniRegistrar localUniRegistrar) throws RegistrationException {

			return null;
		}
	}
}
