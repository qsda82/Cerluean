package com.example.datingapptest.personal_data_setting_activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.HomePage;
import com.example.datingapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Personal_Data_Revise_Birthday_Fragment extends Fragment {
    private TextView tv_birthday_set;
    private Button enter;
    int birth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_revise_birthday, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_birthday_set = (TextView) getView().findViewById(R.id.tv_birthday_set);
        enter=(Button)getView().findViewById(R.id.enter);
        dialogDate();
        enter();
    }

    private void enter(){
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (birth <= 0) {
                    Toast.makeText(Personal_Data_Revise_Birthday_Fragment.this.getContext(), "生日輸入有誤", Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Map<String, Object> MESSAGE = new HashMap<>();
                    MESSAGE.put("年齡", String.valueOf(birth));
                    MESSAGE.put("生日", tv_birthday_set.getText().toString().substring(3));
                    db.collection("personal_data")
                            .document(user.getUid())
                            .update(MESSAGE);
                    Intent intent=new Intent();
                    intent.setClass(Personal_Data_Revise_Birthday_Fragment.this.getContext(), HomePage.class);
                    intent.putExtra("id",1);
                    startActivity(intent);

                }
            }
        });
    }
    private void dialogDate() {
        tv_birthday_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Personal_Data_Revise_Birthday_Fragment.this.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofYear, int dayOfMonth) {
                        //顯示生日（月份要+1，因為這個方法是從0開始算的）
                        tv_birthday_set.setText("生日："+String.format("%d/%d/%d", year, monthofYear + 1, dayOfMonth));

                        Calendar cal = Calendar.getInstance();
                        String strDate = year + "-" + monthofYear + "-" + dayOfMonth;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date birthDay = null;
                        try {
                            birthDay = sdf.parse(strDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        birth = countAge(birthDay);
                    }
                    //設定初始的顯示日期
                }, 2000, 0, 1).show();
            }
        });
    }
    private int countAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        //獲得當前日期
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        //獲得出生日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;

    }
}
    
    

