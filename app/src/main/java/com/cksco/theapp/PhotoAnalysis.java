package com.cksco.theapp;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by Admin on 2018-05-11.
 * this class is an asynchronous task that takes in a byte buffer, and analyzes the intensity of the RGB pixels
 * if it finds a match, it then saves the photo as a PNG.
 * the byte buffer is passed in from a seperate openGL thread, and passed to this thread for analysis for maximum efficency.
 */

public class PhotoAnalysis extends AsyncTask<ByteBuffer, Integer, Void> {
    int mThreshold;
    int mIntensity;
    int mWidth, mHeight;
    Context mContext;


    private StorageReference mstorageReference = FirebaseStorage.getInstance().getReference();


    //constructors setting width and height of the area to be analysed, as well as the context for it.
    PhotoAnalysis(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    PhotoAnalysis(int width, int height, Context context) {
        mWidth = width;
        mHeight = height;
        mContext = context;
    }

    @Override
    //setting threshold before the thread is executed.
    protected void onPreExecute() {
        super.onPreExecute();
        mThreshold = 156;

    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        // Log.println(Log.ASSERT,"DUN","AYYYY THE THREADS DED");
    }

    @Override
    // the main workload of the thread
    protected Void doInBackground(ByteBuffer... byteBuffers) {
        ByteBuffer buff = byteBuffers[0];
        //go through the byte buffer skipping every 4th value. as this value is the alpha value or transperancy. which isn't needed.
        for (int i = 0; i < buff.array().length - 13; i += 4) {
            mIntensity = 0;
            for (int r = 0; r < 3; r++) {
                mIntensity += buff.get(i + r);

            }
            //if it finds the intensity is greater then threshold, we turn it into an image to be saved.
            if (mIntensity >= mThreshold) {
                downloadFromBuffer(buff, "test");
                return null; // this isn't a regular Void, it's from Java.lang so it needs a return statement. in this case we merely return null as we know the thread is done running at this point
            }
        }

        return null; // end background task.


    }

    protected void downloadFromBuffer(ByteBuffer buff, String filename) {
        buff.rewind();
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888); // creating empty bitmpa, and then filling it and scaling it.

        bitmap.copyPixelsFromBuffer(buff);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.preScale(1f, -1f);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        bitmap = null;
        //setting up all nessecary things to create the PNG.
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bytesToUpload = bytes.toByteArray();                 // Byte array for firebase

        File extStorDirect = Environment.getExternalStorageDirectory();
        File file = new File(extStorDirect + File.separator + filename);
        FileOutputStream fileOutputStream = null;
        try {
            //creating the PNG.
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());
            ContentResolver cr = mContext.getContentResolver();

            uploadToFirebase(bytesToUpload, file);

            String imagePath = file.getAbsolutePath();
            String name = file.getName();
            String description = "My bitmap created by Android-er";
            String savedURL = MediaStore.Images.Media
                    .insertImage(cr, imagePath, name, description);
            Log.println(Log.ASSERT, "DUN", "file made bois");


            // Basic try catches and closing the fileoutput stream.
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            return;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }


    public void uploadToFirebase(byte[] bytes, File file) {
        StorageReference filePath = mstorageReference.child("photos").child(UUID.randomUUID().toString());
        UploadTask uploadTask = filePath.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Upload", "Succesful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Upload", "Failed");
            }
        });
    }


}


