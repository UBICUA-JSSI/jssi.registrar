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
package jssi.registrar.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jssi.registrar.driver.sov.SovConfig;
import jssi.registrar.driver.sov.SovDriver;
import uniregistrar.RegistrationException;
import uniregistrar.request.RegisterRequest;
import uniregistrar.request.UpdateRequest;
import uniregistrar.state.RegisterState;
import uniregistrar.state.UpdateState;

/**
 * REST Web Service
 *
 * @author UBICUA
 */
@Path("/")
@RequestScoped
public class Registrar {
    
    private static final Logger LOG = LoggerFactory.getLogger(Registrar.class);

    @Context
    private UriInfo context;
    
    @Inject 
    SovConfig config;

    /**
     * Creates a new instance of Resolver
     */
    public Registrar() {
    }

    /**
     * Retrieves representation of an instance of ubicua.registrar.service.Resolver
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register/")
    public Response register(String content) {
        
        SovDriver driver = new SovDriver(config);
        
        RegisterRequest registerRequest;
        
        try {
            
            registerRequest = RegisterRequest.fromJson(content);
            RegisterState result = driver.register(registerRequest);
            
            if(result == null){
                List list = (List) registerRequest.getSecret().get("keys");
                Map map = (Map) list.get(0);
                String did = (String) map.get("did");
                
                return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Registrar result is null for %s", did))
                    .build();
            }
            
            return Response
                .status(Response.Status.OK)
                .entity(result.toJson())
                .build();
        } catch (RegistrationException | IOException ex){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Registrar problem %s", ex.getMessage()))
                    .build();
        }
    }
    
    /**
     * Retrieves representation of an instance of ubicua.registrar.service.Resolver
     * @param content
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("update/")
    public Response update(String content) {
        
        SovDriver driver = new SovDriver(config);
        
        UpdateRequest updateRequest;
        
        try {
            
            updateRequest = UpdateRequest.fromJson(content);
            UpdateState result = driver.update(updateRequest);
            
            if(result == null){
                List list = (List) updateRequest.getSecret().get("keys");
                Map map = (Map) list.get(0);
                String did = (String) map.get("did");
                
                return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Update result is null for %s", did))
                    .build();
            }
            
            return Response
                .status(Response.Status.OK)
                .entity(result.toJson())
                .build();
        } catch (RegistrationException | IOException ex){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(String.format("Update problem %s", ex.getMessage()))
                    .build();
        }
    }
}
