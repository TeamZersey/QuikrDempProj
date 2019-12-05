package com.example.firebaseauthgoogle

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException


class ImageMainActivity : AppCompatActivity() {

    //private val TAG = "ImageMainActivity"

    // Folder path for Firebase Storage.
    var Storage_Path = "All_Image_Uploads/"

    // Root Database Name for Firebase Database.
    var Database_Path = "All_Image_Uploads_Database"

    // Creating button.
    var ChooseButton: Button? = null
    var UploadButton: Button? = null// Creating button.


    // Creating EditText.
    var ImageName: EditText? = null

    // Creating ImageView.
    var SelectImage: ImageView? = null

    // Creating URI.
    var FilePathUri: Uri? = null

    // Creating StorageReference and DatabaseReference object.
    var storageReference: StorageReference? = null
    var databaseReference: DatabaseReference? = null

    // Image request code for onActivityResult() .
    var Image_Request_Code = 7

    var progressDialog: ProgressDialog? = null



    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, ImageMainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_main)



        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        //Assign ID'S to button.
       val ChooseButton = findViewById<Button>(R.id.ButtonChooseImage);


// Assign ID's to EditText.
        ImageName = findViewById<EditText>(R.id.ImageNameEditText);

// Assign ID'S to image view.
        SelectImage = findViewById<ImageView>(R.id.ShowImageView);

// Assigning Id to ProgressDialog.
        progressDialog = ProgressDialog(this);

        ChooseButton.setOnClickListener(){
            Toast.makeText(this,"button 1 clicked", Toast.LENGTH_SHORT).show()
            // Creating intent.
            // Creating intent.
            val intent = Intent()

            // Setting intent type as image to select image from phone storage.
            // Setting intent type as image to select image from phone storage.
            intent.type = "image/*"
            //intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Please Select Image"),
                Image_Request_Code
            )

        }
       val UploadButton = findViewById<Button>(R.id.ButtonUploadImage);
        UploadButton.setOnClickListener(){
            Toast.makeText(this,"button 2 clicked", Toast.LENGTH_SHORT).show()
            UploadImageFileToFirebaseStorage();
           // initFirestore();
        }

        val displayButton = findViewById<Button>(R.id.DisplayImagesButton);
        displayButton.setOnClickListener(){
            Toast.makeText(this,"button 3 clicked", Toast.LENGTH_SHORT).show()
           // startActivity(DisplayMainActivity.getLaunchIntent(this))
           // Intent intent = new Intent(this, DisplayMainActivity.class)
           // val intent = ImageMainActivity.newIntent(this, DisplayMainActivity.java)
            val intent = Intent(this, ShowImagesActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Image_Request_Code && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            FilePathUri = data.data
            try { // Getting selected image into Bitmap.
                val bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, FilePathUri)
                // Setting up bitmap selected image into ImageView.
                SelectImage!!.setImageBitmap(bitmap)
                // After selecting image change choose button above text.
                val ChooseButton = findViewById<Button>(R.id.ButtonChooseImage);

                ChooseButton!!.text = "Image Selected"
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    /* fun initFirestore() {
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
*/
    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    fun UploadImageFileToFirebaseStorage() { // Checking whether FilePathUri Is empty or not.



        if (FilePathUri != null) { // Setting progressDialog Title.
            progressDialog!!.setTitle("Image is Uploading...")
            // Showing progressDialog.
            progressDialog!!.show()
            // Creating second StorageReference.
           /* val storageReference2nd = storageReference!!.child(*/
            val storageReference2nd = storageReference!!.child(
                Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(
                    FilePathUri
                )
            )
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Getting image name from EditText and store into string variable.
                    val TempImageName =
                        ImageName!!.text.toString().trim { it <= ' ' }
                    // Hiding the progressDialog after done uploading.
                    progressDialog!!.dismiss()
                    // Showing toast message after done uploading.
                    Toast.makeText(
                        applicationContext,
                        "Image Uploaded Successfully ",
                        Toast.LENGTH_LONG
                    ).show()

                    val imageUploadInfo = ImageUploadInfo(TempImageName, taskSnapshot.getMetadata()?.getReference()?.getDownloadUrl().toString())
                    // Getting image upload ID.
                    val ImageUploadId = databaseReference!!.push().key
                    // Adding image upload id s child element into databaseReference.
                    databaseReference!!.child(ImageUploadId!!).setValue(imageUploadInfo)
                } // If something goes wrong .
                .addOnFailureListener { exception ->
                    // Hiding the progressDialog.
                    progressDialog!!.dismiss()
                    // Showing exception erro message.
                    Toast.makeText(this, exception.message, Toast.LENGTH_LONG)
                        .show()
                }
                // On progress change upload time.
                .addOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot?> {
                    fun onProgress(taskSnapshot: UploadTask.TaskSnapshot?) { // Setting progressDialog Title.
                        progressDialog!!.setTitle("Image is Uploading...")
                    }
                })
        } else {
            Toast.makeText(
                this,
                "Please Select Image or Add Image Name",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
