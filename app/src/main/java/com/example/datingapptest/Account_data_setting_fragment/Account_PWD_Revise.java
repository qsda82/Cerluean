package com.example.datingapptest.Account_data_setting_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.Fragment.AccountFragment;
import com.example.datingapptest.MainActivity;
import com.example.datingapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Account_PWD_Revise extends Fragment {
    private Button back, enter;
    private TextInputEditText text;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account_revise_pwd, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        back = (Button) getView().findViewById(R.id.back);
        enter = (Button) getView().findViewById(R.id.enter);
        text = (TextInputEditText) getView().findViewById(R.id.text);
        pwd_revise();
    }

    private void pwd_revise() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AccountFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().isEmpty()) {
                    Toast.makeText(Account_PWD_Revise.this.getContext(), "請輸入信箱",
                            Toast.LENGTH_SHORT).show();
                } else {
                    final String email = text.getText().toString();
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Account_PWD_Revise.this.getContext(), "已寄送信件至信箱",
                                                Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new AccountFragment(), null)
                                                .addToBackStack(null)
                                                .commit();
                                    } else {
                                        Toast.makeText(Account_PWD_Revise.this.getContext(), task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
