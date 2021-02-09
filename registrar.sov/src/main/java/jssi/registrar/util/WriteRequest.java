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
