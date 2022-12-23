package com.example.datingapptest.personal_data_setting_activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.datingapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kongzue.stacklabelview.StackLabel;
import com.kongzue.stacklabelview.interfaces.OnLabelClickListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Personal_Data_Revise_hobby_Fragment extends Fragment {
    private StackLabel stackLabel;
    private Spinner spin_type, spin_item;
    private Button btn_habbit;
    private EditText enter_habbit;
    List<String> choosed_outdoor = new ArrayList<String>();
    List<String> choosed_indoor = new ArrayList<String>();
    List<String> choosed_job = new ArrayList<String>();
    List<String> choosed_country = new ArrayList<String>();
    List<String> cus_enter = new ArrayList<String>();
    final String[] outdoor_sport = new String[]{"無", "腳踏車", "登山", "爬山", "滑雪", "騎馬", "跳舞", "足球", "籃球", "棒球", "羽毛球",
            "高爾夫球", "美式足球", "網球", "排球", "桌球", "游泳", "拳擊", "重訓", "馬拉松", "慢跑", "瑜珈"};
    final String[] job = new String[]{"無", "農業", "林業", "漁業", "牧業", "採礦", "採石", "製造業", "公營事業", "建築業", "汽車維修業", "飯店業", "學生"};
    final String[] country = new String[]{"無", "美國", "日本", "台灣"};
    final String[] indoor_sport = new String[]{"無", "看書", "聽音樂", "攝影", "看電影", "彈奏樂器", "觀賞音樂會", "觀賞歌劇", "喝下午茶", "棋藝", "手工藝", "雕刻",
            "瑜珈", "泥塑", "寫作", "畫畫", "釣魚", "栽花種樹", "插花", "烹調"};
    final String[] type = new String[]{"無", "室外運動", "室內運動", "去過國家", "工作"};
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_revise_hobby, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stackLabel = (StackLabel) getView().findViewById(R.id.stackLabelView);
        spin_item = (Spinner) getView().findViewById(R.id.spin_item2);
        spin_type = (Spinner) getView().findViewById(R.id.spin_type2);
        btn_habbit = (Button) getView().findViewById(R.id.btn_habbit2);
        enter_habbit = (EditText) getView().findViewById(R.id.enter_habbit2);

        load_habbit();
        HabbitChoose();
//        stackLabel.setDeleteButton(true);
    }

    @Override
    public void onPause() {
        super.onPause();


        Map<String, Object> MESSAGE = new HashMap<>();
        MESSAGE.put("室內運動", StringUtils.strip(choosed_indoor.toString(), "[]"));
        MESSAGE.put("室外運動", StringUtils.strip(choosed_outdoor.toString(), "[]"));
        MESSAGE.put("工作", StringUtils.strip(choosed_job.toString(), "[]"));
        MESSAGE.put("去過國家", StringUtils.strip(choosed_country.toString(), "[]"));
        MESSAGE.put("其他", StringUtils.strip(cus_enter.toString(), "[]"));
        db.collection("habbit")
                .document(user.getUid())
                .update(MESSAGE);
    }

    private void load_habbit() {


        db.collection("habbit")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getString("其他") != "") {
                                String[] split_line = task.getResult().getString("其他").split(",");
                                for (String s : split_line) {
                                    stackLabel.addLabel("#" + s);
                                    cus_enter.add(s);
                                }
                                stackLabel.setDeleteButton(true);
                            }
                            if (task.getResult().getString("去過國家") != "") {
                                String[] split_line = task.getResult().getString("去過國家").split(",");
                                for (String s : split_line) {
                                    stackLabel.addLabel("#" + s);
                                    choosed_country.add(s);
                                }
                                stackLabel.setDeleteButton(true);

                            }
                            if (task.getResult().getString("室內運動") != "") {
                                String[] split_line = task.getResult().getString("室內運動").split(",");
                                for (String s : split_line) {
                                    stackLabel.addLabel("#" + s);
                                    choosed_indoor.add(s);
                                }
                                stackLabel.setDeleteButton(true);
                            }
                            if (task.getResult().getString("室外運動") != "") {
                                String[] split_line = task.getResult().getString("室外運動").split(",");
                                for (String s : split_line) {
                                    stackLabel.addLabel("#" + s);
                                    choosed_outdoor.add(s);
                                }
                                stackLabel.setDeleteButton(true);
                            }
                            if (task.getResult().getString("工作") != "") {
                                String[] split_line = task.getResult().getString("工作").split(",");
                                for (String s : split_line) {
                                    stackLabel.addLabel("#" + s);
                                    choosed_job.add(s);
                                }
                                stackLabel.setDeleteButton(true);
                            }
                        }
                    }
                });

        stackLabel.setOnLabelClickListener(new OnLabelClickListener() {
            @Override
            public void onClick(int index, View v, String s) {
                stackLabel.remove(index);

                if (cus_enter.contains(StringUtils.strip(s, "#"))) {
                    cus_enter.remove(StringUtils.strip(s, "#"));

                } else if (choosed_country.contains(StringUtils.strip(s, "#"))) {
                    choosed_country.remove(StringUtils.strip(s, "#"));

                } else if (choosed_job.contains(StringUtils.strip(s, "#"))) {
                    choosed_job.remove(StringUtils.strip(s, "#"));
                } else if (choosed_outdoor.contains(StringUtils.strip(s, "#"))) {
                    choosed_outdoor.remove(StringUtils.strip(s, "#"));
                } else if (choosed_indoor.contains(StringUtils.strip(s, "#"))) {
                    choosed_indoor.remove(StringUtils.strip(s, "#"));
                }
            }
        });
    }

    private void HabbitChoose() {


        final ArrayAdapter<String> adaptertype = new ArrayAdapter<String>(Personal_Data_Revise_hobby_Fragment.this.getContext(), android.R.layout.simple_spinner_item, type);
        adaptertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_type.setAdapter(adaptertype);

        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString() == "室外運動") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(Personal_Data_Revise_hobby_Fragment.this.getContext(), android.R.layout.simple_spinner_item, outdoor_sport);
                    adapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_item.setAdapter(adapteritem);
                } else if (parent.getSelectedItem().toString() == "室內運動") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(Personal_Data_Revise_hobby_Fragment.this.getContext(), android.R.layout.simple_spinner_item, indoor_sport);
                    adapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_item.setAdapter(adapteritem);
                } else if (parent.getSelectedItem().toString() == "去過國家") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(Personal_Data_Revise_hobby_Fragment.this.getContext(), android.R.layout.simple_spinner_item, country);
                    adapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_item.setAdapter(adapteritem);
                } else if (parent.getSelectedItem().toString() == "工作") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(Personal_Data_Revise_hobby_Fragment.this.getContext(), android.R.layout.simple_spinner_item, job);
                    adapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_item.setAdapter(adapteritem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString() != "無") {
                    if (spin_type.getSelectedItem().toString() == "室外運動") {
                        stackLabel.addLabel("#" + parent.getSelectedItem().toString());
                        choosed_outdoor.add(parent.getSelectedItem().toString());

                    } else if (spin_type.getSelectedItem().toString() == "室內運動") {
                        stackLabel.addLabel("#" + parent.getSelectedItem().toString());
                        choosed_indoor.add(parent.getSelectedItem().toString());

                    } else if (spin_type.getSelectedItem().toString() == "工作") {
                        stackLabel.addLabel("#" + parent.getSelectedItem().toString());
                        choosed_job.add(parent.getSelectedItem().toString());

                    } else if (spin_type.getSelectedItem().toString() == "去過國家") {
                        stackLabel.addLabel("#" + parent.getSelectedItem().toString());
                        choosed_country.add(parent.getSelectedItem().toString());
                    }

                    stackLabel.setDeleteButton(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_habbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enter_habbit.getText().toString() != null) {
                    stackLabel.addLabel("#" + enter_habbit.getText().toString());
                    cus_enter.add(enter_habbit.getText().toString());
                    stackLabel.setDeleteButton(true);
                    enter_habbit.setText("");
                }
            }
        });

    }
}
