package com.github.marcodama7.posty.listeners;

import com.github.marcodama7.posty.request.PostyResponse;

/**
 *  Generic interface for implement listener call back when server response
 */
public interface PostyResponseListener {

    public void onResponse(PostyResponse response);

}
