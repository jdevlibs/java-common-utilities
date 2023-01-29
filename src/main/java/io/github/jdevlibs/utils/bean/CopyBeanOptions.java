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
package io.github.jdevlibs.utils.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author supot.jdev
 * @version 1.0
 */
public class CopyBeanOptions {
    private List<String> sourceIgnores;
    private List<String> targetIgnores;

    public List<String> getSourceIgnores() {
        if (sourceIgnores == null) {
            sourceIgnores = new ArrayList<>();
        }
        return sourceIgnores;
    }

    public CopyBeanOptions sourceIgnores(List<String> sourceIgnores) {
        this.sourceIgnores = sourceIgnores;
        return this;
    }

    public CopyBeanOptions sourceIgnores(String ... properties) {
        if (properties != null && properties.length > 0) {
            Collections.addAll(getSourceIgnores(), properties);
        }
        return this;
    }

    public List<String> getTargetIgnores() {
        if (targetIgnores == null) {
            targetIgnores = new ArrayList<>();
        }
        return targetIgnores;
    }

    public CopyBeanOptions targetIgnores(List<String> targetIgnores) {
        this.targetIgnores = targetIgnores;
        return this;
    }

    public CopyBeanOptions targetIgnores(String ... properties) {
        if (properties != null && properties.length > 0) {
            Collections.addAll(getTargetIgnores(), properties);
        }
        return this;
    }

    public void resetSourceIgnores() {
        if (sourceIgnores != null) {
            sourceIgnores.clear();
        }
    }
    public static CopyBeanOptions defaultOptions() {
        return new CopyBeanOptions();
    }
}
