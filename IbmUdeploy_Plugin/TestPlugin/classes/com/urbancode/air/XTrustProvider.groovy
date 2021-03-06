/**
 * Copyright IBM Corporation 2014.  
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract *with IBM Corp. 
 */

package com.urbancode.air

import java.security.Provider;
import java.security.Security;

public class XTrustProvider extends Provider {

    //**********************************************************************************************
    // CLASS
    //**********************************************************************************************
    final static private long serialVersionUID = 1L;

    //----------------------------------------------------------------------------------------------
    static public void install() {
        if (Security.getProvider(XTrustProvider.class.getSimpleName()) == null) {
            Security.insertProviderAt(new XTrustProvider(), 2);
            Security.setProperty("ssl.TrustManagerFactory.algorithm", "XTrust509");
        }
    }

    //**********************************************************************************************
    // INSTANCE
    //**********************************************************************************************

    //----------------------------------------------------------------------------------------------
    public XTrustProvider() {
        super(XTrustProvider.class.getSimpleName(), 1D,
                "Basic XTrustProvider ignoring invalid certificates");

        put("TrustManagerFactory.XTrust509", XTrustManagerFactory.class.getName());
    }
}
