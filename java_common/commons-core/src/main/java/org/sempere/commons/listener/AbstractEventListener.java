/*
 * =============================================================================
 * Copyright by Benjamin Sempere,
 * All rights reserved.
 * =============================================================================
 * Author  : Benjamin Sempere
 * E-mail  : benjamin@sempere.org
 * Homepage: www.sempere.org
 * =============================================================================
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.sempere.commons.listener;

/**
 * Base implementation of EventListener.
 *
 * @author bsempere
 */
public abstract class AbstractEventListener<T extends Event> implements EventListener<T> {

    // *************************************************************************
    //
    // Constructors
    //
    // *************************************************************************

    public AbstractEventListener() {
    }

    // *************************************************************************
    //
    // Methods from EventListener interface 
    //
    // *************************************************************************

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void onEvent(T event) {
        this.onEvent(event, null);
    }
}
