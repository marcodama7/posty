package com.github.marcodama7.posty.util;

/**
 * Created by Marco on 15/04/2015.
 */
public class UriUtils {

    public static String getBaseUri(String uri) {
        String uriOutput = null;
        String _uri = (uri != null) ? uri.toString() : null;
        if (_uri != null) {
            uriOutput = "";
            if (_uri.contains("//")) {
                uriOutput = _uri.substring(0,_uri.indexOf("//"))+"//";
                _uri = _uri.substring(_uri.indexOf("//")+2);
            }
            uriOutput += _uri.substring(0,_uri.indexOf("/"));
        }
        return uriOutput;
    }
}
