# JSSI Registrar Server
Java web application forked from [DIF Universal Registrar](https://github.com/decentralized-identity/universal-registrar)

JSSI Registrar is aimed at registering decentralized identities. To include different storage systems, the driver-based architecure is used. The default sovrin driver provides access to [Hyperledger Indy DLT](https://github.com/hyperledger/indy-sdk).

## Prerequisites
- Apache Netbeans 12+
- Open JDK 15+
- Wildfly 21+

## Dependencies
- registrar.lib
- hyperledger.lib

## Configuration
The configuration directory is specified in the <install_dir>/resolver/registrar.web/src/main/webapp/WEB-INF/web.xml:
```
<context-param>
        <param-name>jssi.driver.config</param-name>
        <param-value><install_dir>/registrar/registrar.assets/config.json</param-value>
 </context-param>
```
Indicate the complete physical path to the config.json file that contains the driver configurations included in the registrar.ear archive. The configuration parameter is located at <install_dir>/registrar/registrar.sov/src/main/webapp/WEB-INF/web.xml:

```
<context-param>
        <param-name>jssi.driver.config</param-name>
        <param-value><install_dir>/registrar/registrar.assets/driver.properties</param-value>
 </context-param>
```
The driver.properties file contains:
```
// Array of genesis
registrar.config=<install_dir>/registrar/registrar.assets/ubicua.genesis
// Libindy path
registrar.native=<install_dir>/hyperledger.native
// Resolver wallet
wallet.registrar.id=resolver_wallet
wallet.registrar.key=resolver_wallet_key
// Endorser
endorser.did=V4SGRU86Z58d6TV7PBUe6f
endorser.verkey=GJ1SzoWzavQYfNL9XkaJdrQejfztN4XqdsiV4ct3LXKL
```
For testing purposes, the Registrar DID has the assigned value of V4SGRU86Z58d6TV7PBUe6f. It means that the Registrar wallet contains the necessary cryptographic material to sign their requests to DLT. Before testing, it is necessary to check if the wallet has been created and the Resolver DID has been registered.

## Logging
To enable the logging in Wildfly, open <wildfly_install_dir>/standalone/configuration/standalone-full.xml and modify 
profile/subsystem xmlns="urn:jboss:domain:logging:8.0" as follows:
```
<logger category="jssi">
 	<level name="DEBUG"/>
 </logger>
 ```

## Execution
Compile and deploy the registrar.ear archive. The JSSI Registrar server plays the role of endorser, that is why a client application needs to access to the server and to register a DID signed by the client. To test this functionality, use the jssi.client/registar.client package.







