package com.github.marcodama7.posty.listeners;

import com.github.marcodama7.posty.message.PostyResponse;

/**
 *  Generic interface for implement listener call back when server response, in multiple calls
 */
public interface PostyMultipleResponseListener {

    public void onResponse(PostyResponse[] responses, int numberOfErrors);


}
