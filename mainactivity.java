package com.example.privacyview.UI;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.privacyview.Drawing.BorderedText;
import com.example.privacyview.Drawing.MultiBoxTracker;
import com.example.privacyview.Drawing.OverlayView;
import com.example.privacyview.Face_Recognition.FaceClassifier;
import com.example.privacyview.Face_Recognition.TFLiteFaceRecognition;
import com.example.privacyview.LiveFeed.CameraConnectionFragment;
import com.example.privacyview.LiveFeed.ImageUtils;
import com.example.privacyview.R;
import com.example.privacyview.services.PrivacyViewForegroundService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageReader.OnImageAvailableListener{


    PrivacyViewForegroundService privacyViewForegroundService;
    public boolean isOverlayAdded = false;

    Handler handler;
    private Matrix frameToCropTransform;
    private int sensorOrientation;
    private Matrix cropToFrameTransform;

    private static final boolean MAINTAIN_ASPECT = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private BorderedText borderedText;
    private MultiBoxTracker tracker;
    private Integer useFacing = null;
    private static final String KEY_USE_FACING = "use_facing";
    private static final int CROP_SIZE = 1000;
    private static final int TF_OD_API_INPUT_SIZE2 = 160;


    //TODO delcare face detector
    FaceDetector detector;

    //TODO declare face recognizer
    private FaceClassifier faceClassifier;

    boolean registerFace = false;

    private PreviewView previewView;
    private static MainActivity instance;
    Boolean isRegistering=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        instance=this;
        privacyViewForegroundService = new PrivacyViewForegroundService();


        // Retrieve the Intent that started this Activity
        Intent intentForNav = getIntent();
        // Retrieve the string extra added in the first Activity
        String action = intentForNav.getStringExtra("register");
        if (action != null && action.equals("true")){
            isRegistering = true;
        }

        // Check if the extra exists and perform the appropriate action
        if ( isRegistering) {
            // Perform registration logic here
            setContentView(R.layout.activity_main_registration_overlay);

            Toast.makeText(this, "Registration requested", Toast.LENGTH_SHORT).show();
            registerFace = true;
        } else {
            // Handle other cases if needed
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Recognition action", Toast.LENGTH_SHORT).show();
        }


        //TODO handling permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 121);
            }
        }

        Intent intent = getIntent();
        useFacing = intent.getIntExtra(KEY_USE_FACING, CameraCharacteristics.LENS_FACING_BACK);




        //TODO initialize the tracker to draw rectangles
        tracker = new MultiBoxTracker(this);


        //TODO initalize face detector
        // Multiple object detection in static images
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();
        detector = FaceDetection.getClient(highAccuracyOpts);


        //TODO initialize FACE Recognition
        try {
            faceClassifier =
                    TFLiteFaceRecognition.create(
                            getAssets(),
                            "facenet.tflite",
                            TF_OD_API_INPUT_SIZE2,
                            false,getApplicationContext());

        } catch (final IOException e) {
            e.printStackTrace();
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 121 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                && isRegistering==false
        ){
            setFragment();

        }
    }

    public static MainActivity getInstance(){
        return instance;
    }

    //TODO fragment which show live footage from camera
    int previewHeight = 0,previewWidth = 0;
    protected void setFragment() {
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = manager.getCameraIdList()[useFacing];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Fragment fragment;
        CameraConnectionFragment camera2Fragment =
                CameraConnectionFragment.newInstance(
                        new CameraConnectionFragment.ConnectionCallback() {
                            @Override
                            public void onPreviewSizeChosen(final Size size, final int rotation) {
                                previewHeight = size.getHeight();
                                previewWidth = size.getWidth();

                                final float textSizePx =
                                        TypedValue.applyDimension(
                                                TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
                                borderedText = new BorderedText(textSizePx);
                                borderedText.setTypeface(Typeface.MONOSPACE);


                                int cropSize = CROP_SIZE;

                                previewWidth = size.getWidth();
                                previewHeight = size.getHeight();

                                sensorOrientation = rotation - getScreenOrientation();

                                rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
                                croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888);

                                frameToCropTransform =
                                        ImageUtils.getTransformationMatrix(
                                                previewWidth, previewHeight,
                                                cropSize, cropSize,
                                                sensorOrientation, MAINTAIN_ASPECT);

                                cropToFrameTransform = new Matrix();
                                frameToCropTransform.invert(cropToFrameTransform);

                                trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);


                                trackingOverlay.addCallback(
                                        new OverlayView.DrawCallback() {
                                            @Override
                                            public void drawCallback(final Canvas canvas) {
                                                tracker.draw(canvas);
                                                Log.d("tryDrawRect","inside draw");

                                            }
                                        });

                                tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
                            }
                        },
                        this,
                        R.layout.camera_fragment,
                        new Size(640, 480));

        camera2Fragment.setCamera(cameraId);
        fragment = camera2Fragment;
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }


    //TODO getting frames of live camera footage and passing them to model
    private boolean isProcessingFrame = false;
    private byte[][] yuvBytes = new byte[3][];
    private int[] rgbBytes = null;
    private int yRowStride;
    private Runnable postInferenceCallback;
    private Runnable imageConverter;
    private Bitmap rgbFrameBitmap;
    @Override
    public void onImageAvailable(ImageReader reader) {
        // We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return;
        }
        if (rgbBytes == null) {
            rgbBytes = new int[previewWidth * previewHeight];
        }
        try {
            final Image image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (isProcessingFrame) {
                image.close();
                return;
            }
            isProcessingFrame = true;
            final Image.Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);
            yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();

            imageConverter =
                    new Runnable() {
                        @Override
                        public void run() {
                            ImageUtils.convertYUV420ToARGB8888(
                                    yuvBytes[0],
                                    yuvBytes[1],
                                    yuvBytes[2],
                                    previewWidth,
                                    previewHeight,
                                    yRowStride,
                                    uvRowStride,
                                    uvPixelStride,
                                    rgbBytes);
                        }
                    };

            postInferenceCallback =
                    new Runnable() {
                        @Override
                        public void run() {
                            image.close();
                            isProcessingFrame = false;
                        }
                    };

            performFaceDetection();

        } catch (final Exception e) {
            Log.d("tryError",e.getMessage()+"abc ");
            return;
        }

    }

    protected void fillBytes(final Image.Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }
    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    Bitmap croppedBitmap;
    List<FaceClassifier.Recognition> mappedRecognitions;

    //TODO Perform face detection
    public void performFaceDetection(){
        imageConverter.run();;
        rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mappedRecognitions = new ArrayList<>();
                InputImage image = InputImage.fromBitmap(croppedBitmap,0);
                detector.process(image)
                        .addOnSuccessListener(
                                        new OnSuccessListener<List<Face>>() {
                                            @Override
                                            public void onSuccess(List<Face> faces) {

                                                for(Face face:faces) {
                                                    final Rect bounds = face.getBoundingBox();
                                                    performFaceRecognition(face);
                                                }
                                                registerFace = false;
                                                tracker.trackResults(mappedRecognitions, 10);
                                                trackingOverlay.postInvalidate();
                                                postInferenceCallback.run();

                                            }
                                        })
                        .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                            }
                                        });



            }
        });
    }

    //TODO perform face recognition
    public void performFaceRecognition(Face face){
        //TODO crop the face
        Rect bounds = face.getBoundingBox();
        if(bounds.top<0){
            bounds.top = 0;
        }
        if(bounds.left<0){
            bounds.left = 0;
        }
        if(bounds.left+bounds.width()>croppedBitmap.getWidth()){
            bounds.right = croppedBitmap.getWidth()-1;
        }
        if(bounds.top+bounds.height()>croppedBitmap.getHeight()){
            bounds.bottom = croppedBitmap.getHeight()-1;
        }

        Bitmap crop = Bitmap.createBitmap(croppedBitmap,
                bounds.left,
                bounds.top,
                bounds.width(),
                bounds.height());
        crop = Bitmap.createScaledBitmap(crop,TF_OD_API_INPUT_SIZE2,TF_OD_API_INPUT_SIZE2,false);


        final FaceClassifier.Recognition result = faceClassifier.recognizeImage(crop, registerFace);
        String title = "Unknown";
        float confidence = 0;
        if (result !=null) {
            if(registerFace){
                //showNameRegistration();
            }else {
                if (result.getDistance() < 0.75f) {
                    confidence = result.getDistance();
                    title = result.getTitle();

                }
                Log.d("JAYYY","NAME : "+title);

            }
        }else {
            Log.d("FaceRecognition", "No result from face classifier.");
            // Handle case where recognition failed
        }

        RectF location = new RectF(bounds);
        if (bounds != null) {
            if(useFacing == CameraCharacteristics.LENS_FACING_BACK) {
                location.right = croppedBitmap.getWidth() - location.right;
                location.left = croppedBitmap.getWidth() - location.left;
            }
            cropToFrameTransform.mapRect(location);
            FaceClassifier.Recognition recognition = new FaceClassifier.Recognition(face.getTrackingId()+"",title,confidence,location);

//
//            String nameOfPerson="Unknown";
//            nameOfPerson=recognition.getTitle();
//            if (nameOfPerson.equals("Unknown") && !isOverlayAdded){
////                addMaskToScreen();
//                //Toast.makeText(MainActivity.this,"Unknown detected mask should be added",Toast.LENGTH_SHORT).show();
//                startForegroundService();
//                isOverlayAdded = true;
//            }
//            else {
//                stopForegroundService();
//                resetOverlayFlag(); // Reset the flag when stopping the service
//            }
            mappedRecognitions.add(recognition);
//            int countOfFaces = 0;
//            countOfFaces=mappedRecognitions.size();
//            handleFaceDetection(title);
//            if (countOfFaces==0){
//                stopForegroundService();
//            }
        }

    }

    public void handleFaceDetection(String detectedFaceType) {
        if (detectedFaceType.equals("Unknown")) {
            // If overlay is not already added, add it
            Log.i("CODE","Value of isOverlay is : "+isOverlayAdded);
            if (!isOverlayAdded) {
                startForegroundService();
                isOverlayAdded = true; // Set the flag to true
            }
        } else if (detectedFaceType.equals("registered")) {
            // Remove the overlay if a registered user is detected
            if (isOverlayAdded) {
                stopForegroundService(); // Implement this method to remove the overlay
                isOverlayAdded = false; // Reset the flag
            }
        }
    }
    public boolean isOverlayAdded(){
        return isOverlayAdded;
    }


    private void resetOverlayFlag() {
        isOverlayAdded = false;
    }
    public void startForegroundService() {
        Intent serviceIntent = new Intent(this, PrivacyViewForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }
    public void stopForegroundService() {
        Intent serviceIntent = new Intent(this, PrivacyViewForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detector.close();
        stopForegroundService();
    }




}
