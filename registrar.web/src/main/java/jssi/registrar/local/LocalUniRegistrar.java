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
package jssi.registrar.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import uniregistrar.RegistrationException;
import uniregistrar.UniRegistrar;
import uniregistrar.driver.Driver;
import jssi.registrar.local.extensions.Extension;
import jssi.registrar.local.extensions.ExtensionStatus;
import uniregistrar.request.DeactivateRequest;
import uniregistrar.request.RegisterRequest;
import uniregistrar.request.UpdateRequest;
import uniregistrar.state.DeactivateState;
import uniregistrar.state.RegisterState;
import uniregistrar.state.UpdateState;

public class LocalUniRegistrar implements UniRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(LocalUniRegistrar.class);

    private List<Extension> extensions = new ArrayList<Extension>();
    private final Drivers drivers;

    public LocalUniRegistrar(Drivers drivers) {
        this.drivers = drivers;
    }

    @Override
    public RegisterState register(String driverId, RegisterRequest registerRequest) throws RegistrationException {

        if (driverId == null) {
            throw new NullPointerException();
        }
        if (registerRequest == null) {
            throw new NullPointerException();
        }

        if (drivers.getDrivers().isEmpty()) {
            throw new RegistrationException("No drivers configured.");
        }

        // start time
        long start = System.currentTimeMillis();

        // prepare register state
        RegisterState registerState = RegisterState.build();
        ExtensionStatus extensionStatus = new ExtensionStatus();

        // execute extensions (before)
        if (!extensionStatus.skipExtensionsBefore()) {

            for (Extension extension : this.getExtensions()) {

                extensionStatus.or(extension.beforeRegister(driverId, registerRequest, registerState, this));
                if (extensionStatus.skipExtensionsBefore()) {
                    break;
                }
            }
        }

        // select and execute driver
        if (!extensionStatus.skipDriver()) {

            Driver driver = drivers.getDrivers().get(driverId);
            if (driver == null) {
                throw new RegistrationException("Unknown driver: " + driverId);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Attemping to register " + registerRequest + " with driver " + driver.getClass());
            }

            RegisterState driverRegisterState = driver.register(registerRequest);

            if (driverRegisterState != null) {
                registerState.setJobId(driverRegisterState.getJobId());
                registerState.setDidState(driverRegisterState.getDidState());
                registerState.setMethodMetadata(driverRegisterState.getMethodMetadata());
            }

            registerState.getRegistrarMetadata().put("driverId", driverId);
        }

        // execute extensions (after)
        if (!extensionStatus.skipExtensionsAfter()) {

            for (Extension extension : this.getExtensions()) {

                extensionStatus.or(extension.afterRegister(driverId, registerRequest, registerState, this));
                if (extensionStatus.skipExtensionsAfter()) {
                    break;
                }
            }
        }

        // stop time
        long stop = System.currentTimeMillis();
        registerState.getRegistrarMetadata().put("duration", stop - start);
        // done
        return registerState;
    }

    @Override
    public UpdateState update(String driverId, UpdateRequest updateRequest) throws RegistrationException {

        if (driverId == null) {
            throw new NullPointerException();
        }
        if (updateRequest == null) {
            throw new NullPointerException();
        }

        if (drivers.getDrivers().isEmpty()) {
            throw new RegistrationException("No drivers configured.");
        }

        // start time
        long start = System.currentTimeMillis();

        // prepare update state
        UpdateState updateState = UpdateState.build();
        ExtensionStatus extensionStatus = new ExtensionStatus();

        // execute extensions (before)
        if (!extensionStatus.skipExtensionsBefore()) {

            for (Extension extension : this.getExtensions()) {
                extensionStatus.or(extension.beforeUpdate(driverId, updateRequest, updateState, this));
                if (extensionStatus.skipExtensionsBefore()) {
                    break;
                }
            }
        }

        // select and execute driver
        if (!extensionStatus.skipDriver()) {

            Driver driver = drivers.getDrivers().get(driverId);
            if (driver == null) {
                throw new RegistrationException("Unknown driver: " + driverId);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Attemping to update " + updateRequest + " with driver " + driver.getClass());
            }

            UpdateState driverUpdateState = driver.update(updateRequest);
            updateState.setJobId(driverUpdateState.getJobId());
            updateState.setDidState(driverUpdateState.getDidState());
            updateState.setMethodMetadata(driverUpdateState.getMethodMetadata());

            updateState.getRegistrarMetadata().put("driverId", driverId);
        }

        // execute extensions (after)
        if (!extensionStatus.skipExtensionsAfter()) {

            for (Extension extension : this.getExtensions()) {

                extensionStatus.or(extension.afterUpdate(driverId, updateRequest, updateState, this));
                if (extensionStatus.skipExtensionsAfter()) {
                    break;
                }
            }
        }

        // stop time
        long stop = System.currentTimeMillis();

        updateState.getRegistrarMetadata().put("duration", stop - start);

        // done
        return updateState;
    }

    @Override
    public DeactivateState deactivate(String driverId, DeactivateRequest deactivateRequest) throws RegistrationException {

        if (driverId == null) {
            throw new NullPointerException();
        }
        if (deactivateRequest == null) {
            throw new NullPointerException();
        }

        if (drivers.getDrivers().isEmpty()) {
            throw new RegistrationException("No drivers configured.");
        }

        // start time
        long start = System.currentTimeMillis();

        // prepare deactivate state
        DeactivateState deactivateState = DeactivateState.build();
        ExtensionStatus extensionStatus = new ExtensionStatus();

        // execute extensions (before)
        if (!extensionStatus.skipExtensionsBefore()) {

            for (Extension extension : this.getExtensions()) {

                extensionStatus.or(extension.beforeDeactivate(driverId, deactivateRequest, deactivateState, this));
                if (extensionStatus.skipExtensionsBefore()) {
                    break;
                }
            }
        }

        // select and execute driver
        if (!extensionStatus.skipDriver()) {

            Driver driver = drivers.getDrivers().get(driverId);
            if (driver == null) {
                throw new RegistrationException("Unknown driver: " + driverId);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Attemping to deactivate " + deactivateRequest + " with driver " + driver.getClass());
            }

            DeactivateState driverDeactivateState = driver.deactivate(deactivateRequest);
            deactivateState.setJobId(driverDeactivateState.getJobId());
            deactivateState.setDidState(driverDeactivateState.getDidState());
            deactivateState.setMethodMetadata(driverDeactivateState.getMethodMetadata());

            deactivateState.getRegistrarMetadata().put("driverId", driverId);
        }

        // execute extensions (after)
        if (!extensionStatus.skipExtensionsAfter()) {

            for (Extension extension : this.getExtensions()) {
                extensionStatus.or(extension.afterDeactivate(driverId, deactivateRequest, deactivateState, this));
                if (extensionStatus.skipExtensionsAfter()) {
                    break;
                }
            }
        }

        // stop time
        long stop = System.currentTimeMillis();
        deactivateState.getRegistrarMetadata().put("duration", stop - start);
        // done
        return deactivateState;
    }

    @Override
    public Map<String, Map<String, Object>> properties() throws RegistrationException {

        if (drivers.getDrivers().isEmpty()) {
            throw new RegistrationException("No drivers configured.");
        }

        Map<String, Map<String, Object>> properties = new HashMap<>();

        for (Entry<String, Driver> driver : drivers.getDrivers().entrySet()) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Loading properties for driver " + driver.getKey() + " (" + driver.getValue().getClass().getSimpleName() + ")");
            }

            Map<String, Object> driverProperties = driver.getValue().properties();
            if (driverProperties == null) {
                driverProperties = Collections.emptyMap();
            }

            properties.put(driver.getKey(), driverProperties);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading properties: " + properties);
        }

        return properties;
    }

   

    @SuppressWarnings("unchecked")
    public <T extends Driver> T getDriver(Class<T> driverClass) {

        for (Driver driver : drivers.getDrivers().values()) {
            if (driverClass.isAssignableFrom(driver.getClass())) {
                return (T) driver;
            }
        }

        return null;
    }

    public List<Extension> getExtensions() {
        return this.extensions;
    }

    public void setExtensions(List<Extension> extensions) {
        this.extensions = extensions;
    }
}
