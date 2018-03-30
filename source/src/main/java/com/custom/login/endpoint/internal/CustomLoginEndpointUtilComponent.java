package com.custom.login.endpoint.internal;

import com.custom.login.endpoint.servlet.LoginContextServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.equinox.http.helper.ContextPathServletAdaptor;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;

import javax.servlet.Servlet;

import static com.custom.login.endpoint.util.CustomFileUtil.buildEndpointUtilProperties;

/**
 * @scr.component name="CustomLoginEndpointUtilComponent" immediate="true"
 * @scr.reference name="osgi.httpservice"
 * interface="org.osgi.service.http.HttpService"
 * cardinality="1..1" policy="dynamic" bind="setHttpService"
 * unbind="unsetHttpService"
 */
public class CustomLoginEndpointUtilComponent {

    private static final Log log = LogFactory.getLog(CustomLoginEndpointUtilComponent.class);
    private HttpService httpService;

    protected void activate(ComponentContext context) {

        try {
            buildEndpointUtilProperties();

            Servlet sessionInvalidateServlet = new ContextPathServletAdaptor(new LoginContextServlet(),
                    "/logincontext");
            httpService.registerServlet("/logincontext", sessionInvalidateServlet, null, null);

        } catch (Throwable e) {
            log.error("Failed to initialize CustomLoginEndpointUtilComponent.", e);
        }
        log.info("CustomLoginEndpointUtilComponent is activated");
    }

    protected void deactivate(ComponentContext context) {

        log.info("CustomLoginEndpointUtilComponent is deactivated");
    }

    protected void setHttpService(HttpService httpService) {
        if (log.isDebugEnabled()) {
            log.debug("HTTP Service is set in the Custom Endpoint Login bundle");
        }

        this.httpService = httpService;
    }

    protected void unsetHttpService(HttpService httpService) {
        if (log.isDebugEnabled()) {
            log.debug("HTTP Service is unset in the Custom Endpoint Login bundle");
        }

        this.httpService = null;
    }
}
