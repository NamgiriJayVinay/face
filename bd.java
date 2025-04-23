import android.content.Context;
import android.content.res.AssetManager;

import com.example.app.constants.AppCategoryGroup;
import com.example.app.constants.ModelConfigDetails;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModelThresholdDetailsTest {

    @Mock
    private Context mockContext;
    
    @Mock
    private AssetManager mockAssetManager;
    
    private ModelThresholdDetails modelThresholdDetails;
    
    // Sample JSON strings for testing
    private static final String VALID_CONFIG_JSON = "{"
            + "\"" + ModelConfigDetails.MODEL_CONFIG_JSON_MODEL_NAME_KEY + "\": \"test_model.tflite\","
            + "\"" + ModelConfigDetails.MODEL_CONFIG_JSON_MODEL_MODEL_VERSION_KEY + "\": \"1.0.0\","
            + "\"" + ModelConfigDetails.MODEL_CONFIG_JSON_MODEL_THRESHOLD_FILE_NAME_JSON + "\": \"threshold.json\""
            + "}";
            
    private static final String VALID_THRESHOLD_JSON = "{"
            + "\"" + ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_DETAILS_KEY + "\": {"
            + "\"" + ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_MEAN_KEY + "\": [0.5, 1.0, 1.5],"
            + "\"" + ModelConfigDetails.MODEL_CONFIG_JSON_SCALER_STANDARD_DEVIATION_KEY + "\": [0.1, 0.2, 0.3]"
            + "},"
            + "\"0\": {\"0\": [0.1, 0.2, 0.3], \"1\": [0.4, 0.5, 0.6]},"
            + "\"1\": {\"0\": [0.7, 0.8, 0.9], \"1\": [1.0, 1.1, 1.2]}"
            + "}";
            
    private static final String INVALID_CONFIG_JSON = "{"
            + "\"invalid_key\": \"value\""
            + "}";
            
    private static final String INVALID_THRESHOLD_JSON = "{"
            + "\"invalid_key\": \"value\""
            + "}";

    @BeforeEach
    public void setUp() throws IOException {
        // Set up mock Context and AssetManager
        when(mockContext.getAssets()).thenReturn(mockAssetManager);
    }

    /**
     * Test for successful initialization with valid JSON files
     */
    @Test
    @DisplayName("Test successful initialization with valid JSON files")
    public void testSuccessfulInitialization() throws IOException {
        // Arrange
        mockValidJsonFiles();
        
        // Act
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Assert
        assertNotNull(modelThresholdDetails);
        assertEquals("test_model.tflite", modelThresholdDetails.getModelFileName());
        assertEquals("1.0.0", modelThresholdDetails.getModelVersion());
        
        // Verify scaler details were loaded correctly
        ArrayList<Double> mean = modelThresholdDetails.getMean();
        ArrayList<Double> std = modelThresholdDetails.getStd();
        
        assertNotNull(mean);
        assertNotNull(std);
        assertEquals(3, mean.size());
        assertEquals(3, std.size());
        assertEquals(0.5, mean.get(0));
        assertEquals(0.1, std.get(0));
    }
    
    /**
     * Test for getting threshold values for a specific app category and foreground state
     */
    @Test
    @DisplayName("Test getting threshold values for category 0, foreground=1")
    public void testGetThresholdForCategory() throws IOException {
        // Arrange
        mockValidJsonFiles();
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Act
        ArrayList<Double> threshold = modelThresholdDetails.getThreshold(0, 1);
        
        // Assert
        assertNotNull(threshold);
        assertEquals(3, threshold.size());
        assertEquals(0.4, threshold.get(0));
        assertEquals(0.5, threshold.get(1));
        assertEquals(0.6, threshold.get(2));
    }
    
    /**
     * Test for getting threshold values for a different app category
     */
    @Test
    @DisplayName("Test getting threshold values for category 1, background=0")
    public void testGetThresholdForDifferentCategory() throws IOException {
        // Arrange
        mockValidJsonFiles();
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Act
        ArrayList<Double> threshold = modelThresholdDetails.getThreshold(1, 0);
        
        // Assert
        assertNotNull(threshold);
        assertEquals(3, threshold.size());
        assertEquals(0.7, threshold.get(0));
        assertEquals(0.8, threshold.get(1));
        assertEquals(0.9, threshold.get(2));
    }
    
    /**
     * Negative test: Invalid config JSON
     */
    @Test
    @DisplayName("Test initialization with invalid config JSON")
    public void testInitializationWithInvalidConfigJson() throws IOException {
        // Arrange
        when(mockAssetManager.open(ModelConfigDetails.MODEL_CONFIG_FILENAME))
                .thenReturn(new ByteArrayInputStream(INVALID_CONFIG_JSON.getBytes()));
        
        // Act
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Assert
        assertNull(modelThresholdDetails.getModelFileName());
        assertNull(modelThresholdDetails.getModelVersion());
    }
    
    /**
     * Negative test: Invalid threshold JSON
     */
    @Test
    @DisplayName("Test initialization with invalid threshold JSON")
    public void testInitializationWithInvalidThresholdJson() throws IOException, JSONException {
        // Arrange
        when(mockAssetManager.open(ModelConfigDetails.MODEL_CONFIG_FILENAME))
                .thenReturn(new ByteArrayInputStream(VALID_CONFIG_JSON.getBytes()));
        when(mockAssetManager.open("threshold.json"))
                .thenReturn(new ByteArrayInputStream(INVALID_THRESHOLD_JSON.getBytes()));
        
        // Act
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Assert
        assertNull(modelThresholdDetails.getMean());
        assertNull(modelThresholdDetails.getStd());
    }
    
    /**
     * Negative test: File not found exception
     */
    @Test
    @DisplayName("Test initialization when config file is not found")
    public void testInitializationWithFileNotFoundException() throws IOException {
        // Arrange
        when(mockAssetManager.open(ModelConfigDetails.MODEL_CONFIG_FILENAME))
                .thenThrow(new IOException("File not found"));
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            modelThresholdDetails = new ModelThresholdDetails(mockContext);
        });
    }
    
    /**
     * Negative test: Getting threshold for non-existent category
     */
    @Test
    @DisplayName("Test getting threshold for non-existent category")
    public void testGetThresholdForNonExistentCategory() throws IOException {
        // Arrange
        mockValidJsonFiles();
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Act
        ArrayList<Double> threshold = modelThresholdDetails.getThreshold(999, 1);
        
        // Assert
        assertNull(threshold);
    }
    
    /**
     * Negative test: Getting threshold for non-existent foreground/background value
     */
    @Test
    @DisplayName("Test getting threshold for non-existent foreground value")
    public void testGetThresholdForNonExistentForegroundValue() throws IOException {
        // Arrange
        mockValidJsonFiles();
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Act
        ArrayList<Double> threshold = modelThresholdDetails.getThreshold(0, 999);
        
        // Assert
        assertNull(threshold);
    }
    
    /**
     * Test with all app category groups to ensure complete coverage
     */
    @Test
    @DisplayName("Test threshold values for all app category groups")
    public void testThresholdForAllCategories() throws IOException {
        // Arrange
        mockValidJsonFiles();
        modelThresholdDetails = new ModelThresholdDetails(mockContext);
        
        // Act & Assert
        for (AppCategoryGroup.AppCategoryGroupName category : AppCategoryGroup.AppCategoryGroupName.values()) {
            int categoryIndex = category.getValue();
            ArrayList<Double> bgThreshold = modelThresholdDetails.getThreshold(categoryIndex, 0);
            ArrayList<Double> fgThreshold = modelThresholdDetails.getThreshold(categoryIndex, 1);
            
            // Only categories 0 and 1 have been mocked with data
            if (categoryIndex <= 1) {
                assertNotNull(bgThreshold);
                assertNotNull(fgThreshold);
                assertEquals(3, bgThreshold.size());
                assertEquals(3, fgThreshold.size());
            }
        }
    }
    
    /**
     * Helper method to set up mock asset manager with valid JSON files
     */
    private void mockValidJsonFiles() throws IOException {
        when(mockAssetManager.open(ModelConfigDetails.MODEL_CONFIG_FILENAME))
                .thenReturn(new ByteArrayInputStream(VALID_CONFIG_JSON.getBytes()));
        when(mockAssetManager.open("threshold.json"))
                .thenReturn(new ByteArrayInputStream(VALID_THRESHOLD_JSON.getBytes()));
    }
}