package com.capstone.cameragallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

public class CustomCamera extends Activity{
	
	private Preview preview;

	private Button buttonClick;

	private Camera camera;

	private String EXTRA_IMAGE_PATH;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.customcamera_activity);
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		preview = new Preview(this, surfaceView);
		
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		FrameLayout previewFrameLayout = (FrameLayout) findViewById(R.id.preview);
		previewFrameLayout.addView(preview);
		
		preview.setKeepScreenOn(true);

		buttonClick = (Button) findViewById(R.id.buttonClick);

		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//				preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});

		buttonClick.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				camera.autoFocus(new AutoFocusCallback(){
					@Override
					public void onAutoFocus(boolean arg0, Camera arg1) {
						//camera.takePicture(shutterCallback, rawCallback, jpegCallback);
					}
				});
				return true;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//      preview.camera = Camera.open();
		camera = Camera.open();
		camera.startPreview();
		preview.setCamera(camera);
	}

	@Override
	protected void onPause() {
		if(camera != null) {
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onPause();
	}

	private void resetCam() {
		camera.startPreview();
		preview.setCamera(camera);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		private String fileName;

		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				String path = savePictureToFileSystem(data);

				setResult(path);
				
//				// Write to SD Card
//				fileName = String.format("/sdcard/camtest/%d.jpg", System.currentTimeMillis());
//				outStream = new FileOutputStream(fileName);
//				outStream.write(data);
//				outStream.close();
//				Log.d("", "onPictureTaken - wrote bytes: " + data.length);

				resetCam();
				
				Intent intent = new Intent(CustomCamera.this, MainActivity.class);
				intent.putExtra("filepath", path);
				startActivity(intent);
				finish();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
			}
			Log.d("", "onPictureTaken - jpeg");
		}
	};
	
	private static String savePictureToFileSystem(byte[] data) {
		File file = getOutputMediaFile();
		saveToFile(data, file);
		return file.getAbsolutePath();
	}

	private void setResult(String path) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_IMAGE_PATH, path);
		setResult(RESULT_OK, intent);
	}
	
	/** Create a File for saving the image */
	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile() {

		// Create Folder
		File folder = new File(Environment.getExternalStorageDirectory()
				.toString() + "/JuzFood/image");
		folder.mkdirs();

		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				Log.v("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());

		// Save the path as a string value
		String extStorageDirectory = folder.toString();

		System.out.println("Folder Path ===== " + extStorageDirectory);

		File mediaFile = new File(extStorageDirectory + File.separator
				+ "JUZFOOD_" + timeStamp + ".jpg");

		return mediaFile;
	}

	public static boolean saveToFile(byte[] bytes, File file) {
		boolean saved = false;
		try {

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
			saved = true;
		} catch (FileNotFoundException e) {
			Log.v("FileNotFoundException", "" + e);
		} catch (IOException e) {
			Log.v("IOException", "" + e);
		}
		return saved;
	}
}
