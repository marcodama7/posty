# posty
Android Library for create Http requests asynchronous easily and customizable, with added one or more files.


Example of usage:

```java

Posty.newRequest("http://www.youruri.com")
                .onResponse(new PostyResponseListener() {
                    @Override
                    public void onResponse(PostyResponse response) {
                        Log.d("POSTY", response.getResponse());
                    }
                })
                .call();
```

or with more customization:

```java

Posty.newRequest("http://www.youruri.com")
                .method(PostyMethod.POST)
                .header("Custom-Header-Name", "Custom-Header-Value")
                .body(jsonObject)
                .onResponse(new PostyResponseListener() {
                    @Override
                    public void onResponse(PostyResponse response) {
                        Log.d("POSTY", response.getResponse());
                    }
                })
                .call();
```

It is possible to call multple requests, and set callback called when all requests are sended and received

```java

Posty.
	//first request
	newRequest("http://www.yourfirsturi.com")
		.onResponse(new PostyResponseListener() {
			@Override
			public void onResponse(PostyResponse response) {
				Log.d("POSTY", response.getResponse());
			}
		})
	// second request			
		.newRequest("http://www.yourseconduri.com")
		.onResponse(new PostyResponseListener() {
			@Override
			public void onResponse(PostyResponse response) {
				// in this second response, I can read the previous request/response
				// with call response.getPreviousResponse();
				Log.d("POSTY", response.getResponse());
			}
		})
	// call all requests and set callback
        .multipleCall(new PostyMultipleResponseListener() {
				@Override
				public void onResponse(PostyResponse[] responses, int numberOfErrors) {
					
					String message = "This dialog is showed when all http calls are sended and received.";
					message+=" I can read "+responses+" responses with "+numberOfErrors+" errors";
					displayDialog("All results", message);
				}
			});
			
```