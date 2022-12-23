package com.example.datingapptest.personal_data_setting_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.HomePage;
import com.example.datingapptest.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Personal_Data_Resive_name_Fragment extends Fragment {

    private TextInputEditText text;
    private Button back,enter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_revise_name, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        text=(TextInputEditText)getView().findViewById(R.id.text) ;
        back=(Button)getView().findViewById(R.id.back);
        enter=(Button)getView().findViewById(R.id.enter);
        back.setOnClickListener(listener);
        enter.setOnClickListener(listener);

    }


    private Button.OnClickListener listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.enter:
                    FirebaseUser user = mAuth.getCurrentUser();
                    Map<String, Object> MESSAGE = new HashMap<>();
                    MESSAGE.put("姓名", text.getText().toString());
                    db.collection("personal_data")
                            .document(user.getUid())
                            .update(MESSAGE);
                    Intent intent1=new Intent();
                    intent1.setClass(Personal_Data_Resive_name_Fragment.this.getContext(),HomePage.class);
                    intent1.putExtra("id",1);
                    startActivity(intent1);
                    break;
                case R.id.back:
                    Intent intent=new Intent();
                    intent.setClass(Personal_Data_Resive_name_Fragment.this.getContext(),HomePage.class);
                    intent.putExtra("id",1);
                    startActivity(intent);
                    break;
            }
        }
    };






}
