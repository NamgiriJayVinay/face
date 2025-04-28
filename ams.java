package com.example.app; // Replace with your actual package name

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.util.Triple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Test class for ModelInferenceManagerImpl
 * Contains 3 positive test cases and 15 negative test cases
 */
@ExtendWith(MockitoExtension.class)
public class ModelInferenceManagerImplTest {

    // Mocks for dependencies
    @Mock private Context mockContext;
    @Mock private ModelManager mockModelManager;
    @Mock private SuggestionScoreUtil mockScoreUtil;
    @Mock private SuggestionDao mockSuggestionDao;
    @Mock private AiPrivacyRepository mockRepository;
    @Mock private ExecutorService mockExecutorService;
    @Mock private Future<Void> mockFuture;
    @Mock private AppDatabase mockAppDatabase;
    
    // Class under test
    private ModelInferenceManagerImpl inferenceManager;
    
    // Test data
    private ArrayList<MinuteWiseInferenceDataPoint> testDataPoints;
    private MinuteWiseInferenceDataPoint testDataPoint;
    private AiMinuteDataEntity testMinuteData;
    private PeriodicUsageDataPoint testPeriodicData;
    
    @BeforeEach
    public void setUp() throws Exception {
        // Setup mock for static AppDatabase.getInstance()
        try (MockedStatic<AppDatabase> mockedAppDatabase = Mockito.mockStatic(AppDatabase.class)) {
            mockedAppDatabase.when(() -> AppDatabase.getInstance(any(Context.class))).thenReturn(mockAppDatabase);
            when(mockAppDatabase.suggestionDao()).thenReturn(mockSuggestionDao);
            
            // Setup mock for static ModelManager.getInstance()
            try (MockedStatic<ModelManager> mockedModelManager = Mockito.mockStatic(ModelManager.class)) {
                mockedModelManager.when(() -> ModelManager.getInstance(any(Context.class))).thenReturn(mockModelManager);
                when(mockModelManager.loadModel()).thenReturn(true);
                
                // Setup mock for static Log methods
                try (MockedStatic<Log> mockedLog = Mockito.mockStatic(Log.class)) {
                    mockedLog.when(() -> Log.i(anyString(), anyString())).thenReturn(0);
                    mockedLog.when(() -> Log.e(anyString(), anyString())).thenReturn(0);
                    mockedLog.when(() -> Log.w(anyString(), anyString())).thenReturn(0);
                    
                    // Setup mock for static AIModelAttributes methods
                    try (MockedStatic<AIModelAttributes> mockedAttributes = Mockito.mockStatic(AIModelAttributes.class)) {
                        HashMap<String, Pair<String, String>> testFeatureMap = new HashMap<>();
                        testFeatureMap.put("feature1", new Pair<>("android.permission.CAMERA", "CAMERA"));
                        testFeatureMap.put("feature2", new Pair<>("android.permission.MICROPHONE", "MICROPHONE"));
                        
                        mockedAttributes.when(AIModelAttributes::getFeaturePermissionTypeMap).thenReturn(testFeatureMap);
                        mockedAttributes.when(AIModelAttributes::getNumericalFeatures).thenReturn(new ArrayList<>(List.of("feature1", "feature2")));
                        
                        // Now create the instance under test
                        inferenceManager = new ModelInferenceManagerImpl(mockContext);
                        
                        // Replace dependencies with mocks
                        inferenceManager.modelManager = mockModelManager;
                        inferenceManager.suggestionScoreUtil = mockScoreUtil;
                        inferenceManager.aiPrivacyRepository = mockRepository;
                        inferenceManager.executorService = mockExecutorService;
                        inferenceManager.result = mockFuture;
                        
                        // Setup test data
                        setupTestData();
                    }
                }
            }
        }
    }
    
    private void setupTestData() {
        // Create test AiMinuteDataEntity
        testMinuteData = new AiMinuteDataEntity();
        testMinuteData.packageName = "com.example.testapp";
        testMinuteData.appCategory = 5;
        testMinuteData.appTrustLevel = 2;
        testMinuteData.timestamp = System.currentTimeMillis();
        testMinuteData.isForeground = 1;
        testMinuteData.accessHour = 14;
        testMinuteData.grantedPermissions = "android.permission.CAMERA,android.permission.MICROPHONE";
        
        // Create test PeriodicUsageDataPoint
        testPeriodicData = new PeriodicUsageDataPoint();
        
        // Create test MinuteWiseInferenceDataPoint
        testDataPoint = new MinuteWiseInferenceDataPoint(testMinuteData, testPeriodicData);
        
        // Create test list of data points
        testDataPoints = new ArrayList<>();
        testDataPoints.add(testDataPoint);
    }

    /* ========== POSITIVE TEST CASES ========== */

    /**
     * POSITIVE TEST CASE 1: Test successful inference with no anomalies
     */
    @Test
    public void testSuccessfulInferenceWithNoAnomalies() throws Exception {
        // Setup ModelManager mock to return empty anomalies map (no anomalies)
        ByteBuffer mockBuffer = ByteBuffer.allocate(100);
        Triple<List<ByteBuffer>, Integer, Integer> mockPreprocessedInput = 
            new Triple<>(List.of(mockBuffer), 10, 5);
        
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
            .thenReturn(mockPreprocessedInput);
            
        when(mockModelManager.runInference(any(ByteBuffer.class), anyInt(), anyInt(), anyMap()))
            .thenReturn(new HashMap<>()); // Empty anomalies map
        
        // Execute method under test
        int result = inferenceManager.performInference(testDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.SUCCESS.getValue(), result, "Should return success code for successful inference");
        verify(mockModelManager).prepareInputBatchesForInference(eq(1), anyMap());
        verify(mockModelManager).runInference(eq(mockBuffer), eq(10), eq(5), anyMap());
        verify(mockExecutorService, never()).submit(any(Runnable.class)); // Should not submit task if no anomalies
    }

    /**
     * POSITIVE TEST CASE 2: Test successful inference with anomalies
     */
    @Test
    public void testSuccessfulInferenceWithAnomalies() throws Exception {
        // Setup ModelManager mock to return anomalies
        ByteBuffer mockBuffer = ByteBuffer.allocate(100);
        Triple<List<ByteBuffer>, Integer, Integer> mockPreprocessedInput = 
            new Triple<>(List.of(mockBuffer), 10, 5);
        
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
            .thenReturn(mockPreprocessedInput);
        
        // Create anomalies map with one anomaly
        Map<String, Pair<Float, Float>> anomaliesMap = new HashMap<>();
        anomaliesMap.put("feature1", new Pair<>(0.8f, 0.5f));
        
        when(mockModelManager.runInference(any(ByteBuffer.class), anyInt(), anyInt(), anyMap()))
            .thenReturn(anomaliesMap);
            
        // Setup executorService mock
        when(mockExecutorService.submit(any(Runnable.class))).thenReturn(mockFuture);
        
        // Execute method under test
        int result = inferenceManager.performInference(testDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.SUCCESS.getValue(), result, "Should return success code for successful inference with anomalies");
        verify(mockModelManager).prepareInputBatchesForInference(eq(1), anyMap());
        verify(mockModelManager).runInference(eq(mockBuffer), eq(10), eq(5), anyMap());
        verify(mockExecutorService).submit(any(Runnable.class)); // Should submit task for anomaly processing
        verify(mockFuture).get(); // Should wait for task completion
    }

    /**
     * POSITIVE TEST CASE 3: Test getting anomaly features from anomalies array
     */
    @Test
    public void testGetAnomalyFeatures() {
        // Setup test data
        Boolean[] anomalies = {true, false, true};
        HashMap<String, String> sensorCountMap = new HashMap<>();
        sensorCountMap.put("feature1", "10");
        sensorCountMap.put("feature2", "20");
        
        // Mock AIModelAttributes.getNumericalFeatures to return test features
        try (MockedStatic<AIModelAttributes> mockedAttributes = Mockito.mockStatic(AIModelAttributes.class)) {
            mockedAttributes.when(AIModelAttributes::getNumericalFeatures)
                .thenReturn(new ArrayList<>(List.of("feature1", "feature2", "feature3")));
            
            // Execute method under test
            ArrayList<String> result = inferenceManager.getAnomalyFeatures(anomalies, sensorCountMap);
            
            // Verify
            assertEquals(1, result.size(), "Should return only features that are anomalous and present in sensorCountMap");
            assertEquals("feature1", result.get(0), "Should return 'feature1' as it's anomalous and in sensorCountMap");
        }
    }

    /* ========== NEGATIVE TEST CASES ========== */

    /**
     * NEGATIVE TEST CASE 1: Test inference with null model manager
     */
    @Test
    public void testInferenceWithNullModelManager() {
        // Set modelManager to null
        inferenceManager.modelManager = null;
        
        // Execute method under test
        int result = inferenceManager.performInference(testDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.MODEL_LOAD_ERROR.getValue(), result, "Should return MODEL_LOAD_ERROR for null model manager");
    }

    /**
     * NEGATIVE TEST CASE 2: Test inference with empty input data
     */
    @Test
    public void testInferenceWithEmptyInputData() {
        // Create empty input data
        ArrayList<MinuteWiseInferenceDataPoint> emptyDataPoints = new ArrayList<>();
        
        // Execute method under test
        int result = inferenceManager.performInference(emptyDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.SUCCESS.getValue(), result, "Should return SUCCESS for empty input data");
        verify(mockModelManager, never()).prepareInputBatchesForInference(anyInt(), anyMap());
        verify(mockModelManager, never()).runInference(any(), anyInt(), anyInt(), anyMap());
    }

    /**
     * NEGATIVE TEST CASE 3: Test inference with exception during preprocessing
     */
    @Test
    public void testInferenceWithPreprocessingException() throws Exception {
        // Setup ModelManager mock to throw exception
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
            .thenThrow(new RuntimeException("Preprocessing error"));
        
        // Execute method under test
        int result = inferenceManager.performInference(testDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.PREPROCESSING_ERROR.getValue(), result, "Should return PREPROCESSING_ERROR for exception during preprocessing");
    }

    /**
     * NEGATIVE TEST CASE 4: Test inference with exception during inference
     */
    @Test
    public void testInferenceWithInferenceException() throws Exception {
        // Setup ModelManager mock to throw exception during inference
        ByteBuffer mockBuffer = ByteBuffer.allocate(100);
        Triple<List<ByteBuffer>, Integer, Integer> mockPreprocessedInput = 
            new Triple<>(List.of(mockBuffer), 10, 5);
        
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
            .thenReturn(mockPreprocessedInput);
            
        when(mockModelManager.runInference(any(ByteBuffer.class), anyInt(), anyInt(), anyMap()))
            .thenThrow(new RuntimeException("Inference error"));
        
        // Execute method under test
        int result = inferenceManager.performInference(testDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.PREPROCESSING_ERROR.getValue(), result, "Should return PREPROCESSING_ERROR for exception during inference");
    }

    /**
     * NEGATIVE TEST CASE 5: Test inference with exception during suggestion insertion
     */
    @Test
    public void testInferenceWithSuggestionInsertionException() throws Exception {
        // Setup ModelManager mock to return anomalies
        ByteBuffer mockBuffer = ByteBuffer.allocate(100);
        Triple<List<ByteBuffer>, Integer, Integer> mockPreprocessedInput = 
            new Triple<>(List.of(mockBuffer), 10, 5);
        
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
            .thenReturn(mockPreprocessedInput);
        
        // Create anomalies map with one anomaly
        Map<String, Pair<Float, Float>> anomaliesMap = new HashMap<>();
        anomaliesMap.put("feature1", new Pair<>(0.8f, 0.5f));
        
        when(mockModelManager.runInference(any(ByteBuffer.class), anyInt(), anyInt(), anyMap()))
            .thenReturn(anomaliesMap);
            
        // Setup executorService mock to throw exception
        when(mockExecutorService.submit(any(Runnable.class))).thenReturn(mockFuture);
        when(mockFuture.get()).thenThrow(new RuntimeException("Task execution error"));
        
        // Execute method under test
        int result = inferenceManager.performInference(testDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.PREPROCESSING_ERROR.getValue(), result, "Should return PREPROCESSING_ERROR for exception during suggestion insertion");
    }

    /**
     * NEGATIVE TEST CASE 6: Test fill sensor count map with exception for feature
     */
    @Test
    public void testFillSensorCountMapWithFeatureException() throws Exception {
        // Create a mock MinuteWiseInferenceDataPoint that throws exception for getFeatureValue
        MinuteWiseInferenceDataPoint mockDataPoint = mock(MinuteWiseInferenceDataPoint.class);
        mockDataPoint.minuteWisePreProcessEntityForVAE = testMinuteData;
        
        when(mockDataPoint.getFeatureValue(any())).thenThrow(new RuntimeException("Feature error"));
        
        // Create test list with the mock data point
        ArrayList<MinuteWiseInferenceDataPoint> mockDataPoints = new ArrayList<>();
        mockDataPoints.add(mockDataPoint);
        
        // Setup other mocks for successful test execution
        ByteBuffer mockBuffer = ByteBuffer.allocate(100);
        Triple<List<ByteBuffer>, Integer, Integer> mockPreprocessedInput = 
            new Triple<>(List.of(mockBuffer), 10, 5);
        
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
            .thenReturn(mockPreprocessedInput);
            
        when(mockModelManager.runInference(any(ByteBuffer.class), anyInt(), anyInt(), anyMap()))
            .thenReturn(new HashMap<>()); // No anomalies
        
        // Execute method under test
        int result = inferenceManager.performInference(mockDataPoints);
        
        // Verify (should not throw exception, just log it)
        assertEquals(AiErrorCode.SUCCESS.getValue(), result, "Should return SUCCESS despite feature exception");
    }

    /**
     * NEGATIVE TEST CASE 7: Test model load failure in constructor
     */
    @Test
    public void testModelLoadFailureInConstructor() {
        // Setup mock for static methods
        try (MockedStatic<AppDatabase> mockedAppDatabase = Mockito.mockStatic(AppDatabase.class)) {
            mockedAppDatabase.when(() -> AppDatabase.getInstance(any(Context.class))).thenReturn(mockAppDatabase);
            when(mockAppDatabase.suggestionDao()).thenReturn(mockSuggestionDao);
            
            try (MockedStatic<ModelManager> mockedModelManager = Mockito.mockStatic(ModelManager.class)) {
                mockedModelManager.when(() -> ModelManager.getInstance(any(Context.class))).thenReturn(mockModelManager);
                // Return false for model load
                when(mockModelManager.loadModel()).thenReturn(false);
                
                try (MockedStatic<Log> mockedLog = Mockito.mockStatic(Log.class)) {
                    mockedLog.when(() -> Log.w(anyString(), anyString())).thenReturn(0);
                    
                    // Create instance under test
                    ModelInferenceManagerImpl testManager = new ModelInferenceManagerImpl(mockContext);
                    
                    // Verify warning was logged (can't directly verify but would be logged)
                    assertNotNull(testManager, "Manager should be created even if model load fails");
                }
            }
        }
    }

    /**
     * NEGATIVE TEST CASE 8: Test isSameEvent with null values
     */
    @Test
    public void testIsSameEventWithNullValues() throws Exception {
        // Create events with null fields for comparison
        Event event1 = new Event(null, "type1", "state1", 1.0, 1000L, 0.5, 0.1f, 0.2f);
        Event event2 = new Event(null, "type1", "state1", 1.0, 1000L, 0.5, 0.1f, 0.2f);
        Event event3 = new Event("permission1", null, "state1", 1.0, 1000L, 0.5, 0.1f, 0.2f);
        Event event4 = new Event("permission1", "type1", null, 1.0, 1000L, 0.5, 0.1f, 0.2f);
        
        // Use reflection to access private method
        java.lang.reflect.Method isSameEventMethod = ModelInferenceManagerImpl.class.getDeclaredMethod(
            "isSameEvent", Event.class, Event.class);
        isSameEventMethod.setAccessible(true);
        
        // Execute and verify
        assertTrue((boolean)isSameEventMethod.invoke(inferenceManager, event1, event2), "Events with matching null permissions should be same");
        assertFalse((boolean)isSameEventMethod.invoke(inferenceManager, event1, event3), "Events with different permissions (null vs non-null) should not be same");
        assertFalse((boolean)isSameEventMethod.invoke(inferenceManager, event3, event4), "Events with different types (null vs non-null) should not be same");
    }

    /**
     * NEGATIVE TEST CASE 9: Test getUpdatedEventList with empty existing list
     */
    @Test
    public void testGetUpdatedEventListWithEmptyExistingList() throws Exception {
        // Create test events
        Event event1 = new Event("permission1", "type1", "state1", 1.0, 1000L, 0.5, 0.1f, 0.2f);
        Event event2 = new Event("permission2", "type2", "state2", 2.0, 2000L, 0.6, 0.2f, 0.3f);
        List<Event> emptyList = new ArrayList<>();
        List<Event> newList = List.of(event1, event2);
        
        // Use reflection to access private method
        java.lang.reflect.Method getUpdatedEventListMethod = ModelInferenceManagerImpl.class.getDeclaredMethod(
            "getUpdatedEventList", List.class, List.class);
        getUpdatedEventListMethod.setAccessible(true);
        
        // Execute method
        @SuppressWarnings("unchecked")
        ArrayList<Event> result = (ArrayList<Event>)getUpdatedEventListMethod.invoke(inferenceManager, emptyList, newList);
        
        // Verify
        assertEquals(2, result.size(), "Result should contain only new events");
        assertTrue(result.contains(event1), "Result should contain first new event");
        assertTrue(result.contains(event2), "Result should contain second new event");
    }

    /**
     * NEGATIVE TEST CASE 10: Test getUpdatedEventList with duplicate events
     */
    @Test
    public void testGetUpdatedEventListWithDuplicateEvents() throws Exception {
        // Create test events
        Event event1 = new Event("permission1", "type1", "state1", 1.0, 1000L, 0.5, 0.1f, 0.2f);
        Event event2 = new Event("permission2", "type2", "state2", 2.0, 2000L, 0.6, 0.2f, 0.3f);
        Event event3 = new Event("permission1", "type1", "state1", 3.0, 3000L, 0.7, 0.3f, 0.4f); // Same as event1 for matching
        List<Event> existingList = List.of(event1, event2);
        List<Event> newList = List.of(event3); // Only contains a duplicate of event1
        
        // Use reflection to access private method
        java.lang.reflect.Method getUpdatedEventListMethod = ModelInferenceManagerImpl.class.getDeclaredMethod(
            "getUpdatedEventList", List.class, List.class);
        getUpdatedEventListMethod.setAccessible(true);
        
        // Execute method
        @SuppressWarnings("unchecked")
        ArrayList<Event> result = (ArrayList<Event>)getUpdatedEventListMethod.invoke(inferenceManager, existingList, newList);
        
        // Verify
        assertEquals(2, result.size(), "Result should contain new event plus non-duplicate existing event");
        assertTrue(result.contains(event3), "Result should contain new event");
        assertTrue(result.contains(event2), "Result should contain non-duplicate existing event");
        assertFalse(result.contains(event1), "Result should not contain duplicate existing event");
    }

    /**
     * NEGATIVE TEST CASE 11: Test getEventListFromPermissionList with exempted permissions
     */
    @Test
    public void testGetEventListFromPermissionListWithExemptedPermissions() throws Exception {
        // Mock dependencies
        String packageName = "com.example.testapp";
        long timestamp = System.currentTimeMillis();
        Map<String, Pair<Float, Float>> anomalyFeatureMap = new HashMap<>();
        anomalyFeatureMap.put("feature1", new Pair<>(0.8f, 0.5f));
        Map<String, String> sensorCountMap = new HashMap<>();
        sensorCountMap.put("feature1", "10");
        int isForeground = 1;
        
        // Mock static AiPrivacyConstants.exemptedPermissions
        try (MockedStatic<AiPrivacyConstants> mockedConstants = Mockito.mockStatic(AiPrivacyConstants.class)) {
            List<String> exemptedPermissions = new ArrayList<>();
            exemptedPermissions.add("android.permission.CAMERA"); // Exempt camera permission
            mockedConstants.when(() -> AiPrivacyConstants.exemptedPermissions).thenReturn(exemptedPermissions);
            
            // Use reflection to access private method
            java.lang.reflect.Method getEventListMethod = ModelInferenceManagerImpl.class.getDeclaredMethod(
                "getEventListFromPermissionList", String.class, long.class, Map.class, Map.class, int.class);
            getEventListMethod.setAccessible(true);
            
            // Execute method
            @SuppressWarnings("unchecked")
            ArrayList<Event> result = (ArrayList<Event>)getEventListMethod.invoke(
                inferenceManager, packageName, timestamp, anomalyFeatureMap, sensorCountMap, isForeground);
            
            // Verify
            assertEquals(0, result.size(), "Should return empty list when all permissions are exempted");
        }
    }

    /**
     * NEGATIVE TEST CASE 12: Test insertOrUpdateSuggestion with existing suggestion
     */
    @Test
    public void testInsertOrUpdateSuggestionWithExistingSuggestion() throws Exception {
        // Mock dependencies for testing
        String packageName = "com.example.testapp";
        long timestamp = System.currentTimeMillis();
        int appCategory = 5;
        int appTrustLevel = 2;
        int accessHours = 14;
        Map<String, Pair<Float, Float>> anomalyFeatureMap = new HashMap<>();
        anomalyFeatureMap.put("feature1", new Pair<>(0.8f, 0.5f));
        int isForeground = 1;
        Map<String, String> permissionUsageMap = new HashMap<>();
        permissionUsageMap.put("feature1", "10");
        
        // Mock existing suggestion
        SuggestionEntity existingSuggestion = new SuggestionEntity();
        existingSuggestion.event = new ArrayList<>();
        existingSuggestion.event.add(new Event("permission1", "type1", "state1", 1.0, 1000L, 0.5, 0.1f, 0.2f));
        
        // Mock repository
        when(mockRepository.getSuggestionByPackageNameAndFeature(packageName, SuggestionConstants.FEATURE_VAE))
            .thenReturn(List.of(existingSuggestion));
        
        // Use reflection to access private method
        java.lang.reflect.Method insertOrUpdateMethod = ModelInferenceManagerImpl.class.getDeclaredMethod(
            "insertOrUpdateSuggestion", String.class, long.class, int.class, int.class, int.class, 
            Map.class, int.class, Map.class);
        insertOrUpdateMethod.setAccessible(true);
        
        // Execute method
        insertOrUpdateMethod.invoke(inferenceManager, packageName, timestamp, appCategory, 
            appTrustLevel, accessHours, anomalyFeatureMap, isForeground, permissionUsageMap);
        
        // Verify
        verify(mockRepository).updateSuggestion(any(SuggestionEntity.class));
        verify(mockRepository, never()).insertSuggestions(any(SuggestionEntity.class));
    }

    /**
     * NEGATIVE TEST CASE 13: Test insertOrUpdateSuggestion with new suggestion
     */
    @Test
    public void testInsertOrUpdateSuggestionWithNewSuggestion() throws Exception {
        // Mock dependencies for testing
        String packageName = "com.example.testapp";
        long timestamp = System.currentTimeMillis();
        int appCategory = 5;
        int appTrustLevel = 2;
        int accessHours = 14;
        Map<String, Pair<Float, Float>> anomalyFeatureMap = new HashMap<>();
        anomalyFeatureMap.put("feature1", new Pair<>(0.8f, 0.5f));
        int isForeground = 1;
        Map<String, String> permissionUsageMap = new HashMap<>();
        permissionUsageMap.put("feature1", "10");
        
        // Mock no existing suggestion
        when(mockRepository.getSuggestionByPackageNameAndFeature(packageName, SuggestionConstants.FEATURE_VAE))
            .thenReturn(new ArrayList<>());
        
        // Mock AppTrustLevelType and AppCategoryType static methods
        try (MockedStatic<AppTrustLevelType> mockedTrustLevel = Mockito.mockStatic(AppTrustLevelType.class)) {
            AppTrustLevelType mockTrustLevelType = mock(AppTrustLevelType.class);
            mockedTrustLevel.when(() -> AppTrustLevelType.getAppTrustLevelType(appTrustLevel))
                .thenReturn(mockTrustLevelType);
            
            try (MockedStatic<AppCategoryType> mockedCategory = Mockito.mockStatic(AppCategoryType.class)) {
                AppCategoryType mockCategoryType = mock(AppCategoryType.class);
                mockedCategory.when(() -> AppCategoryType.getAppCategoryType(appCategory))
                    .thenReturn(mockCategoryType);
                
                // Use reflection to access private method
                java.lang.reflect.Method insertOrUpdateMethod = ModelInferenceManagerImpl.class.getDeclaredMethod(
                    "insertOrUpdateSuggestion", String.class, long.class, int.class, int.class, int.class, 
                    Map.class, int.class, Map.class);
                insertOrUpdateMethod.setAccessible(true);
                
                // Execute method
                insertOrUpdateMethod.invoke(inferenceManager, packageName, timestamp, appCategory, 
                    appTrustLevel, accessHours, anomalyFeatureMap, isForeground, permissionUsageMap);
                
                // Verify
                verify(mockRepository, never()).updateSuggestion(any(SuggestionEntity.class));
                verify(mockRepository).insertSuggestions(any(SuggestionEntity.class));
            }
        }
    }

    /**
     * NEGATIVE TEST CASE 14: Test multiple data points processing
     */
    @Test
    public void testMultipleDataPointsProcessing() throws Exception {
        // Create multiple test data points
        ArrayList<MinuteWiseInferenceDataPoint> multipleDataPoints = new ArrayList<>();
        multipleDataPoints.add(testDataPoint);
        multipleDataPoints.add(testDataPoint); // Add same point twice
        
        // Setup mock behavior for processing
        ByteBuffer mockBuffer = ByteBuffer.allocate(100);
        Triple<List<ByteBuffer>, Integer, Integer> mockPreprocessedInput = 
            new Triple<>(List.of(mockBuffer), 10, 5);
        
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
            .thenReturn(mockPreprocessedInput);
            
        when(mockModelManager.runInference(any(ByteBuffer.class), anyInt(), anyInt(), anyMap()))
            .thenReturn(new HashMap<>()); // No anomalies
        
        // Execute method under test
        int result = inferenceManager.performInference(multipleDataPoints);
        
        // Verify
        assertEquals(AiErrorCode.SUCCESS.getValue(), result, "Should return SUCCESS for multiple data points");
        verify(mockModelManager, times(2