package com.example.marinab.testapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri
import android.support.v4.content.FileProvider
import java.io.IOException


class MainActivity : AppCompatActivity() {
    val CAMERA_REQUEST_CODE = 0
    val REQUEST_TAKE_PHOTO = 1
    var mCurrentPhotoPath : String = ""

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val textView: TextView = findViewById(R.id.textView)
            textView.text = "alpjasjdkasndknaskdnkasndkasndkjnaskjdnaskjdnkasjndkas"
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null){
                // Create the File where the photo should go
                var photoFile : File? = null
                try {
                    photoFile = createImageFile();
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                }
                try {
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        var photoURI: Uri = FileProvider.getUriForFile(this,
                                "com.example.marinab.testapplication",
                                photoFile)
                        callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(callCameraIntent, REQUEST_TAKE_PHOTO)
                    }
                } catch (ex: IOException) {
                    var exx = ex.message
                }

            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var httpRequest = HttpRequest()
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK ) { //&& data != null

                    val imgFile = File(mCurrentPhotoPath)
                    Thread({
                        httpRequest.pushPostRquest2(imgFile)}).start()




                }
            }

            else -> {
                Toast.makeText(this, "Unrezognized request code", Toast.LENGTH_SHORT).show();
            }
        }
    }
//                    var data = data.extras.get("data") as Bitmap
//                    photoImageView.setImageBitmap(data)

    //        val base64 = Base64.encodeToString(image, Base64.DEFAULT)
    //                val bmOptions = BitmapFactory.Options()
    //               var bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions)
//                    bitmap = Bitmap.createScaledBitmap(bitmap, parent.getWidth(), parent.getHeight(), true)

//                    var photoURI: Uri = Uri.parse(mCurrentPhotoPath)
//                    val mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoURI)


//                    val bmOptions = BitmapFactory.Options()
//                    bmOptions.inJustDecodeBounds = true
//                    var bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
//                    var dataString = BitMapToString(bitmap)

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
