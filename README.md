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