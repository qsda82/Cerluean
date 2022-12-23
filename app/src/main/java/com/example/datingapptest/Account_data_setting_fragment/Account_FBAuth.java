package com.example.datingapptest.Account_data_setting_fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.Fragment.AccountFragment;
import com.example.datingapptest.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Account_FBAuth extends Fragment {
    private TextView fb_data_show;
    private LoginButton login_button;
    private ImageView img;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private String name, email, birthday, id;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account_fbauth, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity().getApplication());
        login_button = (LoginButton) getView().findViewById(R.id.login_button);
        fb_data_show = (TextView) getView().findViewById(R.id.fb_data_show);
        callbackManager = CallbackManager.Factory.create();
        img = (ImageView) getView().findViewById(R.id.imageView2);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        login_logout_listener();
        access();
        fb_login();

    }
    private void login_logout_listener(){
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {

                if (accessToken == null) {

                } else if (accessToken2 == null) {
                    img.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }
            }
        };
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void access()  {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null && !accessToken.isExpired()){
            GraphRequest request = GraphRequest.newMeRequest(accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                id = object.getString("id");
                                String imgURLStr = "https://graph.facebook.com/" + id + "/picture";
                                new AsyncTask<String, Void, Bitmap>() {
                                    @Override
                                    protected Bitmap doInBackground(String... params) {
                                        String url = params[0];
                                        return getBitmapFromURL(url);
                                    }

                                    @Override
                                    protected void onPostExecute(Bitmap result) {

                                        img.setImageBitmap(result);
                                        super.onPostExecute(result);

                                    }
                                }.execute(imgURLStr);

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

    }

    private void fb_login() {
        login_button.setReadPermissions("email", "public_profile");
        // If using in a fragment
        login_button.setFragment(this);

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    // String x=object.getString("birthday");
                                    id = object.getString("id");
                                    name = object.getString("name");
                                    email = object.getString("email");
                                    fb_data_show.setText(id + "," + name + "," + email);
                                    String width = "50";
                                    String height = "50";

                                    String imgURLStr = "https://graph.facebook.com/" + id + "/picture";
                                    new AsyncTask<String, Void, Bitmap>() {
                                        @Override
                                        protected Bitmap doInBackground(String... params) {
                                            String url = params[0];
                                            return getBitmapFromURL(url);
                                        }

                                        @Override
                                        protected void onPostExecute(Bitmap result) {

                                            img.setImageBitmap(result);
                                            super.onPostExecute(result);

                                        }
                                    }.execute(imgURLStr);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
                Toast.makeText(Account_FBAuth.this.getContext(), "認證成功",
                        Toast.LENGTH_SHORT).show();
                save_verification();
                dialog();
            }




            private void save_verification() {
                FirebaseUser user = mAuth.getCurrentUser();
                Map<String, Object> MESSAGE = new HashMap<>();
                MESSAGE.put("臉書認證", "是");
                db.collection("personal_data")
                        .document(user.getUid())
                        .update(MESSAGE);
            }

            private void dialog() {
                new AlertDialog.Builder(Account_FBAuth.this.getContext())
                        .setMessage("是否沿用臉書姓名與信箱？")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_menu_edit)
                        .setTitle("資料沿用?")
                        .setPositiveButton("是", listener)
                        .setNegativeButton("否", listener)
                        .show();
            }


            private AlertDialog.OnClickListener listener = new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Map<String, Object> MESSAGE = new HashMap<>();
                        MESSAGE.put("姓名", name);
                        MESSAGE.put("信箱", email);
                        //MESSAGE.put("生日",birthday);
                        db.collection("personal_data")
                                .document(user.getUid())
                                .update(MESSAGE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new AccountFragment(), null)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new AccountFragment(), null)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            };


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
}
