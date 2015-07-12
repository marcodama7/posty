# Posty <a href='https://bintray.com/marcodama7/maven/Posty/_latestVersion'><img src='https://api.bintray.com/packages/marcodama7/maven/Posty/images/download.svg'></a>
Android Library for create network requests asynchronous easily and customizable, added one or more files to be upload.

##Examples

```java

Posty.newRequest("http://www.yoururl.com")
                .call(new PostyResponseListener() {
                    @Override
                    public void onResponse(PostyResponse response) {
                        Log.d("POSTY", response.getResponse());
                    }
                });
```

or with more customization:

```java

Posty.newRequest("http://www.youruri.com")
                .method(PostyMethod.POST)
                .header("Custom-Header-Name", "Custom-Header-Value")
                .body(jsonObject)
                .call(new PostyResponseListener() {
                    @Override
                    public void onResponse(PostyResponse response) {
                        Log.d("POSTY", response.getResponse());
                    }
                });
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
					Log.d("POSTY", message);
				}
			});
```

## Installing

There are three ways to install this library

#### As a Gradle dependency

Simply add:

```groovy
compile 'com.github.marcodama7.posty:posty:0.1.1'
```
in the gradle.build file

#### As a library project

Download the source code and import it as a module in Android Studio. The project is available in the folder **posty**. 

#### As a jar
Visit the [releases](https://github.com/marcodama7/posty/releases/) page to download jars directly. You can drop them into your `libs` folder and configure the gradle to add this library.

