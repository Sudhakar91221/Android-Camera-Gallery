Android-Camera-Gallery
======================

in Java file
SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		preview = new Preview(this, surfaceView);
		
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		FrameLayout previewFrameLayout = (FrameLayout) findViewById(R.id.preview);
		previewFrameLayout.addView(preview);
		
		preview.setKeepScreenOn(true);
		
		on button action 
		camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		
		
		This code will let you capture picture from camera
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
