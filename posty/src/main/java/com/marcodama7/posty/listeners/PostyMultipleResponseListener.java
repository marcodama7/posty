package com.marcodama7.posty.listeners;

import com.marcodama7.posty.message.PostyResponse;

import java.util.ArrayList;

/**
 *  Generic interface for implement listener call back when server response, in multiple calls
 */
public interface PostyMultipleResponseListener {

    public void onResponse(PostyResponse[] responses, int numberOfErrors);


}
