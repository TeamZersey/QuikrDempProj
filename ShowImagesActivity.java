package com.example.firebaseauthgoogle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

//import android.support.v7.app.AppCompatActivity;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;*/

public class ShowImagesActivity extends AppCompatActivity {

    public String TAG ="ShowImagesActivity";

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private NewAdapter adapter;

    private NewAdapter adapter1;

    //database reference
    private DatabaseReference mDatabase;

    //database reference
    StorageReference mStorageReference;

    FirebaseStorage mFirebaseStorage;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;
    private List<String> s_upload;

    FirebaseFirestore db;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        setupUI();
        setupAdd();
        /*getdbdoc();*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        s_upload = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        getdbdoc();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child(Constants.STORAGE_PATH_UPLOADS);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload listData=postSnapshot.getValue(Upload.class);

                    uploads.add(listData);
                }
                int s = uploads.size();
                if(s == 0) {
                    System.out.println("size" + s);
                    Toast.makeText(ShowImagesActivity.this,"NO PRODUCTS TO DISPLAY", Toast.LENGTH_SHORT).show();
                    //dismissing the progress dialog
                    progressDialog.dismiss();
                }
                else {
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    adapter = new NewAdapter(ShowImagesActivity.this,uploads);
                    recyclerView.setAdapter(adapter);
                    //dismissing the progress dialog
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//dismissing the progress dialog
                progressDialog.dismiss();
                Toast.makeText(ShowImagesActivity.this,"NO PRODUCTS TO DISPLAY", Toast.LENGTH_SHORT).show();
            }
        });
        //getdbdoc();

    }
    public void getdbdoc()
    {
        db = FirebaseFirestore.getInstance();
        // Code here executes on main thread after user presses button
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().values());
                                System.out.println("show data "+document.getData().get("Title"));

                                //List<String> uploads = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.get("Title") != null) {
                                        Upload upload = new Upload(doc.getString("Title"));
                                        Upload listData1=doc.toObject(Upload.class);
                                        s_upload.add(doc.getString("Title"));
                                        s_upload.add(listData1);

                                        upload.getTitle();


                                    }


                                }
                                adapter1 = new NewAdapter(ShowImagesActivity.this,s_upload,"hello");


                                /*TextView txt = findViewById(R.id.tvtitleval);
                                txt.setText((CharSequence) document.getData().get("Title"));*/
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    /*db.collection("users").document()
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Upload u = documentSnapshot.toObject(Upload.class);
                muploads.add(u);
            }
        });*/
    }

    public void setupAdd() {
        final Button buttondadd= findViewById(R.id.add_button);
        buttondadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent I = new Intent(ShowImagesActivity.this, UploadImageActivity.class);
                startActivity(I);
            }
        });
    }


    public void setupUI() {
        final Button buttondadd= findViewById(R.id.sign_out_button);
        buttondadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();
            }
        });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent I = new Intent(ShowImagesActivity.this, SignInActivity.class);
        startActivity(I);
    }


}

