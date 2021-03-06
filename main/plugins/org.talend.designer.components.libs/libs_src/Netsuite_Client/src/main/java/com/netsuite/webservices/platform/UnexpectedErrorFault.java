
package com.netsuite.webservices.platform;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.0
 * 2015-05-08T11:20:33.878+08:00
 * Generated source version: 3.1.0
 */

@WebFault(name = "unexpectedErrorFault", targetNamespace = "urn:faults_2014_2.platform.webservices.netsuite.com")
public class UnexpectedErrorFault extends Exception {
    
    private com.netsuite.webservices.platform.faults.UnexpectedErrorFault unexpectedErrorFault;

    public UnexpectedErrorFault() {
        super();
    }
    
    public UnexpectedErrorFault(String message) {
        super(message);
    }
    
    public UnexpectedErrorFault(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedErrorFault(String message, com.netsuite.webservices.platform.faults.UnexpectedErrorFault unexpectedErrorFault) {
        super(message);
        this.unexpectedErrorFault = unexpectedErrorFault;
    }

    public UnexpectedErrorFault(String message, com.netsuite.webservices.platform.faults.UnexpectedErrorFault unexpectedErrorFault, Throwable cause) {
        super(message, cause);
        this.unexpectedErrorFault = unexpectedErrorFault;
    }

    public com.netsuite.webservices.platform.faults.UnexpectedErrorFault getFaultInfo() {
        return this.unexpectedErrorFault;
    }
}
