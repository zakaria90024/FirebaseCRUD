package com.androwep_ltd.firebasecrud.ViewLIstActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androwep_ltd.firebasecrud.Model.Model;
import com.androwep_ltd.firebasecrud.R;
import com.androwep_ltd.firebasecrud.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;

    //layout manager for recyclerview
    RecyclerView.LayoutManager layoutManager;
    //Firebase instance
    FirebaseFirestore db;
    CustomAdapter adapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //initialize firestore
        db = FirebaseFirestore.getInstance();

        //initialize view
        mRecyclerView = findViewById(R.id.recycler_id);

        //progress diaglog
        pd = new ProgressDialog(this);

        //set recycler view propertis
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //for show data in recycler
        showData();
    }

    private void showData() {
        //set title progress diaglog
        pd.setTitle("Loading Data...");
        //for show progress diaglog
        pd.show();

        db.collection("document").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                modelList.clear();
             //called when data is retrieved
                pd.dismiss();
                //show data
                for (DocumentSnapshot doc:task.getResult()){
                    Model model = new Model(doc.getString("id"),
                            doc.getString("title"),
                            doc.getString("decription"));
                    modelList.add(model);
                }

                //adapter
                adapter = new CustomAdapter(ListActivity.this, modelList);
                //set adapter to recycler
                mRecyclerView.setAdapter(adapter);

                Toast.makeText(ListActivity.this, "Success",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //called when data is error retrieved
                pd.dismiss();
                Toast.makeText(ListActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    public void insertBtnClick(View view) {
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void deleteData(int index){
        //set title progress diaglog
        pd.setTitle("Deleting Data...");
        //for show progress diaglog
        pd.show();

        db.collection("document").document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this,"Delete Successfull",Toast.LENGTH_LONG).show();
                        showData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ListActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                showData();
            }
        });
    }






    //for menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //influte menu main xml
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //called when we press search button
                serchData(query);//function call with string entered searchview as parameter

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called as and when type a single latter
                return false;
            }
        });

        return true;
    }

    private void serchData(String query) {

        //set title of progress bar
        pd.setTitle("Searching....");
        //shoe progress bar when click
        pd.show();

        db.collection("document").whereEqualTo("search", query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //called when search is success
                        modelList.clear();
                        pd.dismiss();

                        for (DocumentSnapshot doc:task.getResult()){
                            Model model = new Model(doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("decription"));
                            modelList.add(model);
                        }

                        //adapter
                        adapter = new CustomAdapter(ListActivity.this, modelList);
                        //set adapter to recycler
                        mRecyclerView.setAdapter(adapter);

                        Toast.makeText(ListActivity.this, "Success",Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //called when search is failed
                pd.dismiss();
                Toast.makeText(ListActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle other menu item click here
        if (item.getItemId() == R.id.action_settings){
            showData();
            //Toast.makeText(ListActivity.this, "setting", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
