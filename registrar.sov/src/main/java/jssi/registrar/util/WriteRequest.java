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
package jssi.registrar.util;

/**
 *
 * @author UBICUA
 */
public enum WriteRequest {
    
    NODE("0"),
    NYM("1"),
    TXN_AUTHOR_AGREEMENT("4"),
    TXN_AUTHOR_AGREEMENT_AML("5"),
    ATTRIB("100"),
    SCHEMA("101"),
    CLAIM_DEF("102"),
    POOL_UPGRADE("109"),
    NODE_UPGRADE("110"),
    POOL_CONFIG("111"),
    REVOC_REG_DEF("113"),
    REVOC_REG_ENTRY("114"),
    AUTH_RULE("20"),
    AUTH_RULES("122");

    public final String code;

    private WriteRequest(String code) {
        this.code = code;
    }
}
