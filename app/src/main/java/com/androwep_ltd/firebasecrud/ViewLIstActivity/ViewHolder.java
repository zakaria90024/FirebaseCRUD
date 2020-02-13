package com.androwep_ltd.firebasecrud.ViewLIstActivity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androwep_ltd.firebasecrud.R;

public class ViewHolder extends RecyclerView.ViewHolder{
    TextView mTitleTv, mDescriptionTv;

    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });

        //For long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return true;
            }
        });
        //initialize view with model_layout.xml

        mTitleTv = itemView.findViewById(R.id.titleTextItem);
        mDescriptionTv = itemView.findViewById(R.id.destextItem);

    }

    private ViewHolder.ClickListener mClickListener;

    //interface for click lisner
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void  setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
