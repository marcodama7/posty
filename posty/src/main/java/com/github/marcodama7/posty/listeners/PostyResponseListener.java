package com.github.marcodama7.posty.listeners;
import com.github.marcodama7.posty.request.PostyResponse;

/**
 *  interface implement listener call back when server response
 */
public interface PostyResponseListener {
    void onResponse(PostyResponse response);
}
