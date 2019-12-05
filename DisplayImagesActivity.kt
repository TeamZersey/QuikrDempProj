package com.example.firebaseauthgoogle

//import android.R

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class DisplayImagesActivity : AppCompatActivity()
{

        companion object {
        fun getLaunchIntent(from: Context) = Intent(from, DisplayImagesActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

        // Root Database Name for Firebase Database.
        var Database_Path = "All_Image_Uploads/"

        // Creating DatabaseReference.
        var databaseReference: DatabaseReference? = null

        // Creating RecyclerView.
        val recyclerView: RecyclerView? = null

        // Creating RecyclerView.Adapter.
        var adapter: RecyclerView.Adapter<*>? = null

        // Creating Progress dialog
        var progressDialog: ProgressDialog? = null

        // Creating List of ImageUploadInfo class.
        var list: List<ImageUploadInfo> = ArrayList()

        override fun onCreate(savedInstanceState: Bundle?) {

            try {


                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_display_images)

                // Assign id to RecyclerView.
                // Assign id to RecyclerView.
                // val buttonChoose = findViewById<Button>(R.id.buttonChoose);
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView

                // Setting RecyclerView size true.
                // Setting RecyclerView size true.
                recyclerView!!.setHasFixedSize(true)

                // Setting RecyclerView layout as LinearLayout.
                // Setting RecyclerView layout as LinearLayout.
                recyclerView!!.layoutManager = LinearLayoutManager(this@DisplayImagesActivity)

                // Assign activity this to progress dialog.
                // Assign activity this to progress dialog.
                progressDialog = ProgressDialog(this@DisplayImagesActivity)

                // Setting up message in Progress dialog.
                // Setting up message in Progress dialog.
                progressDialog!!.setMessage("Loading Images From Firebase.")

                // Showing progress dialog.
                // Showing progress dialog.
                progressDialog!!.show()

                // Setting up Firebase image upload folder path in databaseReference.
                // The path is already defined in MainActivity.
                // Setting up Firebase image upload folder path in databaseReference.
// The path is already defined in MainActivity.
                val databaseReference =
                    FirebaseStorage.getInstance().getReference(Database_Path + "/");
                // databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path)


                try {
                    // Adding Add Value Event Listener to databaseReference.
                    databaseReference!!.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (postSnapshot in snapshot.getChildren()) {
                                val imageUploadInfo: ImageUploadInfo? =
                                    postSnapshot.getValue(ImageUploadInfo::class.java)
                                list.add(imageUploadInfo)
                            }
                            adapter = RecyclerViewAdapter(applicationContext, list)
                            recyclerView!!.adapter = adapter
                            // Hiding the progress dialog.
                            progressDialog!!.dismiss()
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            progressDialog!!.dismiss()

                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                        /*fun onCancelled(databaseError: DatabaseError?) { // Hiding the progress dialog.
                progressDialog!!.dismiss()
            }*/
                    })
                } catch (e: Exception) {
                    System.out.println("Error DisplayImagesActivity" + e.message);
                    Toast.makeText(this, "Error" + e.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                System.out.println("Error Null DisplayImagesActivity" + e.message);
            }

        }


}

private fun StorageReference.addValueEventListener(valueEventListener: ValueEventListener) {

}

private fun DatabaseReference.addValueEventListener(valueEventListener: ValueEventListener) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

private fun <E> List<E>.add(imageUploadInfo: E?) {

}
