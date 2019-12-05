package com.example.firebaseauthgoogle

/*import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData*/
/*import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData*/

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


class ProductMainActivity : AppCompatActivity(){
//class ProductMainActivity : AppCompatActivity(), View.OnClickListener {
   /* override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    //constant to track image chooser intent
    private val PICK_IMAGE_REQUEST = 234

    //view objects
    private val buttonChoose: Button? = null
    private val buttonUpload: Button? = null
    private val editTextName: EditText? = null
    private val textViewShow: TextView? = null
    private val imageView: ImageView? = null

    //uri to store file
    private var filePath: Uri? = null

    //firebase objects
    private val storageReference: StorageReference? = null
    private val mDatabase: DatabaseReference? = null

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, ProductMainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_main)

        /*buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        editTextName = (EditText) findViewById(R.id.editText);
        textViewShow = (TextView) findViewById(R.id.textViewShow);*/

        val buttonChoose = findViewById<Button>(R.id.buttonChoose);
        val buttonUpload =  findViewById<Button>(R.id.buttonUpload);
        val imageView =  findViewById<ImageView>(R.id.imageView);
        val editTextName = findViewById<EditText>(R.id.editText);
        val textViewShow = findViewById<TextView>(R.id.textViewShow)

        buttonChoose.setOnClickListener(){
            Toast.makeText(this,"button 1 clicked", Toast.LENGTH_SHORT).show()
            showFileChooser();
        }

        buttonUpload.setOnClickListener(){
            Toast.makeText(this,"button 2 clicked", Toast.LENGTH_SHORT).show()
            uploadFile();
        }

        val storageReference = FirebaseStorage.getInstance().getReference();
        val mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        /*buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        textViewShow.setOnClickListener(this);*/
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val imageView =  findViewById<ImageView>(R.id.imageView);
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    fun getFileExtension(uri: Uri?): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }

   /* Method uploadFile() that will upload the selected file to Firebase Storage*/

    private fun uploadFile() { //checking if file is available
        try {
        if (filePath != null) { //displaying progress dialog while image is uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading")
            progressDialog.show()
            //getting the storage reference
            val storageReference = FirebaseStorage.getInstance().getReference();
            val sRef = storageReference!!.child(
                Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(
                    filePath
                )
            )
            //adding the file to reference
            sRef.putFile(filePath!!)
                .addOnSuccessListener { taskSnapshot ->
                    //dismissing the progress dialog
                    progressDialog.dismiss()
                    //displaying success toast
                    Toast.makeText(applicationContext, "File Uploaded ", Toast.LENGTH_LONG)
                        .show()
                    //creating the upload object to store uploaded image details

                        val editTextName = findViewById<EditText>(R.id.editText);
                        val upload = Upload(
                            editTextName.text.toString().trim { it <= ' ' },
                            //taskSnapshot.getDownloadUrl().toString()
                            taskSnapshot.getMetadata()?.getReference()?.getDownloadUrl().toString()
                        )

                    //adding an upload to firebase database
                    val uploadId = mDatabase!!.push().key
                    mDatabase.child(uploadId!!).setValue(upload)
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                    System.out.println("Exception msg" +  exception.message);
                }
                .addOnProgressListener { taskSnapshot ->
                    //displaying the upload progress
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
                }
        } else {
            //display an error if no file is selected

            Toast.makeText(this,"no file is selected", Toast.LENGTH_SHORT).show()
        }
        }
        catch (e: Exception)
        {
            System.out.println("Error Null");
        }
    }


}
