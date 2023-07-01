/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.exception;

/**
 * @author Catch
 * @since 2023-05-30
 */
public class RequestTooLargeException extends HttpStatusException {

    public RequestTooLargeException(String message) {
        super(413, message);
    }

    public RequestTooLargeException(String format, Object... args) {
        super(413, String.format(format, args));
    }

    public RequestTooLargeException(Throwable t) {
        super(413, t);
    }

    public RequestTooLargeException(String message, Throwable t) {
        super(413, message, t);
    }

}
