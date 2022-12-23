package com.example.datingapptest.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.R;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Resive_name_Fragment;

import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_Age_Fragment;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_Birthday_Fragment;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_Sex_Fragment;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_email_Fragment;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_hobby_Fragment;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_home_Fragment;
import com.example.datingapptest.personal_data_setting_activity.Personal_Data_Revise_phone_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonalDataFragment extends Fragment {
    private ListView lv;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_personaldata, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadData();
    }

    public void LoadData() {
        final ProgressDialog progressDialog = new ProgressDialog(PersonalDataFragment.this.getContext());
        progressDialog.setTitle("載入中...");
        progressDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("personal_data")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String name = task.getResult().getString("姓名");
                            String email = task.getResult().getString("信箱");
                            String sex = task.getResult().getString("性別");
                            String home = task.getResult().getString("聯絡地址");
                            String age;
                            String birthday;
                            String phone;
                            String phoneAuth;
                            String fbAuth;
                            String emailAuth;
                            String hobby="點擊查看";
                            if (task.getResult().getString("年齡") == "") {
                                age = "尚未設置";
                            } else {
                                age = task.getResult().getString("年齡");
                            }
                            if (task.getResult().getString("生日") == "") {
                                birthday= "尚未設置";
                            } else {
                                birthday = task.getResult().getString("生日");
                            }

                            if (task.getResult().getString("手機") == "") {
                                phone = "尚未設置";
                            } else {
                                phone = task.getResult().getString("手機");
                            }

                            if (task.getResult().getString("手機認證") == "") {
                                phoneAuth = "尚未設置";
                            } else {
                                phoneAuth = task.getResult().getString("手機認證");
                            }

                            if (task.getResult().getString("臉書認證") == "") {
                                fbAuth = "尚未設置";
                            } else {
                                fbAuth = task.getResult().getString("臉書認證");
                            }

                            if (task.getResult().getString("email認證") == "") {
                                emailAuth = "尚未設置";
                            } else {
                                emailAuth = task.getResult().getString("手機認證");
                            }

                            String[] text = new String[]{name, sex,birthday, age, email, phone, home, phoneAuth, emailAuth, fbAuth,hobby};
                            lv = (ListView) getView().findViewById(R.id.lv);
                            lv.setOnItemClickListener(listener);
                            List<HashMap<String, String>> list = new ArrayList<>();
                            String[] title = new String[]{"姓名", "性別", "生日", "年齡", "信箱", "手機", "聯絡地址", "手機認證", "信箱認證", "臉書認證","興趣"};
                            for (int i = 0; i < title.length; i++) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("title", title[i]);
                                hashMap.put("text", text[i]);
                                //把title , text存入HashMap之中
                                list.add(hashMap);
                                //把HashMap存入list之中
                            }
                            SimpleAdapter listAdapter = new SimpleAdapter(
                                    PersonalDataFragment.this.getContext(),
                                    list,
                                    android.R.layout.simple_list_item_2,
                                    new String[]{"title", "text"},
                                    new int[]{android.R.id.text1, android.R.id.text2});
                            // 5個參數 : context , List , layout , key1 & key2 , text1 & text2

                            lv.setAdapter(listAdapter);
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Resive_name_Fragment(), null)
                        .addToBackStack(null)
                        .commit();

            } else if (position == 1) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_Sex_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            } else if (position == 2) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_Birthday_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            } else if (position == 3) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_Age_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            } else if (position == 4) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_email_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            } else if (position == 5) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_phone_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            } else if (position == 6) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_home_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            }  else if (position == 10) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Personal_Data_Revise_hobby_Fragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        }


    };

}
