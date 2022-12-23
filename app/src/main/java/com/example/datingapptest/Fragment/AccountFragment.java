package com.example.datingapptest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.Account_data_setting_fragment.Account_EmailAuth;
import com.example.datingapptest.Account_data_setting_fragment.Account_FBAuth;
import com.example.datingapptest.Account_data_setting_fragment.Account_PWD_Revise;
import com.example.datingapptest.Account_data_setting_fragment.Account_PhoneAuth;
import com.example.datingapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    private ImageView fb_check_image, email_check_image, phone_check_image;
    private Button PhoneAuth_btn, EmailAuth_btn, FBAuth_btn, pwd_revise_btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PhoneAuth_btn = (Button) getView().findViewById(R.id.PhoneAuth_btn);
        EmailAuth_btn = (Button) getView().findViewById(R.id.EmailAuth_btn);
        FBAuth_btn = (Button) getView().findViewById(R.id.FBAuth_btn);
        pwd_revise_btn = (Button) getView().findViewById(R.id.pwd_revise_btn);
        PhoneAuth_btn.setOnClickListener(listener);
        EmailAuth_btn.setOnClickListener(listener);
        FBAuth_btn.setOnClickListener(listener);
        pwd_revise_btn.setOnClickListener(listener);
        fb_check_image = (ImageView) getView().findViewById(R.id.fb_check_image);
        email_check_image = (ImageView) getView().findViewById(R.id.email_check_image);
        phone_check_image = (ImageView) getView().findViewById(R.id.phone_check_image);
        fb_check_image.setImageResource(R.drawable.uncheck);
        email_check_image.setImageResource(R.drawable.uncheck);
        phone_check_image.setImageResource(R.drawable.uncheck);
        init();
    }

    private void email_check() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user.isEmailVerified()) {
            Map<String, Object> MESSAGE = new HashMap<>();
            MESSAGE.put("email認證", "是");
            db.collection("personal_data")
                    .document(user.getUid())
                    .update(MESSAGE);
            email_check_image.setImageResource(R.drawable.check);
        }
    }

    private void init() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("personal_data")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getString("臉書認證").equals("是")) {
                                fb_check_image.setImageResource(R.drawable.check);
                            }

                            if (task.getResult().getString("手機認證").equals("是")) {
                                phone_check_image.setImageResource(R.drawable.check);
                            }

                            email_check();
                        }
                    }
                });

    }

    private Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pwd_revise_btn:
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new Account_PWD_Revise(), null)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.FBAuth_btn:
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new Account_FBAuth(), null)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.EmailAuth_btn:
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new Account_EmailAuth(), null)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.PhoneAuth_btn:
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new Account_PhoneAuth(), null)
                            .addToBackStack(null)
                            .commit();
                    break;
            }
        }
    };


}
