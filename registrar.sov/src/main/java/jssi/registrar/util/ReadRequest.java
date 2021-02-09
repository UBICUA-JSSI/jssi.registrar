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
