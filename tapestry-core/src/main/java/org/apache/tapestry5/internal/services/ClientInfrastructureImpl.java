// Copyright 2009 The Apache Software Foundation
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

package org.apache.tapestry5.internal.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ClientInfrastructure;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * The default Javascript Stack consists of Prototype, Scriptaculous & the Tapestry-specific library.
 *
 * @since 5.1.0.2
 */
public class ClientInfrastructureImpl implements ClientInfrastructure
{
    private final SymbolSource symbolSource;

    private final AssetSource assetSource;

    private final ThreadLocale threadLocale;

    private final List<Asset> javascriptStack, stylesheetStack, javascriptStackTestMode, stylesheetStackTestMode;
    
    private final boolean isBlackbirdEnabled;

    private static final String[] CORE_JAVASCRIPT = new String[]
            {
                    // Core scripts added to any page that uses scripting

                    "${tapestry.scriptaculous}/prototype.js",
                    "${tapestry.scriptaculous}/scriptaculous.js",
                    "${tapestry.scriptaculous}/effects.js",

                    // Uses functions defined by the prior three

                    "org/apache/tapestry5/tapestry.js"
            };

    private static final String[] CORE_JAVASCRIPT_TEST_MODE = new String[]
            {
                    // Only available in test mode

                    "${tapestry.blackbird}/blackbird.js"
            };
    

    private static final String[] CORE_STYLESHEET = new String[]
            {
                    "${tapestry.default-stylesheet}"
            };
    

    private static final String[] CORE_STYLESHEET_TEST_MODE = new String[]
            {
                    // Only available in test mode
        
                    "${tapestry.blackbird}/blackbird.css"
            };

    public ClientInfrastructureImpl(SymbolSource symbolSource,
                                    AssetSource assetSource,
                                    ThreadLocale threadLocale,
                                    @Symbol(SymbolConstants.BLACKBIRD_ENABLED)
                                    boolean isBlackbirdEnabled)
    {
        this.symbolSource = symbolSource;
        this.assetSource = assetSource;
        this.threadLocale = threadLocale;
        this.isBlackbirdEnabled = isBlackbirdEnabled;

        javascriptStack = convertToAssets(CORE_JAVASCRIPT);
        stylesheetStack = convertToAssets(CORE_STYLESHEET);
        
        javascriptStackTestMode = convertToAssets(CORE_JAVASCRIPT_TEST_MODE);
        stylesheetStackTestMode = convertToAssets(CORE_STYLESHEET_TEST_MODE);
    }

    private List<Asset> convertToAssets(String[] paths)
    {
        List<Asset> assets = CollectionFactory.newList();

        for (String path : paths)
        {
            assets.add(expand(path, null));
        }

        return Collections.unmodifiableList(assets);
    }

    private Asset expand(String path, Locale locale)
    {
        String expanded = symbolSource.expandSymbols(path);

        return assetSource.getAsset(null, expanded, locale);
    }

    public List<Asset> getJavascriptStack()
    {
        List<Asset> result = createStack(javascriptStack, javascriptStackTestMode);

        Asset messages = assetSource.getAsset(null, "org/apache/tapestry5/tapestry-messages.js",
                                              threadLocale.getLocale());

        result.add(messages);

        return result;
    }

    public List<Asset> getStylesheetStack()
    {   
        return createStack(stylesheetStack, stylesheetStackTestMode);
    }
    
    private List<Asset> createStack(List<Asset> stack, List<Asset> optionalStack)
    {
        List<Asset> result = CollectionFactory.newList(stack);
        
        if(isBlackbirdEnabled)
        {
            result.addAll(optionalStack);
        }
        
        return result;
    }
}
