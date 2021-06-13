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
package jssi.registrar.driver.sov;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.pool.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import static org.hyperledger.indy.sdk.ledger.Ledger.multiSignRequest;
import static org.hyperledger.indy.sdk.ledger.Ledger.submitRequest;
import jssi.registrar.util.WriteRequest;
import uniregistrar.RegistrationException;
import uniregistrar.driver.Driver;
import uniregistrar.request.DeactivateRequest;
import uniregistrar.request.RegisterRequest;
import uniregistrar.request.UpdateRequest;
import uniregistrar.state.DeactivateState;
import uniregistrar.state.RegisterState;
import uniregistrar.state.SetRegisterStateFinished;
import uniregistrar.state.UpdateState;


public class SovDriver implements Driver {

    private static final Logger LOG = LoggerFactory.getLogger(SovDriver.class);

    private static final Gson gson = new Gson();
    private SovConfig config = null;
    
    public SovDriver(SovConfig config) {
        this.config = config;
    }

    @Override
    public RegisterState register(RegisterRequest registerRequest) throws RegistrationException {
        
        String jobId = registerRequest.getJobId();
        
        List keys = (List) registerRequest.getSecret().get("keys");
        Map key = (Map) keys.get(0);
        String targetDid = (String) key.get("id");
        String targetVerkey = (String) key.get("verkey");
        
        String endorserDid = config.getEndorserDid();
        
        // open pool
        if (config.getPoolMap().isEmpty() || config.getPoolVersionMap().isEmpty() || config.getWallet() == null || config.getEndorserDid()== null) {
            throw new RegistrationException("General error resolver initialization");
        }

        String network = (String) registerRequest.getOptions().get("network");
        if (network == null || network.trim().isEmpty()) {
            network = "ubicua";
        }

        // find pool version
        Integer poolVersion = config.getPoolVersionMap().get(network);
        if (poolVersion == null) {
            throw new RegistrationException("No pool version for network: " + network);
        }

        // find pool
        Pool pool = config.getPoolMap().get(network);
        if (pool == null) {
            throw new RegistrationException("No pool for network: " + network);
        }

        if(isDidRegistered(pool, endorserDid, targetDid)){
            return null;
        }
        
        String buildNymResponse;
        
        try {
            String nymRequest = Ledger.buildNymRequest(endorserDid, targetDid, targetVerkey, null, null).get();
            buildNymResponse = Ledger.signAndSubmitRequest(pool, config.getWallet(), endorserDid, nymRequest).get();
        } catch (IndyException | InterruptedException | ExecutionException ex) {
            throw new RegistrationException("Cannot build GET_NYM request: " + ex.getMessage(), ex);
        }
        
        LOG.debug("GET_NYM for " + targetDid + ": " + buildNymResponse);
        
        Map<String, Object> methodMetadata = new LinkedHashMap<>();
        methodMetadata.put("network", network);
        methodMetadata.put("poolVersion", poolVersion);
        methodMetadata.put("submitterDid", endorserDid);
        // done
        RegisterState registerState = RegisterState.build();
        SetRegisterStateFinished.setStateFinished(registerState, targetDid, registerRequest.getSecret());
        registerState.setJobId(jobId);
        registerState.setMethodMetadata(methodMetadata);

        return registerState;
    }
    
    @Override
    public UpdateState update(UpdateRequest updateRequest) throws RegistrationException {
        
        String endorserDid = config.getEndorserDid();
        
        String network = (String) updateRequest.getOptions().get("network");
        if (network == null || network.trim().isEmpty()) {
            network = "ubicua";
        }

        // find pool version
        Integer poolVersion = config.getPoolVersionMap().get(network);
        if (poolVersion == null) {
            throw new RegistrationException("No pool version for network: " + network);
        }
        // find pool
        Pool pool = config.getPoolMap().get(network);
        if (pool == null) {
            throw new RegistrationException("No pool for network: " + network);
        }
        
        String type = (String) updateRequest.getAction().getOperation().get("type");
        
        
        boolean hasCode = false;
        for (WriteRequest item : WriteRequest.values()) {
            if (item.code.equals(type)) {
                hasCode = true;
                break;
            }
        }
        if (!hasCode) {
            throw new RegistrationException("No attribute of valid type");
        }
        
        String buildAttrResponse;
        try {
            String op = multiSignRequest(config.getWallet(), endorserDid, updateRequest.getAction().toJson()).get();
            buildAttrResponse = submitRequest(pool, op).get();
        } catch (IndyException | InterruptedException | ExecutionException | JsonProcessingException ex) {
            throw new RegistrationException("Cannot build ATTRIB request: " + ex.getMessage(), ex);
        }

        LOG.debug("ATTRIB for " + updateRequest.getAction().getIdentifier()  + ": " + buildAttrResponse);
        
        Map<String, Object> methodMetadata = new LinkedHashMap<>();
        methodMetadata.put("network", network);
        methodMetadata.put("poolVersion", poolVersion);
        methodMetadata.put("submitterDid", endorserDid);
        // done
        UpdateState updateState = UpdateState.build();
        
        updateState.setMethodMetadata(methodMetadata);
        return updateState;

    }

    @Override
    public DeactivateState deactivate(DeactivateRequest deactivateRequest) throws RegistrationException {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Map<String, Object> properties() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean isDidRegistered(Pool pool, String endorserDid, String targetDid) throws RegistrationException{
        // send GET_NYM request
        String getNymResponse;

        try {

            String nymRequest = Ledger.buildGetNymRequest(endorserDid, targetDid).get();
            getNymResponse = Ledger.signAndSubmitRequest(pool, config.getWallet(), endorserDid, nymRequest).get();

        } catch (IndyException | InterruptedException | ExecutionException ex) {
            throw new RegistrationException("Cannot send GET_NYM request: " + ex.getMessage(), ex);
        }

        LOG.debug("GET_NYM for " + targetDid + ": " + getNymResponse);

        // check GET_NYM response data
        JsonObject nymResponse = gson.fromJson(getNymResponse, JsonObject.class);
        JsonObject nymResult = nymResponse == null ? null : nymResponse.getAsJsonObject("result");
        JsonElement nymData = nymResult == null ? null : nymResult.get("data");
        JsonObject nymDataContent = (nymData == null || nymData instanceof JsonNull) ? null : gson.fromJson(nymData.getAsString(), JsonObject.class);

        return nymDataContent != null;
    }
}
