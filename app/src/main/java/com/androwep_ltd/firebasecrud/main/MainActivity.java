package com.androwep_ltd.firebasecrud.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androwep_ltd.firebasecrud.R;
import com.androwep_ltd.firebasecrud.ViewLIstActivity.ListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    //View
    EditText mTitle, Descreption;
    Button mSaveBtn, mViewBtn;

    //prograss diaglog
    ProgressDialog pd;
    FirebaseFirestore db;
    //variable for get data from intent
    String pId,pTitle,pDespreption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");

        //variable for get data from intent




        mTitle = findViewById(R.id.titleEdit);
        Descreption = findViewById(R.id.desEdit);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //update data
            actionBar.setTitle("Update");
            //get data

            pId = bundle.getString("pId");
            pTitle = bundle.getString("pTitle");
            pDespreption = bundle.getString("pDescription");

            //set data
            mTitle.setText(pTitle);
            Descreption.setText(pDespreption);



        }else {
            //new Data
            actionBar.setTitle("Add Data");
        }



        //progress diaglog
        pd = new ProgressDialog(this);

        //firebase instance
        db = FirebaseFirestore.getInstance();
    }



    public void saveBtnClick(View view) {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){


            //updating
            String id = pId;
            String title = mTitle.getText().toString().trim();
            String description = Descreption.getText().toString().trim();
            //function call to update
            updateData(id,title,description);

        }else {
            //adding new
            String title = mTitle.getText().toString().trim();
            String description = Descreption.getText().toString().trim();

            uploadData(title,description);

        }


    }

    private void updateData(String id, String title, String description) {
        //set title of progress bar
        pd.setTitle("Uploading Data");
        //shoe progress bar when click
        pd.show();

        db.collection("document").document(id)
                .update("title", title, "search", title.toLowerCase(),"decription", description)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when update successfull
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,"Updated",Toast.LENGTH_LONG).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //called when update error
                pd.dismiss();
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }


    private void uploadData(String title, String description) {
        //set title of progress bar
        pd.setTitle("Uploading Data");
        //shoe progress bar when click
        pd.show();

        //random id for each data
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);//id of data
        doc.put("title", title);
        doc.put("search", title.toLowerCase());

        doc.put("decription", description);

        db.collection("document").document(id).set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //for data added successfully
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Upload Successfull", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //for any error
                pd.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void viewBtnClick(View view) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);

    }
}
