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
public enum ReadRequest {
    GET_TXN(3),
    GET_TXN_AUTHOR_AGREEMENT(6),
    GET_TXN_AUTHOR_AGREEMENT_AML(7),
    GET_ATTR(104),
    GET_NYM(105),
    GET_SCHEMA(107),
    GET_CLAIM_DEF(108),
    GET_REVOC_REG_DEF(115),
    GET_REVOC_REG(116),
    GET_REVOC_REG_DELTA(117),
    GET_AUTH_RULE(121);

    public final int code;

    private ReadRequest(int code) {
        this.code = code;
    }
}
