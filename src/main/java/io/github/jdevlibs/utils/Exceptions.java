/*  ---------------------------------------------------------------------------
 *  * Copyright 2020-2021 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  ---------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils;

/**
* @author Supot Saelao
* @version 1.0
*/
public final class Exceptions {
	private Exceptions() {}
	
    public static RuntimeException toRuntimeException(Throwable throwable) {
        return throwable instanceof RuntimeException ? (RuntimeException) throwable
                : new RuntimeException(throwable);
    }

    public static RuntimeException toRuntimeException(String message, Throwable throwable) {
        return message == null ? toRuntimeException(throwable)
                : new RuntimeException(message, throwable);
    }
    
    public static RuntimeException toRuntimeException(String message) {
        return new RuntimeException(message);
    }
}
