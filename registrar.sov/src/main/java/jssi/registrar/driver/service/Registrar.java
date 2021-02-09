/*
 * The MIT License
 *
 * Copyright 2020 UBICUA.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
