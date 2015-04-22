package com.github.marcodama7.postyproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.marcodama7.posty.core.Posty;
import com.github.marcodama7.posty.core.PostyMethod;
import com.github.marcodama7.posty.listeners.PostyMultipleResponseListener;
import com.github.marcodama7.posty.listeners.PostyResponseListener;
import com.github.marcodama7.posty.message.PostyFile;
import com.github.marcodama7.posty.message.PostyResponse;
import com.github.marcodama7.posty.util.RealPathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {


    private static final int PICK_IMAGE_1 = 1;
    private static final int PICK_IMAGE_2 = 2;
    Button call;
    EditText _uri;
    EditText postdatakey;
    EditText postdatavalue;
    EditText image1_key;
    ImageView image1;
    EditText image2_key;
    ImageView image2;

    String filePathImage1 = null;
    String filePathImage2 = null;
    String realFilePathImage1 = null;
    String realFilePathImage2 = null;
    MainActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        call = (Button) findViewById(R.id.call);
        instance = this;

        _uri = (EditText) findViewById(R.id.uri);
        postdatakey = (EditText) findViewById(R.id.postdatakey);
        postdatavalue = (EditText) findViewById(R.id.postdatavalue);
        image1_key = (EditText) findViewById(R.id.image1_key);
        image1 = (ImageView) findViewById(R.id.image1);
        image2_key = (EditText) findViewById(R.id.image2_key);
        image2 = (ImageView) findViewById(R.id.image2);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_1);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_2);
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reading post data:
                Map<String, String> postData = null;
                if (postdatakey.getText().length() > 0 && postdatavalue.getText().length() > 0) {
                    postData = new HashMap<String, String>();
                    postData.put(postdatakey.getText().toString(), postdatavalue.getText().toString());
                }

                // Reading files attachment data
                List<PostyFile> files = new ArrayList<PostyFile>();
                if (realFilePathImage1 != null && realFilePathImage1.length() > 0) {
                    String key = (image1_key.getText().length() > 0) ? image1_key.getText().toString() : "immagine1";
                    files.add(new PostyFile(key, realFilePathImage1));
                }
                if (realFilePathImage2 != null && realFilePathImage2.length() > 0) {
                    String key = (image2_key.getText().length() > 0) ? image2_key.getText().toString() : "immagine2";
                    files.add(new PostyFile(key, realFilePathImage2));
                }

                // First request
                Posty.newRequest(_uri.getText().toString())
                        .body(postData, files)
                        .header("X-Request-Number", "1")
                        .method(PostyMethod.POST)
                        .body(postData)
                        .onResponse(new PostyResponseListener() {
                            @Override
                            public void onResponse(PostyResponse postyResponse) {
                                if (postyResponse.inError()) {
                                    // Http call error
                                    String message = postyResponse.getResponse();
                                    if (message == null || message.length() < 1) {
                                        message = (postyResponse.getErrorMessage() != null && postyResponse.getErrorMessage().length() > 0) ? postyResponse.getErrorMessage() : "{empty}";
                                    }
                                    displayDialog("First Result", message);
                                } else {
                                    // Http call success!
                                    String response = (postyResponse.getResponse() != null) ? postyResponse.getResponse() : "{empty}";
                                    displayDialog("First Result", response);
                                }
                            }
                        })
                        //.call();
                        // second request:
                        .newRequest(_uri.getText().toString())
                        .body(postData, files)
                        .header("X-Request-Number", "2")
                        .method(PostyMethod.POST)
                        .body(postData)
                        .onResponse(new PostyResponseListener() {
                            @Override
                            public void onResponse(PostyResponse postyResponse) {
                                if (postyResponse.inError()) {
                                    // Http call error
                                    String message = postyResponse.getResponse();
                                    if (message == null || message.length() < 1) {
                                        message = (postyResponse.getErrorMessage() != null && postyResponse.getErrorMessage().length() > 0) ? postyResponse.getErrorMessage() : "{empty}";
                                    }
                                    displayDialog("Second Result", message);
                                } else {
                                    // Http call success!
                                    String response = (postyResponse.getResponse() != null) ? postyResponse.getResponse() : "{empty}";
                                    displayDialog("Second Result", response);
                                }
                            }
                        })
                        .multipleCall(new PostyMultipleResponseListener() {
                            @Override
                            public void onResponse(PostyResponse[] responses, int numberOfErrors) {
                                int numberOfResponses = (responses == null) ? 0 : responses.length;
                                String message = "This dialog is showed when all http calls are sended and received.";
                                message+=" I can read "+numberOfResponses+" responses with "+numberOfErrors+" errors";
                                displayDialog("All results", message);
                            }
                        });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayDialog(String title, String message) {
        // Connection error
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_IMAGE_1 && data != null && data.getData() != null) {
            Uri _uri = data.getData();
            //User had pick an image.
            Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();

            //Link to the image
            filePathImage1 = cursor.getString(0);
            realFilePathImage1 = RealPathUtil.getRealPathFromURI(instance, _uri);
            RealPathUtil.setImageSourceFromUri(instance, image1, realFilePathImage1);
            cursor.close();
        }
        else if(requestCode == PICK_IMAGE_2 && data != null && data.getData() != null) {
            Uri _uri = data.getData();
            //User had pick an image.
            Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            //Link to the image
            filePathImage2 = cursor.getString(0);
            realFilePathImage2 = RealPathUtil.getRealPathFromURI(instance, _uri);
            RealPathUtil.setImageSourceFromUri(instance, image2, realFilePathImage2);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
