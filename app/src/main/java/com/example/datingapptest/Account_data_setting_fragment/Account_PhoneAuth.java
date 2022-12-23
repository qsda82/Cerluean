package com.example.datingapptest.Account_data_setting_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.Fragment.AccountFragment;
import com.example.datingapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class Account_PhoneAuth extends Fragment {
    private Button back, enter, ver_btn;
    private TextInputEditText text;
    private EditText ver_num;
    private String codesent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account_phoneauth, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        back = (Button) getView().findViewById(R.id.back);
        enter = (Button) getView().findViewById(R.id.enter);
        text = (TextInputEditText) getView().findViewById(R.id.text);
        ver_btn = (Button) getView().findViewById(R.id.ver_btn);
        ver_num = (EditText) getView().findViewById(R.id.ver_num);
        vertification_codesend();
        vertication();
    }

    private void vertication() {
        ver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = ver_num.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider . getCredential ( codesent , code );
                if(code==credential.getSmsCode() ){
                    Toast.makeText(Account_PhoneAuth.this.getContext(), "認證成功"
                            , Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Map<String, Object> MESSAGE = new HashMap<>();
                    MESSAGE.put("手機認證","是");
                    db.collection("personal_data")
                            .document(user.getUid())
                            .update(MESSAGE);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new AccountFragment(), null)
                            .addToBackStack(null)
                            .commit();
                }else {
                    Toast.makeText(Account_PhoneAuth.this.getContext(), "認證失敗"
                            , Toast.LENGTH_SHORT).show();
                    ver_num.setText("");
                }
            }
        });

    }

    private void vertification_codesend() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+886" + text.getText().toString().substring(1), // Phone number to verify
                        60, // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        Account_PhoneAuth.this.getActivity(), // Activity (for callback binding)
                        mCallbacks); // OnVerificationStateChangedCallbacks
                Toast.makeText(Account_PhoneAuth.this.getContext(), "已發送簡訊"
                        , Toast.LENGTH_SHORT).show();
            }
        });


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Account_PhoneAuth.this.getContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent = s;

        }
    };
}
