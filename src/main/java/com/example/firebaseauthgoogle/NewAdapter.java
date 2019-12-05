package com.example.firebaseauthgoogle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
    private List<Upload>list_data;
    private Context ct,contxt;
    String mg;

    private List<String> l_data;

    public NewAdapter(Context ct,List<Upload> list_data) {
        this.ct = ct;
        this.list_data = list_data;

    }
    public NewAdapter(Context contxt,List<String> l_data,String mg) {
        this.contxt = contxt;
        this.l_data = l_data;
        this.mg = mg;

    }

    FirebaseFirestore db;
    public String tval;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ct).inflate(R.layout.layout_images,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        /*db = FirebaseFirestore.getInstance();
        // Code here executes on main thread after user presses button
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                System.out.println("show data "+document.getData().get("Title"));

                                tval = document.getData().get("Title").toString();
                               *//*TextView txt = (TextView)itemView.findViewById(R.id.tvtitleval);
                                txt.setText((CharSequence) document.getData().get("Title"));*//*
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/

        Upload ld=list_data.get(position);
        //Upload sdata=l_data.get(position);
        System.out.println("uploaded title" + l_data.get(position) + l_data.size());
        holder.txt.setText(ld.getTitle());
        holder.tvname.setText(ld.getName());
        Glide.with(ct)
                .load(ld.getUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {  // <--- here
            @Override
            public void onClick(View v) {
                Log.i("W4K","Click-"+ position);
                ct.startActivity(new Intent(ct,UploadImageActivity.class));  // <--- here
            }
        });
    }

    /*Picasso.placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()*/
    @Override
    public int getItemCount() {
        return list_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView tvname,txt;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            tvname=(TextView)itemView.findViewById(R.id.textViewName);
             txt = (TextView)itemView.findViewById(R.id.tvtitleval);

        }
    }

}
