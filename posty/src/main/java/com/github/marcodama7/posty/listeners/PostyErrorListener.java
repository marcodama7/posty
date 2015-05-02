package com.github.marcodama7.posty.listeners;

import com.github.marcodama7.posty.request.PostyResponse;

/**
 *  interface implement listener call back when server response with an error
 */
public interface PostyErrorListener {
    void onError(PostyResponse response);
}
