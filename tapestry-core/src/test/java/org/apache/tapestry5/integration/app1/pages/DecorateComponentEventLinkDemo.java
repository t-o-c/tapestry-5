// Copyright 2010 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.integration.app1.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.services.ComponentEventRequestParameters;


public class DecorateComponentEventLinkDemo
{
    
    @Property
    @Persist
    private boolean decorated;
    
    void onDecorateComponentEventLink(Link link, ComponentEventRequestParameters params)
    {
        link.addParameter("decorated", "true");
    }
   
    
    void onAction(@RequestParameter("decorated") boolean decorated)
    {
        this.decorated = decorated;
    }
}
