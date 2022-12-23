package com.example.datingapptest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kongzue.stacklabelview.StackLabel;
import com.kongzue.stacklabelview.interfaces.OnLabelClickListener;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class register_page extends AppCompatActivity {
    private Button btn_uploadphoto, btn_habbit, btn_sign;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private EditText enter_account,  enter_sex, enter_home, enter_habbit;
    private TextInputLayout enter_password;
    private TextInputEditText text;
    private StackLabel stackLabel;
    private Spinner spin_type, spin_item;
    List<String> choosed_outdoor = new ArrayList<String>();
    List<String> choosed_indoor = new ArrayList<String>();
    List<String> choosed_job = new ArrayList<String>();
    List<String> choosed_country = new ArrayList<String>();
    List<String> cus_enter = new ArrayList<String>();
    //firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btn_habbit = (Button) findViewById(R.id.btn_habbit);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        btn_uploadphoto = (Button) findViewById(R.id.btn_uploadphoto);
        imageView = (ImageView) findViewById(R.id.imageView);
        enter_account = (EditText) findViewById(R.id.enter_account);
        enter_password = (TextInputLayout) findViewById(R.id.enter_password);
        enter_home = (EditText) findViewById(R.id.enter_home);
        enter_sex = (EditText) findViewById(R.id.enter_sex);
        enter_habbit = (EditText) findViewById(R.id.enter_habbit);
        stackLabel = (StackLabel) findViewById(R.id.stackLabelView);

        spin_item = (Spinner) findViewById(R.id.spin_item);
        spin_type = (Spinner) findViewById(R.id.spin_type);
        text=(TextInputEditText)findViewById(R.id.text) ;
        btn_sign.setOnClickListener(listener);
        PhotoUpload();
        HabbitChoose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private  void RegisterAction(){

        mAuth.createUserWithEmailAndPassword(enter_account.getText().toString(), text.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String account = enter_account.getText().toString();
                            Toast.makeText(register_page.this, "註冊成功",
                                    Toast.LENGTH_SHORT).show();
                            login(enter_account.getText().toString(),text.getText().toString());
                            HabbitUpload();
                            RegisterUpload(account);
                            Intent intent=new Intent();
                            intent.setClass(register_page.this,HomePage.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(register_page.this, "註冊失敗"+task.getException(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password);

    }
    private void PhotoUpload() {

        //按下照片上傳按鈕
        btn_uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selete Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void HabbitChoose() {
        final String[] outdoor_sport = new String[]{"無", "腳踏車", "登山", "爬山", "滑雪", "騎馬", "跳舞", "足球", "籃球", "棒球", "羽毛球",
                "高爾夫球", "美式足球", "網球", "排球", "桌球", "游泳", "拳擊", "重訓", "馬拉松", "慢跑", "瑜珈"};
        final String[] job = new String[]{"無", "農業", "林業", "漁業", "牧業", "採礦", "採石", "製造業", "公營事業", "建築業", "汽車維修業", "飯店業", "學生"};
        final String[] country = new String[]{"無", "美國", "日本", "台灣"};
        final String[] indoor_sport = new String[]{"無", "看書", "聽音樂", "攝影", "看電影", "彈奏樂器", "觀賞音樂會", "觀賞歌劇", "喝下午茶", "棋藝", "手工藝", "雕刻",
                "瑜珈", "泥塑", "寫作", "畫畫", "釣魚", "栽花種樹", "插花", "烹調"};
        final String[] type = new String[]{"無", "室外運動", "室內運動", "去過國家", "工作"};

        final ArrayAdapter<String> adaptertype = new ArrayAdapter<String>(register_page.this, android.R.layout.simple_spinner_item, type);
        adaptertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_type.setAdapter(adaptertype);

        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString() == "室外運動") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(register_page.this, android.R.layout.simple_spinner_item, outdoor_sport);
                    adapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_item.setAdapter(adapteritem);
                } else if (parent.getSelectedItem().toString() == "室內運動") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(register_page.this, android.R.layout.simple_spinner_item, indoor_sport);
                    adapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_item.setAdapter(adapteritem);
                } else if (parent.getSelectedItem().toString() == "去過國家") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(register_page.this, android.R.layout.simple_spinner_item, country);
                    adapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_item.setAdapter(adapteritem);
                } else if (parent.getSelectedItem().toString() == "工作") {
                    ArrayAdapter<String> adapteritem = new ArrayAdapter<String>(register_page.this, android.R.layout.simple_spinner_item, job);
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
                    if(spin_type.getSelectedItem().toString()=="室外運動"){
                        stackLabel.addLabel("#" + parent.getSelectedItem().toString());
                        choosed_outdoor.add(parent.getSelectedItem().toString());
                    }else if(spin_type.getSelectedItem().toString()=="室內運動"){
                        stackLabel.addLabel("#" + parent.getSelectedItem().toString());
                        choosed_indoor.add(parent.getSelectedItem().toString());
                    }else if(spin_type.getSelectedItem().toString()=="工作"){
                        stackLabel.addLabel("#" + parent.getSelectedItem().toString());
                        choosed_job.add(parent.getSelectedItem().toString());
                    }else if(spin_type.getSelectedItem().toString()=="去過國家"){
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
        stackLabel.setOnLabelClickListener(new OnLabelClickListener() {
            @Override
            public void onClick(int index, View v, String s) {
                //是否开启了删除模式
                //删除并重新设置标签
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

    private void HabbitUpload() {
        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> MESSAGE = new HashMap<>();
        MESSAGE.put("室內運動", StringUtils.strip(choosed_indoor.toString(),"[]"));
        MESSAGE.put("室外運動", StringUtils.strip(choosed_outdoor.toString(),"[]"));
        MESSAGE.put("工作",StringUtils.strip( choosed_job.toString(),"[]"));
        MESSAGE.put("去過國家", StringUtils.strip(choosed_country.toString(),"[]"));
        MESSAGE.put("其他", StringUtils.strip(cus_enter.toString(),"[]"));
        db.collection("habbit")
                .document(user.getUid())
                .set(MESSAGE);
    }

    private void RegisterUpload(String account) {
        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> MESSAGE = new HashMap<>();
        MESSAGE.put("uid", user.getUid());
        MESSAGE.put("姓名", account);
        MESSAGE.put("性別", enter_sex.getText().toString());
        MESSAGE.put("生日", "");
        MESSAGE.put("年齡", "");
        MESSAGE.put("信箱", account);
        MESSAGE.put("手機", "");
        MESSAGE.put("照片", "gs://android-5fcb6.appspot.com/image/" + user.getUid() );
        MESSAGE.put("聯絡地址", enter_home.getText().toString());
        MESSAGE.put("臉書認證", "否");
        MESSAGE.put("email認證", "否");
        MESSAGE.put("手機認證", "否");

        db.collection("personal_data")
                .document(user.getUid())
                .set(MESSAGE);
    }

    private Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(register_page.this);
                progressDialog.setTitle("上傳...");
                progressDialog.show();

                FirebaseUser user = mAuth.getCurrentUser();
                StorageReference ref = storageReference.child("image/" + user.getUid());
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("登錄" + (int) progress + "%");
                    }
                });

            }
            RegisterAction();
        }
    };
}
