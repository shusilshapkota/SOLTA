package com.cksco.theapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;

import android.hardware.Camera;

import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static java.nio.ByteBuffer.allocateDirect;

/**
 * Created by cksco on 5/2/2018.
 */

public class MyGLCamSurf extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    public MyGLCamSurf(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyGLCamSurf(Context context) {
        super(context);
        mContext = context;
        init();
    }


    protected boolean testerBool;
    protected PhotoAnalysis mAnalysis;
    protected int mStartX, mStartY, mEndX, mEndY;
    protected final MainActivity_ main = new MainActivity_();
    protected List<Bitmap> frameList = new ArrayList<Bitmap>();
    protected Context mContext;
    protected Camera mCamera;
    protected SurfaceTexture mSurfaceTexture;
    protected final MyOESTexture mCamTexture = new MyOESTexture();
    protected final MyShader mOffScrnShadder = new MyShader();
    protected int mWidth, mHeight;
    protected boolean updateTexture = false;
    private ByteBuffer mFullQuadVertices;
    private float[] mTransformM = new float[16];
    private float[] mOrientationM = new float[16];
    private float[] mRatio = new float[2];

    public void init() {
        final byte QUAD_COORD[] = {-1, 1, -1, -1, 1, 1, 1, -1};
        mFullQuadVertices = allocateDirect(4 * 2);
        mFullQuadVertices.put(QUAD_COORD).position(0);
        setPreserveEGLContextOnPause(true);

        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        updateTexture = true;
        requestRender();
    }

    @Override
    public synchronized void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //load and compile shader

        try {
            mOffScrnShadder.setProgram(R.raw.vshader, R.raw.fshader, mContext);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public synchronized void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        //generate c    amera texture------------------------
        mCamTexture.init();
        //set up surfacetexture------------------
        SurfaceTexture oldSurfaceTexture = mSurfaceTexture;
        mSurfaceTexture = new SurfaceTexture(mCamTexture.getTexureID());
        mSurfaceTexture.setOnFrameAvailableListener(this);
        if (oldSurfaceTexture != null) {
            oldSurfaceTexture.release();
        }
        //set camera para-----------------------------------
        int camera_width = 0;
        int camera_height = 0;

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Camera.Parameters param = mCamera.getParameters();
        List<Size> psize = param.getSupportedPreviewSizes();
        Size newSize = psize.get(0);
        if (psize.size() > 0) {
            int i;
            for (i = 0; i < psize.size(); i++) {
                Log.println(Log.ASSERT, "RES", " : " + psize.get(i).width + " " + psize.get(i).height);
                if (psize.get(i).width > newSize.width || psize.get(i).height > newSize.height) {

                    newSize = psize.get(i);


                }

            }
            if (i > 0)
                i--;
            param.setPreviewSize(newSize.width, newSize.height);

            camera_width = newSize.width;
            camera_height = newSize.height;
            mStartX = (mWidth / 2) - (newSize.width / 2);
            mStartY = (mHeight / 2) - (newSize.height / 2);
            mEndX = (mWidth / 2) + (newSize.width / 2);
            mEndY = (mHeight / 2) + (newSize.height / 2);

            testerBool = true;
            System.out.println("WIDTH IS HERE    " + newSize.width + " , " + newSize.height);

        }

        //get the camera orientation and display dimension------------
        if (mContext.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            Matrix.setRotateM(mOrientationM, 0, 90.0f, 0f, 0f, 1f);
            mRatio[1] = camera_width * 1.0f / height;
            mRatio[0] = camera_height * 1.0f / width;
        } else {
            Matrix.setRotateM(mOrientationM, 0, 0.0f, 0f, 0f, 1f);
            mRatio[1] = camera_height * 1.0f / height;
            mRatio[0] = camera_width * 1.0f / width;
        }

        //start camera-----------------------------------------
        mCamera.setParameters(param);
        mCamera.startPreview();

        //start render---------------------
        requestRender();
    }

    @Override
    public synchronized void onDrawFrame(GL10 gl) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //render the texture to FBO if new frame is available
        if (updateTexture) {
            mSurfaceTexture.updateTexImage();

            // AnalyzPixels(mFullQuadVertices);
            mSurfaceTexture.getTransformMatrix(mTransformM);

            updateTexture = false;
            // analyzPixels();
            // mFrames.execute(analyzPixels());
            GLES20.glViewport(0, 0, mWidth, mHeight);

            mOffScrnShadder.useProgram();

            int uTransformM = mOffScrnShadder.getHandle("uTransformM");
            int uOrientationM = mOffScrnShadder.getHandle("uOrientationM");
            int uRatioV = mOffScrnShadder.getHandle("ratios");

            GLES20.glUniformMatrix4fv(uTransformM, 1, false, mTransformM, 0);
            GLES20.glUniformMatrix4fv(uOrientationM, 1, false, mOrientationM, 0);
            GLES20.glUniform2fv(uRatioV, 1, mRatio, 0);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mCamTexture.getTexureID());

            renderQuad(mOffScrnShadder.getHandle("aPosition"));
            analyzPixels();
        }

    }

    private void renderQuad(int aPosition) {
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_BYTE, false, 0, mFullQuadVertices);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onDestroy() {
        updateTexture = false;
        mSurfaceTexture.release();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
        }

        mCamera = null;
    }

    public void saveFrames(Bitmap bitmap, String filename) throws IOException {
        if (testerBool) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 50);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            File extStorDirect = Environment.getExternalStorageDirectory();
            File file = new File(extStorDirect + File.separator + filename);
            FileOutputStream fileOutputStream = null;
            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes.toByteArray());
                ContentResolver cr = getContext().getContentResolver();

                String imagePath = file.getAbsolutePath();
                String name = file.getName();
                String description = "My bitmap created by Android-er";
                String savedURL = MediaStore.Images.Media
                        .insertImage(cr, imagePath, name, description);
                System.out.println(bitmap.getHeight() + " , " + bitmap.getWidth());


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
            testerBool = false;
        } else
            return;

    }


    public void analyzPixels() {
        // int width = getWidth()-mEndX;
        // int height = getHeight()-mEndY;
        // Log.println(Log.ASSERT,"TESTER","width : "+(mEndX-mStartX)+" height"+(mEndY-mStartY));
        System.gc();
        ByteBuffer byteBuf;
        int width = getWidth();
        int height = getHeight();
        try {
            byteBuf = allocateDirect(width * height * 4);

            byteBuf.order(ByteOrder.LITTLE_ENDIAN);

            GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuf);
            new PhotoAnalysis(width, height, getContext()).execute(byteBuf);

            byteBuf.clear();
            return;
        } catch (OutOfMemoryError e) {
            return;
        }


      /*  long start = System.currentTimeMillis();


        byteBuf.rewind();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        bitmap.copyPixelsFromBuffer(byteBuf);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.preScale(1f, -1f);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        try {
            saveFrames(bitmap2, "test");


        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        bitmap = null;


        return bitmap2;
*/

    }

}


