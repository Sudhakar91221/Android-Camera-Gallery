package com.capstone.cameragallery;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Uri imageUri;
	private ImageView userImageView;
	private File fileName;
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		userImageView = (ImageView) findViewById(R.id.ivMainActivity);
		
		String file = getIntent().getStringExtra("filepath");
		if (file != null) {
			
			fileName = new File(file);
			userImageView.setImageBitmap(BitmapFactory.decodeFile(fileName.toString()));
		}

	}


	public void btnMainActivityCustomCameraAction(View view) {
		startActivity(new Intent(this, CustomCamera.class));

	}

	public void btnMainActivityCameraAction(View view) {
		takeImageFromCamera();
	}
	
	public void btnMainActivityGalleryAction(View view) {
		takeImageFromGallery();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Pick Image from Gallery
	private void takeImageFromGallery() {
		Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         
        startActivityForResult(i, PICK_IMAGE);
	}

	private void takeImageFromCamera() {
		// define the file-name to save photo taken by Camera activity

		fileName = getOutputMediaFile();
		// create parameters for Intent with filename
		ContentValues values = new ContentValues();
		// values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION,
				"Image capture by camera");
		// imageUri is the current activity attribute, define and save
		// it for later usage (also in onSaveInstanceState)
		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		// create new Intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileName));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, PICK_Camera_IMAGE);
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			
			if (resultCode == Activity.RESULT_OK) {
				 Uri selectedImage = data.getData();
		            String[] filePathColumn = { MediaStore.Images.Media.DATA };
		 
		            Cursor cursor = getContentResolver().query(selectedImage,
		                    filePathColumn, null, null, null);
		            cursor.moveToFirst();
		 
		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String picturePath = cursor.getString(columnIndex);
		            fileName = new File(picturePath);
		            cursor.close();
		             
		            userImageView.setImageBitmap(BitmapFactory.decodeFile(fileName.toString()));
			}
			break;
		case PICK_Camera_IMAGE:
			if (resultCode == RESULT_OK) {

				Log.v("", "Project Image path === " + fileName);

				// copyFile(new File(selectedImagePath), f);

				userImageView.setImageBitmap(BitmapFactory.decodeFile(fileName.toString()));
				
				
				
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		Bitmap bm = null;

		try {

			
		} catch (Exception exception) {
			Log.v("", "Error Message" + exception.getMessage());
		}
	}
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}
}
