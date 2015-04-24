package com.github.marcodama7.posty;

import com.github.marcodama7.posty.core.PostyRequestDec;
import com.github.marcodama7.posty.request.PostyRequest;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Principal class for create instance of PostyRequestDec, wich wrap a new http request
 */
public class Posty {

    private static CookieManager cookieManager;

    private PostyRequestDec ptRequest;

    private Posty(){

    }

    private Posty(PostyRequestDec ptRequest) {
        this.ptRequest = ptRequest;
    }

    public static PostyRequestDec newRequest(String uri){
        Posty posty = new Posty(new PostyRequestDec(new PostyRequest(uri)));
        return posty.ptRequest;
    }

    public static CookieManager getCookieManager() {
        if (cookieManager == null) {
            cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        }
        return cookieManager;
    }


    public static void setCookieManager(CookieManager cookieManager) {
        Posty.cookieManager = cookieManager;
    }

    public static void clearCookie(){
        getCookieManager().getCookieStore().removeAll();
    }

    public static void clearCookie(URI uri){
        List<HttpCookie> cookieList = getCookieManager().getCookieStore().get(uri);
        if (cookieList != null) {
            for (HttpCookie cookie : cookieList) {
                getCookieManager().getCookieStore().remove(uri,cookie);
            }
        }
    }



}
