package com.example.firebaseauthgoogle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class DisplayMainActivity extends AppCompatActivity {


    private static final String TAG = "DisplayMainActivity" ;
    // Folder path for Firebase Storage.
   public String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    public String Database_Path = "All_Image_Uploads_Database";

    // Creating StorageReference and DatabaseReference object.
    StorageReference StorageReference;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_main);

            // Assign id to RecyclerView.
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            // Setting RecyclerView size true.
            recyclerView.setHasFixedSize(true);

            // Setting RecyclerView layout as LinearLayout.
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Assign activity this to progress dialog.
            progressDialog = new ProgressDialog(this);

            // Setting up message in Progress dialog.
            progressDialog.setMessage("Loading Images From Firebase.");

            // Showing progress dialog.
            progressDialog.show();

            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");

            myRef.setValue("Hello, World!");

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            // Setting up Firebase image upload folder path in databaseReference.
            // The path is already defined in MainActivity.
            StorageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS);

            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

            //adding an event listener to fetch values
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //dismissing the progress dialog
                    progressDialog.dismiss();

                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        ImageUploadInfo upload = postSnapshot.getValue(ImageUploadInfo.class);
                        list.add(upload);
                    }
                    //creating adapter
                    adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                    //adding adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            System.out.println("Exception "+ e.getMessage());
        }
    }
}
