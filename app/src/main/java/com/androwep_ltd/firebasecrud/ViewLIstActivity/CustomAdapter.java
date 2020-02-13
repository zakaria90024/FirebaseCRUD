package com.androwep_ltd.firebasecrud.ViewLIstActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androwep_ltd.firebasecrud.Model.Model;
import com.androwep_ltd.firebasecrud.R;
import com.androwep_ltd.firebasecrud.main.MainActivity;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    //make paramitar constractor

    ListActivity listActivity;
    List<Model>modelList;


    public CustomAdapter(ListActivity listActivity, List<Model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //for passing/inflate layoute

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sample, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        //handle item click here

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //this will called when user clicked item

                //show data in toast in clicking
                String title = modelList.get(position).getTitle();
                String des = modelList.get(position).getDescription();

                Toast.makeText(listActivity, title+"\n"+des, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onItemLongClick(View view, final int position) {
                //this will called when user LONG clicked item

                //Creating AlartDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
                //option to display in dialog
                String[] option =  {"Update", "Delete"};

                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //update is called
                            String id = modelList.get(position).getId();
                            String title = modelList.get(position).getTitle();
                            String descreption = modelList.get(position).getDescription();

                            //Intent to start activity
                            Intent intent = new Intent(listActivity, MainActivity.class);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDescription", descreption);
                            //start activity
                            listActivity.startActivity(intent);
                        }
                        if (which == 1){
                            //delete is called
                            listActivity.deleteData(position);

                        }
                    }
                }).create().show();

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind view set data
        holder.mTitleTv.setText(modelList.get(position).getTitle());
        holder.mDescriptionTv.setText(modelList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
