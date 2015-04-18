package com.marcodama7.posty.listeners;

import com.marcodama7.posty.message.PostyResponse;

/**
 *  Generic interface for implement listener call back when server response
 */
public interface PostyResponseListener {

    public void onResponse(PostyResponse response);

}
