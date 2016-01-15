package org.moonwave.util;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;

public class JSFUtil {

    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public static Map<String, String> getRequestParameterMap() {
        return getExternalContext().getRequestParameterMap();
    }

    public static Map<String, Object> getSessionMap(){
        return getExternalContext().getSessionMap();
    }

    /**
     * Sample output - http://localhost:8080/context
     * 
     */
    public static String getApplicationURL() {
        HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
        return baseURL;
    }

    public static String getRequestContextPath() {
        return JSFUtil.getExternalContext().getRequestContextPath();
    }

    public static Flash getFlash() {
        return getExternalContext().getFlash();
    }

    public static NavigationHandler getNavigationHandler() {
        return FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    }

    public static ConfigurableNavigationHandler getConfigurableNavigationHandler() {
        return (ConfigurableNavigationHandler) getNavigationHandler();
    }

    /**
     * Sample - JSFUtil.forward("/error/error.xhtml")
     *
     * @param path Context relative path to the specified resource,
     *  which must start with a slash ("/") character
     */
    public static void forward(String path) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().dispatch(path);
    }

    /**
     * Sample - JSFUtil.navigation("/error/error.xhtml?faces-redirect=true")
     *
     * @param url
     */
    public static void navigation(String url) {
        ConfigurableNavigationHandler cnh = getConfigurableNavigationHandler();
        cnh.performNavigation(url);
    }

    /**
     * Sample - JSFUtil.redirectTo("/error/error.xhtml")
     * 
     * @param url
     */
    public static void redirectTo(String pageUrl) throws IOException {
       ExternalContext ec = JSFUtil.getExternalContext();
       ec.redirect(ec.getRequestContextPath() + pageUrl);
    }
}