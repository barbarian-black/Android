package com.example.whack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

@SuppressLint("NewApi")
public class Capture_face extends Activity {

	
	private Camera mCamera;
	private Preview mPreview;
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {

	        File pictureFile = null;
	        if (pictureFile == null){
	            Log.d("TAG", "Error creating media file, check storage permissions: ");
	            return;
	        }

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d("TAG", "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d("TAG", "Error accessing file: " + e.getMessage());
	        }finally{
	        	mCamera.startPreview();
	        }
	    }
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_face);
        // Create an instance of Camera
		
		if(checkCameraHardware(this)){
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new Preview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
		}

	}
	

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	    	Log.d("errors", "No camera hardware found");
	        // no camera on this device
	        return false;
	    }
	}
	
	/**
	 * safely opening camera
	 */
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	public void start_camera(View view){
		mCamera.takePicture(null, null, mPicture);
	}
    
	


}
