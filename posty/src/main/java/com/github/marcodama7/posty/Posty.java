package com.github.marcodama7.posty;

import com.github.marcodama7.posty.core.PostyRequestDec;
import com.github.marcodama7.posty.request.PostyRequest;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Principal class for create instance of PostyRequestDec: a wrapper for store a new Http request(s)
 */
public class Posty {

    private static CookieManager cookieManager;
    private PostyRequestDec ptRequest;

    private Posty(){

    }

    private Posty(PostyRequestDec ptRequest) {
        this.ptRequest = ptRequest;
    }

    /**
     * Create a new Http request based on a specific URI, and return an instance of PostyRequestDec
     * @param uri: String rappresent an URI to be call
     * @return an instance of PostyRequestDec
     */
    public static PostyRequestDec newRequest(String uri){
        Posty posty = new Posty(new PostyRequestDec(new PostyRequest(uri)));
        return posty.ptRequest;
    }

    /**
     * Return a Cookie Manager
     * @return a cookie manager
     */
    public static CookieManager getCookieManager() {
        if (cookieManager == null) {
            cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        }
        return cookieManager;
    }

    /**
     * Set another cookieManager
     * @param cookieManager a new CookieMagager
     */
    public static void setCookieManager(CookieManager cookieManager) {
        Posty.cookieManager = cookieManager;
    }

    /**
     * Remove all cookie
     */
    public static void clearCookie(){
        getCookieManager().getCookieStore().removeAll();
    }

    /**
     * Clear all cookie associated to a specific URI
     * @param uri: Object URI: object of the se
     */
    public static void clearCookie(URI uri){
        List<HttpCookie> cookieList = getCookieManager().getCookieStore().get(uri);
        if (cookieList != null) {
            for (HttpCookie cookie : cookieList) {
                getCookieManager().getCookieStore().remove(uri,cookie);
            }
        }
    }

}
