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
package jssi.registrar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jssi.registrar.local.Drivers;
import jssi.registrar.local.LocalUniRegistrar;
import uniregistrar.RegistrationException;
import uniregistrar.UniRegistrar;
import uniregistrar.request.DeactivateRequest;
import uniregistrar.request.RegisterRequest;
import uniregistrar.request.UpdateRequest;
import uniregistrar.state.DeactivateState;
import uniregistrar.state.RegisterState;
import uniregistrar.state.UpdateState;

/**
 * REST Web Service
 *
 * @author UBICUA
 */
@Path("/")
@RequestScoped
public class Registrar implements UniRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(Registrar.class);

    @Context
    private UriInfo context;

    @Inject
    private Drivers drivers;

    /**
     * Creates a new instance of Registrar
     */
    public Registrar() {
    }

    /**
     * POST method for updating or creating an instance of Registrar
     *
     * @param driverId
     * @param content representation for the resource
     * @return Response
     */
    @POST
    @Path("register/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@DefaultValue("sov") @QueryParam("driverId") String driverId, String content) {
        LOG.debug(String.format("Processing driverId %s", driverId));

        RegisterRequest registerRequest;

        try {
            registerRequest = RegisterRequest.fromJson(content);
        } catch (IOException ex) {
            if (LOG.isErrorEnabled()) {
                LOG.error(String.format("Request problem: %s", ex.getMessage()), ex);
            }

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getLocalizedMessage())
                    .build();
        }
        
        RegisterState registerState;
	String registerStateString;

        try {
            registerState = drivers.getDrivers().get(driverId).register(registerRequest);
            registerStateString = registerState == null ? null : registerState.toJson();
        } catch (JsonProcessingException | RegistrationException ex) {

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Driver reported for %s: %s", registerRequest, ex.getMessage()))
                    .build();
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Register state for " + registerRequest + ": " + registerStateString);
        }

        // no register state?
        if (registerState == null) {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(String.format("No register state for %s: ", registerRequest))
                    .build();
        }

        // write register state
        return Response
                .status(Response.Status.OK)
                .entity(registerStateString)
                .build();
    }

    @Override
    public RegisterState register(String driverId, RegisterRequest registerRequest) throws RegistrationException {
        UniRegistrar registrar = new LocalUniRegistrar(drivers);
        return registrar.register(driverId, registerRequest);
    }
    
    /**
     * POST method for updating or creating an instance of Registrar
     *
     * @param driverId
     * @param content representation for the resource
     * @return Response
     */
    @POST
    @Path("update/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@DefaultValue("sov") @QueryParam("driverId") String driverId, String content) {
        LOG.debug(String.format("Processing driverId %s", driverId));

        UpdateRequest updateRequest;

        try {
            updateRequest = UpdateRequest.fromJson(content);
        } catch (IOException ex) {
            if (LOG.isErrorEnabled()) {
                LOG.error(String.format("Request problem: %s", ex.getMessage()), ex);
            }

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getLocalizedMessage())
                    .build();
        }
        
        UpdateState updateState;
	String updateStateString;

        try {
            updateState = drivers.getDrivers().get(driverId).update(updateRequest);
            updateStateString = updateState == null ? null : updateState.toJson();
        } catch (JsonProcessingException | RegistrationException ex) {

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Driver reported for %s: %s", updateRequest, ex.getMessage()))
                    .build();
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Update state for " + updateRequest + ": " + updateStateString);
        }

        // no register state?
        if (updateRequest == null) {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(String.format("No update state for %s: ", updateRequest))
                    .build();
        }

        // write register state
        return Response
                .status(Response.Status.OK)
                .entity(updateStateString)
                .build();
    }

    @Override
    public UpdateState update(String driverId, UpdateRequest updateRequest) throws RegistrationException {
        UniRegistrar registrar = new LocalUniRegistrar(drivers);
        return registrar.update(driverId, updateRequest);
    }

    @Override
    public DeactivateState deactivate(String driverId, DeactivateRequest deactivateRequest) throws RegistrationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Map<String, Object>> properties() throws RegistrationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
