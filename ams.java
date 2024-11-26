 I'll help you add these features step by step.

1. First, let's add the TensorFlow Lite dependency in your app's build.gradle:

```gradle
dependencies {
    implementation 'org.tensorflow:tensorflow-lite:2.12.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
}
```

2. Create a new class for Face Embeddings:

```java
// FaceEmbedding.java
public class FaceEmbedding {
    private float[] embedding;
    private String name;

    public FaceEmbedding(float[] embedding, String name) {
        this.embedding = embedding;
        this.name = name;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public String getName() {
        return name;
    }
}
```

3. Create a FaceRecognitionHelper class for embedding generation and matching:

```java
// FaceRecognitionHelper.java
import org.tensorflow.lite.Interpreter;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class FaceRecognitionHelper {
    private static final int EMBEDDING_SIZE = 128; // Size of face embedding vector
    private static final float RECOGNITION_THRESHOLD = 0.7f; // Similarity threshold
    private Interpreter tfLite;
    private List<FaceEmbedding> knownFaceEmbeddings;
    private Context context;

    public FaceRecognitionHelper(Context context) {
        this.context = context;
        knownFaceEmbeddings = new ArrayList<>();
        loadModel();
    }

    private void loadModel() {
        try {
            // Load model from assets folder
            File modelFile = new File(context.getFilesDir(), "facenet_model.tflite");
            tfLite = new Interpreter(modelFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Generate embedding for a face image
    public float[] generateEmbedding(Bitmap faceBitmap) {
        // Preprocess the bitmap - resize to model input size (e.g., 160x160)
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(faceBitmap, 160, 160, true);
        
        // Convert bitmap to ByteBuffer
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(1 * 160 * 160 * 3 * 4);
        inputBuffer.order(ByteOrder.nativeOrder());
        
        int[] intValues = new int[160 * 160];
        resizedBitmap.getPixels(intValues, 0, 160, 0, 0, 160, 160);
        
        for (int pixelValue : intValues) {
            inputBuffer.putFloat(((pixelValue >> 16) & 0xFF) / 255.0f);
            inputBuffer.putFloat(((pixelValue >> 8) & 0xFF) / 255.0f);
            inputBuffer.putFloat((pixelValue & 0xFF) / 255.0f);
        }

        // Output buffer for the embedding
        float[][] outputEmbedding = new float[1][EMBEDDING_SIZE];
        
        // Run inference
        tfLite.run(inputBuffer, outputEmbedding);
        
        return outputEmbedding[0];
    }

    // Compare two face embeddings using cosine similarity
    private float calculateSimilarity(float[] embedding1, float[] embedding2) {
        float dotProduct = 0.0f;
        float norm1 = 0.0f;
        float norm2 = 0.0f;
        
        for (int i = 0; i < EMBEDDING_SIZE; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += embedding1[i] * embedding1[i];
            norm2 += embedding2[i] * embedding2[i];
        }
        
        return dotProduct / (float)(Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // Recognize a face by comparing with stored embeddings
    public String recognizeFace(float[] newEmbedding) {
        String recognizedName = "Unknown";
        float maxSimilarity = 0.0f;
        
        for (FaceEmbedding knownFace : knownFaceEmbeddings) {
            float similarity = calculateSimilarity(newEmbedding, knownFace.getEmbedding());
            if (similarity > maxSimilarity && similarity > RECOGNITION_THRESHOLD) {
                maxSimilarity = similarity;
                recognizedName = knownFace.getName();
            }
        }
        
        return recognizedName;
    }

    // Add a new face embedding to known faces
    public void addFaceEmbedding(float[] embedding, String name) {
        knownFaceEmbeddings.add(new FaceEmbedding(embedding, name));
        saveFaceEmbeddings(); // Save to storage
    }

    // Save embeddings to storage
    private void saveFaceEmbeddings() {
        try {
            File embeddingsFile = new File(context.getFilesDir(), "face_embeddings.dat");
            FileOutputStream fos = new FileOutputStream(embeddingsFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(knownFaceEmbeddings);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load embeddings from storage
    public void loadFaceEmbeddings() {
        try {
            File embeddingsFile = new File(context.getFilesDir(), "face_embeddings.dat");
            if (embeddingsFile.exists()) {
                FileInputStream fis = new FileInputStream(embeddingsFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                knownFaceEmbeddings = (List<FaceEmbedding>) ois.readObject();
                ois.close();
                fis.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

4. Modify FaceDetection.java to include embedding generation:

```java
// Add to existing variables
private FaceRecognitionHelper faceRecognitionHelper;

// In onCreate()
faceRecognitionHelper = new FaceRecognitionHelper(this);
faceRecognitionHelper.loadFaceEmbeddings();

// Modify registerCapturedFaces() to include embedding generation
private void registerCapturedFaces() {
    String name = nameEditText.getText().toString().trim();
    if (name.isEmpty()) {
        Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        return;
    }

    // Process each captured face
    for (String imagePath : capturedFacePaths) {
        try {
            // Load the face image
            Bitmap faceBitmap = BitmapFactory.decodeFile(imagePath);
            
            // Generate embedding
            float[] embedding = faceRecognitionHelper.generateEmbedding(faceBitmap);
            
            // Store embedding with name
            faceRecognitionHelper.addFaceEmbedding(embedding, name);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Rest of your existing code...
}

// Add button in layout for recognition
Button recognizeButton = findViewById(R.id.recognizeButton);
recognizeButton.setOnClickListener(v -> {
    Intent intent = new Intent(this, FaceRecognitionActivity.class);
    startActivity(intent);
});
```

5. Create new FaceRecognitionActivity.java:

```java
public class FaceRecognitionActivity extends AppCompatActivity {
    private PreviewView previewView;
    private FaceBoxOverlay recognitionOverlay;
    private ExecutorService cameraExecutor;
    private FaceDetector faceDetector;
    private FaceRecognitionHelper faceRecognitionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        previewView = findViewById(R.id.recognition_preview_view);
        recognitionOverlay = findViewById(R.id.recognition_face_overlay);
        
        faceRecognitionHelper = new FaceRecognitionHelper(this);
        faceRecognitionHelper.loadFaceEmbeddings();
        
        setupCamera();
    }

    private void setupCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor();

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build();
        faceDetector = FaceDetection.getClient(options);

        ProcessCameraProvider.getInstance(this).addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(this).get();
                bindCameraUseCases(cameraProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
            @SuppressWarnings("ConstantConditions")
            Image image = imageProxy.getImage();
            if (image == null) {
                imageProxy.close();
                return;
            }

            InputImage inputImage = InputImage.fromMediaImage(image, 
                    imageProxy.getImageInfo().getRotationDegrees());

            faceDetector.process(inputImage)
                    .addOnSuccessListener(faces -> {
                        for (Face face : faces) {
                            Rect bounds = face.getBoundingBox();
                            // Convert image to bitmap and crop face
                            Bitmap faceBitmap = cropFaceFromImage(image, bounds);
                            if (faceBitmap != null) {
                                // Generate embedding
                                float[] embedding = faceRecognitionHelper.generateEmbedding(faceBitmap);
                                // Get recognized name
                                String recognizedName = faceRecognitionHelper.recognizeFace(embedding);
                                // Update overlay with name
                                recognitionOverlay.updateFaceBox(bounds, recognizedName);
                            }
                        }
                    })
                    .addOnCompleteListener(task -> imageProxy.close());
        });

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
            );

            preview.setSurfaceProvider(previewView.getSurfaceProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap cropFaceFromImage(Image image, Rect bounds) {
        // Implementation similar to your existing image conversion code
        // but cropping to the face bounds
        try {
            Bitmap fullBitmap = imageToBitmap(image);
            return Bitmap.createBitmap(fullBitmap, 
                    bounds.left, bounds.top, 
                    bounds.width(), bounds.height());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}
```

6. Create layout for recognition activity (activity_face_recognition.xml):

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.camera.view.PreviewView
        android:id="@+id/recognition_preview_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <com.your.package.FaceBoxOverlay
        android:id="@+id/recognition_face_overlay"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

7. Update your FaceBoxOverlay class to handle named boxes:

```java
public class FaceBoxOverlay extends View {
    private Paint boxPaint;
    private Paint textPaint;
    private List<Pair<Rect, String>> faceBoxesWithNames;

    // Constructor and other existing methods...

    public void updateFaceBox(Rect bounds, String name) {
        if (faceBoxesWithNames == null) {
            faceBoxesWithNames = new ArrayList<>();
        }
        faceBoxesWithNames.add(new Pair<>(bounds, name));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (faceBoxesWithNames != null) {
            for (Pair<Rect, String> faceBox : faceBoxesWithNames) {
                // Draw box
                canvas.drawRect(faceBox.first, boxPaint);
                // Draw name
                canvas.drawText(faceBox.second, 
                        faceBox.first.left, 
                        faceBox.first.top - 10, 
                        textPaint);
            }
        }
    }
}
```

This implementation:
1. Generates embeddings for registered faces during registration
2. Stores embeddings with associated names
3. Provides real-time face recognition using stored embeddings
4. Shows bounding boxes with recognized names in real-time
5. Uses TensorFlow Lite for face embedding generation
6. Implements cosine similarity for face matching

Note: You'll need to:
1. Download a suitable TensorFlow Lite face recognition model
2. Place it in your assets folder
3. Adjust the input size and preprocessing according to your model
4. Fine-tune the recognition threshold based on your needs
5. Add proper error handling and loading indicators

Would you like me to explain any specific part in more detail?

 Here are all the necessary imports for FaceRecognitionActivity.java:

```java
// Android and AndroidX core imports
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// Camera X imports
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

// ML Kit imports
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

// Your custom imports (adjust package name as needed)
import com.your.package.helpers.FaceRecognitionHelper;
import com.your.package.utils.FaceBoxOverlay;
import com.your.package.R;

// Utility imports
import com.google.common.util.concurrent.ListenableFuture;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Optional imports for additional features
import android.util.Size;
import android.util.Log;
import java.util.ArrayList;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import java.io.IOException;
```

Usage notes for specific imports:

1. Camera-related imports:
```java
androidx.camera.core.*  // For CameraX functionality
androidx.camera.lifecycle.ProcessCameraProvider  // For camera lifecycle management
androidx.camera.view.PreviewView  // For camera preview
```

2. ML Kit imports:
```java
com.google.mlkit.vision.*  // For face detection
```

3. Custom imports:
```java
// Adjust these according to your package structure
com.your.package.helpers.FaceRecognitionHelper  // Your face recognition helper class
com.your.package.utils.FaceBoxOverlay  // Your custom view for drawing face boxes
```

4. Utility imports:
```java
java.util.concurrent.*  // For threading and executors
java.nio.ByteBuffer  // For image processing
```

Make sure to:
1. Replace `com.your.package` with your actual package name
2. Add any additional imports if you implement extra features
3. Remove unused imports to keep the code clean
4. Check that all referenced libraries are included in your build.gradle file

If you're using additional features like saving images or logging, you might need these imports too:
```java
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
```

For error handling and debugging:
```java
import android.util.Log;
import java.io.IOException;
import java.lang.Exception;
```

Remember to add the required permissions in your AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
```

Would you like me to explain any specific import or add any additional ones for particular features?

public class FaceRecognitionActivity extends AppCompatActivity {
    private PreviewView previewView;
    private FaceBoxOverlay recognitionOverlay;
    private ExecutorService cameraExecutor;
    private FaceDetector faceDetector;
    private FaceRecognitionHelper faceRecognitionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        previewView = findViewById(R.id.recognition_preview_view);
        recognitionOverlay = findViewById(R.id.recognition_face_overlay);

        faceRecognitionHelper = new FaceRecognitionHelper(this);
        faceRecognitionHelper.loadFaceEmbeddings();

        setupCamera();
    }

    private void setupCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor();

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build();
        faceDetector = FaceDetection.getClient(options);

        ProcessCameraProvider.getInstance(this).addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(this).get();
                bindCameraUseCases(cameraProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
            @SuppressWarnings("ConstantConditions")
            Image image = imageProxy.getImage();
            if (image == null) {
                imageProxy.close();
                return;
            }

            InputImage inputImage = InputImage.fromMediaImage(image, 
                    imageProxy.getImageInfo().getRotationDegrees());

            faceDetector.process(inputImage)
                    .addOnSuccessListener(faces -> {
                        for (Face face : faces) {
                            Rect bounds = face.getBoundingBox();
                            // Convert image to bitmap and crop face
                            Bitmap faceBitmap = cropFaceFromImage(image, bounds);
                            if (faceBitmap != null) {
                                // Generate embedding
                                float[] embedding = faceRecognitionHelper.generateEmbedding(faceBitmap);
                                // Get recognized name
                                String recognizedName = faceRecognitionHelper.recognizeFace(embedding);
                                // Update overlay with name
                                recognitionOverlay.updateFaceBox(bounds, recognizedName);
                            }
                        }
                    })
                    .addOnCompleteListener(task -> imageProxy.close());
        });

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
            );

            preview.setSurfaceProvider(previewView.getSurfaceProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap cropFaceFromImage(Image image, Rect bounds) {
        try {
            Bitmap fullBitmap = imageToBitmap(image);
            return Bitmap.createBitmap(fullBitmap, 
                    bounds.left, bounds.top, 
                    bounds.width(), bounds.height());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap imageToBitmap(Image image) {
        if (image == null) return null;

        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        // U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}