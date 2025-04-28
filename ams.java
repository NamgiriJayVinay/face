give for this test cases for 5: 15 as pos:neg tcs along with coments of having each case as positive or negative with tc number : 
public class ModelInferenceManagerImpl implements ModelInferenceManager {

    private final String TAG = AiPrivacyConstants.TAG + ModelInferenceManagerImpl.class.getSimpleName();
    private final ModelManager modelManager;
    HashMap<String, String> sensorCountMap = new HashMap<String, String>();
    HashMap<String, Pair<String, String>> featurePermissionTagMap = new HashMap<String, Pair<String, String>>();
    SuggestionScoreUtil suggestionScoreUtil = new SuggestionScoreUtil();
    SuggestionDao suggestionDao;
    AiPrivacyRepository aiPrivacyRepository;
    ExecutorService executorService;
    Future<Void> result = null;

    public ModelInferenceManagerImpl(Context context) {
        modelManager = ModelManager.getInstance(context);
        boolean loadModelSuccessFlag = modelManager.loadModel();
        featurePermissionTagMap = AIModelAttributes.getFeaturePermissionTypeMap();
        suggestionDao = AppDatabase.getInstance(context).suggestionDao();
        aiPrivacyRepository = new AiPrivacyRepository(context);
        executorService = Executors.newSingleThreadExecutor();
        if (!loadModelSuccessFlag) {
            Log.w(TAG, "Model Load Failed...");
        }
    }

    @Override
    public int performInference(ArrayList<MinuteWiseInferenceDataPoint> inferenceDataPoints) {
        if (modelManager == null) {
            Log.e(TAG, "Model manager is NULL Inference stopped");
            return AiErrorCode.MODEL_LOAD_ERROR.getValue();
        }
        synchronized (this) {
            try {
                Log.i(TAG, "Model inference started");
                long inferenceStartTime = System.currentTimeMillis();
                int inputRecordSize = inferenceDataPoints.size();
                for (int i = 0; i < inputRecordSize; i++) {
                    this.sensorCountMap.clear();
                    fillSensorCountMap(inferenceDataPoints.get(i), sensorCountMap);
                    Log.i(TAG, "Sensor count map :: " + sensorCountMap);
                    Triple<List<ByteBuffer>, Integer, Integer> preProcessedInput = this.modelManager.prepareInputBatchesForInference(1, sensorCountMap);
                    Map<String, Pair<Float, Float>> anomaliesMap = this.modelManager.runInference(
                            preProcessedInput.getFirst().get(0),
                            preProcessedInput.getSecond(),
                            preProcessedInput.getThird(),
                            sensorCountMap
                    );
                    Log.i(TAG, "Anomalies found in feature :: " + anomaliesMap.keySet());
                    if (!anomaliesMap.isEmpty()) { // anomaly found
                        Log.i(TAG, "Anomalies Found ......");
                        String packageName = inferenceDataPoints.get(i).minuteWisePreProcessEntityForVAE.packageName;
                        int appCategory = inferenceDataPoints.get(i).minuteWisePreProcessEntityForVAE.appCategory;
                        int appTrustLevel = inferenceDataPoints.get(i).minuteWisePreProcessEntityForVAE.appTrustLevel;
                        long timestamp = inferenceDataPoints.get(i).minuteWisePreProcessEntityForVAE.timestamp;
                        int isForeground = inferenceDataPoints.get(i).minuteWisePreProcessEntityForVAE.isForeground;
                        int accessHours = (int) inferenceDataPoints.get(i).minuteWisePreProcessEntityForVAE.accessHour;
                        Log.i(TAG, "Insert in the suggestion entity");
                        result = (Future<Void>) executorService.submit(() -> {
                            HashMap<String, String> permissionUsageMap = new HashMap<>(sensorCountMap);
                            insertOrUpdateSuggestion(packageName, timestamp, appCategory, appTrustLevel, accessHours, anomaliesMap, isForeground, permissionUsageMap);
                        });
                        result.get();
                    } else {
                        Log.i(TAG, "No Anomaly Found...");
                    }
                }
                Log.i(TAG, "Vae inference time: " + (System.currentTimeMillis() - inferenceStartTime) + "ms" + " for total input records: " + inputRecordSize);
            } catch (Exception ex) {
                Log.e(TAG, "Error while performing inference :: " + ex.getMessage());
                return AiErrorCode.PREPROCESSING_ERROR.getValue();
            }
        }
        return AiErrorCode.SUCCESS.getValue();
    }

    private void fillSensorCountMap(MinuteWiseInferenceDataPoint minuteWiseInferenceDataPoint, HashMap<String, String> sensorCountMap) {
        sensorCountMap.put(AIModelAttributes.GRANTED_PERMISSION_ATTRIBUTE_KEY, minuteWiseInferenceDataPoint.minuteWisePreProcessEntityForVAE.grantedPermissions);
        sensorCountMap.put(AIModelAttributes.AIModelAttributeNames.BACKGROUND_FEATURE.getValue(), Integer.toString(minuteWiseInferenceDataPoint.minuteWisePreProcessEntityForVAE.isForeground));
        sensorCountMap.put(AIModelAttributes.AIModelAttributeNames.APP_CATEGORY.getValue(), Integer.toString(minuteWiseInferenceDataPoint.minuteWisePreProcessEntityForVAE.appCategory));
        sensorCountMap.put(AIModelAttributes.AIModelAttributeNames.APP_TRUST_LEVEL.getValue(), Integer.toString(minuteWiseInferenceDataPoint.minuteWisePreProcessEntityForVAE.appTrustLevel));
        sensorCountMap.put(AIModelAttributes.AIModelAttributeNames.ACCESS_HOURS.getValue(), Integer.toString(minuteWiseInferenceDataPoint.minuteWisePreProcessEntityForVAE.accessHour));

        for (AIModelAttributes.AIModelAttributeNames permissionFeature : AIModelAttributes.AIModelAttributeNames.values()) {
            try {
                int val = minuteWiseInferenceDataPoint.getFeatureValue(permissionFeature);
                if (val > 0) {
                    sensorCountMap.put(permissionFeature.getValue(), Integer.toString(val));
                }
            } catch (FeatureNotFoundException featureNotFoundException) {
                Log.i(TAG, "Feature not found error");
            } catch (Exception ex) {
                Log.e(TAG, "Error occurred while fetching feature value :: " + ex.getMessage());
            }
        }
    }

    public ArrayList<String> getAnomalyFeatures(Boolean[] anomalies, HashMap<String, String> sensorCountMap) {
        ArrayList<String> anomalyFeatures = new ArrayList<String>();
        ArrayList<String> aiNumericalFeatures = AIModelAttributes.getNumericalFeatures();
        for (int i = 0; i < aiNumericalFeatures.size(); i++) {
            if (anomalies[i] && sensorCountMap.containsKey(aiNumericalFeatures.get(i))) {
                anomalyFeatures.add(aiNumericalFeatures.get(i));
            }
        }
        return anomalyFeatures;
    }

    private void insertOrUpdateSuggestion(String packageName, long timestamp, int appCategory, int appTrustLevel, int accessHours, Map<String, Pair<Float, Float>> anomalyFeatureMap, int isForeground, Map<String, String> permissionUsageCountMap) {
        List<Event> eventList = getEventListFromPermissionList(packageName, timestamp, anomalyFeatureMap, permissionUsageCountMap, isForeground);

        List<SuggestionEntity> existingSuggestionList = aiPrivacyRepository.getSuggestionByPackageNameAndFeature(packageName, SuggestionConstants.FEATURE_VAE);
        if (!existingSuggestionList.isEmpty()) {
            SuggestionEntity suggestionEntity = existingSuggestionList.get(0);
            List<Event> existingEventList = suggestionEntity.event;
            List<Event> upatedEventList = getUpdatedEventList(existingEventList, eventList);
            for (int j = 0; j < upatedEventList.size(); j++) {
                upatedEventList.get(j).setScore(
                        suggestionScoreUtil.getEventSuggestionScore(
                                upatedEventList.get(j),
                                appCategory,
                                eventList.size()
                        )
                );
            }
            suggestionEntity.event = upatedEventList;
            suggestionEntity.lastTimestamp = timestamp;
            suggestionEntity.suggestionScore = suggestionScoreUtil.getSuggestionScore(
                    appTrustLevel,
                    appCategory,
                    accessHours,
                    upatedEventList
            );
            aiPrivacyRepository.updateSuggestion(suggestionEntity);
        } else {
            AppTrustLevelType appTrustLevelType = AppTrustLevelType.getAppTrustLevelType(appTrustLevel);
            AppCategoryType appCategoryType = AppCategoryType.getAppCategoryType(appCategory);
            double suggestionScore = suggestionScoreUtil.getSuggestionScore(
                    appTrustLevel,
                    appCategory,
                    accessHours,
                    eventList
            );
            for (int j = 0; j < eventList.size(); j++) {
                eventList.get(j).setScore(
                        suggestionScoreUtil.getEventSuggestionScore(
                                eventList.get(j),
                                appCategory,
                                eventList.size()
                        )
                );
            }
            SuggestionEntity suggestion = new SuggestionEntity(
                    timestamp,
                    packageName,
                    appTrustLevelType,
                    appCategoryType,
                    SuggestionConstants.FEATURE_VAE,
                    eventList,
                    suggestionScore
            );
            aiPrivacyRepository.insertSuggestions(suggestion);
        }
    }

    private ArrayList<Event> getEventListFromPermissionList(String packageName, long timestamp, Map<String, Pair<Float, Float>> anomalyFeatureMap, Map<String, String> sensorCountMap, int isForeground) {
        ArrayList<Event> eventList = new ArrayList<Event>();
        for (String anomalyFeat : anomalyFeatureMap.keySet()) {
            if(AiPrivacyConstants.exemptedPermissions.contains(featurePermissionTagMap.get(anomalyFeat).first)) {
                Log.i(TAG, "Permission :: " + featurePermissionTagMap.get(anomalyFeat) + " detected and skipped.");
                continue;
            }
            Event event = new Event(
                    featurePermissionTagMap.get(anomalyFeat).first,
                    featurePermissionTagMap.get(anomalyFeat).second,
                    isForeground == 1 ? SuggestionConstants.FOREGROUND_STATE : SuggestionConstants.BACKGROUND_STATE,
                    Integer.parseInt(sensorCountMap.get(anomalyFeat)),
                    timestamp,
                    0,
                    anomalyFeatureMap.get(anomalyFeat).first,
                    anomalyFeatureMap.get(anomalyFeat).second
            );
            event.setTotalForegroundUsage(
                    aiPrivacyRepository.getPermissionUsageLast24Hours(
                            featurePermissionTagMap.get(anomalyFeat).first,
                            packageName,
                            timestamp,
                            AccessStateType.FOREGROUND.getValue()

                    )
            );
            event.setTotalBackgroundUsage(
                    aiPrivacyRepository.getPermissionUsageLast24Hours(
                            featurePermissionTagMap.get(anomalyFeat).first,
                            packageName,
                            timestamp,
                            AccessStateType.BACKGROUND.getValue()
                    )
            );
            eventList.add(event);
        }
        return eventList;
    }

    private ArrayList<Event> getUpdatedEventList(List<Event> existingEventList, List<Event> newEventList) {
        ArrayList<Event> updatedEventList = new ArrayList<Event>(newEventList);
        for (Event existingEvent : existingEventList) {
            boolean isSameEventPresent = false;
            for (Event newEvent : newEventList) {
                if (isSameEvent(newEvent, existingEvent)) {
                    isSameEventPresent = true;
                    break;
                }
            }
            if (!isSameEventPresent) {
                updatedEventList.add(existingEvent);
            }
        }
        return updatedEventList;
    }

    private boolean isSameEvent(Event a, Event b) {
        return (Objects.equals(a.getPermission(), b.getPermission())) && (Objects.equals(a.getType(), b.getType())) && (Objects.equals(a.getState(), b.getState()));
    }

}
which uses public interface ModelInferenceManager {

    int performInference(ArrayList<MinuteWiseInferenceDataPoint> inferenceDataPoints);

}


@@@@@@@@@

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RunWith(MockitoJUnitRunner.class)
public class ModelInferenceManagerImplTest {

    @Mock
    private Context mockContext;
    
    @Mock
    private ModelManager mockModelManager;
    
    @Mock
    private SuggestionDao mockSuggestionDao;
    
    @Mock
    private AiPrivacyRepository mockAiPrivacyRepository;
    
    @Mock
    private ExecutorService mockExecutorService;
    
    @Mock
    private Future<Void> mockFuture;
    
    private ModelInferenceManagerImpl modelInferenceManager;
    
    @Before
    public void setUp() throws Exception {
        // Set up ModelManager.getInstance to return our mock
        ModelManager.setInstance(mockModelManager);
        
        // Mock AppDatabase to return our mock SuggestionDao
        AppDatabase mockAppDatabase = mock(AppDatabase.class);
        when(mockAppDatabase.suggestionDao()).thenReturn(mockSuggestionDao);
        AppDatabase.setInstance(mockAppDatabase);
        
        // Create a spy of the class under test so we can mock some methods
        modelInferenceManager = spy(new ModelInferenceManagerImpl(mockContext));
        
        // Replace the repository and executor with our mocks
        modelInferenceManager.aiPrivacyRepository = mockAiPrivacyRepository;
        modelInferenceManager.executorService = mockExecutorService;
        modelInferenceManager.result = mockFuture;
        
        // Mock the executor service to execute tasks immediately
        when(mockExecutorService.submit(any(Runnable.class))).thenReturn(mockFuture);
    }
    
    /**
     * TC-P1: Valid inference with single data point and anomaly detected
     */
    @Test
    public void testPerformInference_SingleDataPointWithAnomaly() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        setupMockForSuccessfulInferenceWithAnomaly();
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
        verify(mockAiPrivacyRepository, times(1)).insertSuggestions(any(SuggestionEntity.class));
    }
    
    /**
     * TC-P2: Valid inference with multiple data points, some with anomalies
     */
    @Test
    public void testPerformInference_MultipleDataPointsSomeWithAnomalies() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createMultipleDataPoints(3);
        setupMockForMixedInferenceResults();
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
        // Verify that insertSuggestions was called at least once (for data points with anomalies)
        verify(mockAiPrivacyRepository, times(2)).insertSuggestions(any(SuggestionEntity.class));
    }
    
    /**
     * TC-P3: Valid inference with existing suggestions in database
     */
    @Test
    public void testPerformInference_WithExistingSuggestions() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        setupMockForSuccessfulInferenceWithAnomaly();
        
        // Setup existing suggestions
        List<SuggestionEntity> existingSuggestions = new ArrayList<>();
        existingSuggestions.add(createMockSuggestion("com.example.app"));
        when(mockAiPrivacyRepository.getSuggestionByPackageNameAndFeature(
                anyString(), anyString())).thenReturn(existingSuggestions);
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
        verify(mockAiPrivacyRepository, times(1)).updateSuggestion(any(SuggestionEntity.class));
        verify(mockAiPrivacyRepository, times(0)).insertSuggestions(any(SuggestionEntity.class));
    }
    
    /**
     * TC-P4: Valid inference with no anomalies detected
     */
    @Test
    public void testPerformInference_NoAnomalies() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        setupMockForNoAnomalies();
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
        // Verify that no suggestions were inserted or updated
        verify(mockAiPrivacyRepository, times(0)).insertSuggestions(any(SuggestionEntity.class));
        verify(mockAiPrivacyRepository, times(0)).updateSuggestion(any(SuggestionEntity.class));
    }
    
    /**
     * TC-P5: Valid inference with exempted permissions
     */
    @Test
    public void testPerformInference_WithExemptedPermissions() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        setupMockForAnomalyWithExemptedPermissions();
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
        // Verify that suggestions were still created but exempted permissions were skipped
        verify(mockAiPrivacyRepository, times(1)).insertSuggestions(any(SuggestionEntity.class));
    }
    
    /**
     * TC-N1: Null inferenceDataPoints parameter
     */
    @Test(expected = NullPointerException.class)
    public void testPerformInference_NullDataPoints() {
        modelInferenceManager.performInference(null);
    }
    
    /**
     * TC-N2: Empty inferenceDataPoints ArrayList
     */
    @Test
    public void testPerformInference_EmptyDataPoints() {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = new ArrayList<>();
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
    }
    
    /**
     * TC-N3: Model manager is null
     */
    @Test
    public void testPerformInference_NullModelManager() {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        modelInferenceManager.modelManager = null;
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.MODEL_LOAD_ERROR.getValue(), result);
    }
    
    /**
     * TC-N4: Model load fails
     */
    @Test
    public void testPerformInference_ModelLoadFails() {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        when(mockModelManager.loadModel()).thenReturn(false);
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert - Should still process successfully despite model load warning
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
    }
    
    /**
     * TC-N5: Exception during preprocessing
     */
    @Test
    public void testPerformInference_PreprocessingException() {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
                .thenThrow(new RuntimeException("Preprocessing error"));
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.PREPROCESSING_ERROR.getValue(), result);
    }
    
    /**
     * TC-N6: Exception during inference
     */
    @Test
    public void testPerformInference_InferenceException() {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        Triple<List<ByteBuffer>, Integer, Integer> mockTriple = 
                new Triple<>(List.of(ByteBuffer.allocate(10)), 1, 1);
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
                .thenReturn(mockTriple);
        when(mockModelManager.runInference(any(), anyInt(), anyInt(), anyMap()))
                .thenThrow(new RuntimeException("Inference error"));
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.PREPROCESSING_ERROR.getValue(), result);
    }
    
    /**
     * TC-N7: Exception during suggestion insertion
     */
    @Test
    public void testPerformInference_InsertionException() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        setupMockForSuccessfulInferenceWithAnomaly();
        
        // Make the executor task throw an exception
        when(mockFuture.get()).thenThrow(new RuntimeException("Database error"));
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.PREPROCESSING_ERROR.getValue(), result);
    }
    
    /**
     * TC-N8: Missing feature in data point
     */
    @Test
    public void testPerformInference_MissingFeature() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        MinuteWiseInferenceDataPoint mockDataPoint = dataPoints.get(0);
        
        // Make getFeatureValue throw FeatureNotFoundException for some calls
        doThrow(new FeatureNotFoundException("Feature not found"))
                .when(mockDataPoint).getFeatureValue(any(AIModelAttributes.AIModelAttributeNames.class));
        
        setupMockForSuccessfulInferenceWithAnomaly();
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert - Should still succeed despite feature not found errors
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
    }
    
    /**
     * TC-N9: Invalid feature value in data point
     */
    @Test
    public void testPerformInference_InvalidFeatureValue() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        MinuteWiseInferenceDataPoint mockDataPoint = dataPoints.get(0);
        
        // Make getFeatureValue throw a general exception for some calls
        doThrow(new RuntimeException("Invalid feature value"))
                .when(mockDataPoint).getFeatureValue(any(AIModelAttributes.AIModelAttributeNames.class));
        
        setupMockForSuccessfulInferenceWithAnomaly();
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert - Should still succeed despite feature value errors
        assertEquals(AiErrorCode.SUCCESS.getValue(), result);
    }
    
    /**
     * TC-N10: Database access error
     */
    @Test
    public void testPerformInference_DatabaseAccessError() throws Exception {
        // Arrange
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = createSingleDataPoint();
        setupMockForSuccessfulInferenceWithAnomaly();
        
        // Make the repository throw an exception
        doThrow(new RuntimeException("Database access error"))
                .when(mockAiPrivacyRepository).insertSuggestions(any(SuggestionEntity.class));
        when(mockFuture.get()).thenThrow(new RuntimeException("Database access error"));
        
        // Act
        int result = modelInferenceManager.performInference(dataPoints);
        
        // Assert
        assertEquals(AiErrorCode.PREPROCESSING_ERROR.getValue(), result);
    }
    
    // Helper methods to set up test data and mocks
    
    private ArrayList<MinuteWiseInferenceDataPoint> createSingleDataPoint() {
        MinuteWiseInferenceDataPoint dataPoint = mock(MinuteWiseInferenceDataPoint.class);
        MinuteWisePreProcessEntityForVAE entityForVAE = mock(MinuteWisePreProcessEntityForVAE.class);
        
        entityForVAE.packageName = "com.example.app";
        entityForVAE.appCategory = 1;
        entityForVAE.appTrustLevel = 2;
        entityForVAE.timestamp = System.currentTimeMillis();
        entityForVAE.isForeground = 1;
        entityForVAE.accessHour = 14;
        entityForVAE.grantedPermissions = "CAMERA,LOCATION";
        
        when(dataPoint.minuteWisePreProcessEntityForVAE).thenReturn(entityForVAE);
        
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = new ArrayList<>();
        dataPoints.add(dataPoint);
        return dataPoints;
    }
    
    private ArrayList<MinuteWiseInferenceDataPoint> createMultipleDataPoints(int count) {
        ArrayList<MinuteWiseInferenceDataPoint> dataPoints = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            MinuteWiseInferenceDataPoint dataPoint = mock(MinuteWiseInferenceDataPoint.class);
            MinuteWisePreProcessEntityForVAE entityForVAE = mock(MinuteWisePreProcessEntityForVAE.class);
            
            entityForVAE.packageName = "com.example.app" + i;
            entityForVAE.appCategory = i % 3;
            entityForVAE.appTrustLevel = i % 4;
            entityForVAE.timestamp = System.currentTimeMillis() + (i * 60000);
            entityForVAE.isForeground = i % 2;
            entityForVAE.accessHour = (i + 10) % 24;
            entityForVAE.grantedPermissions = "CAMERA,LOCATION";
            
            when(dataPoint.minuteWisePreProcessEntityForVAE).thenReturn(entityForVAE);
            dataPoints.add(dataPoint);
        }
        
        return dataPoints;
    }
    
    private void setupMockForSuccessfulInferenceWithAnomaly() throws Exception {
        // Mock the model manager methods
        Triple<List<ByteBuffer>, Integer, Integer> mockTriple = 
                new Triple<>(List.of(ByteBuffer.allocate(10)), 1, 1);
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
                .thenReturn(mockTriple);
        
        // Create anomaly map with one anomaly
        Map<String, Pair<Float, Float>> anomalyMap = new HashMap<>();
        anomalyMap.put("CAMERA_COUNT", new Pair<>(0.8f, 0.9f));
        
        when(mockModelManager.runInference(any(), anyInt(), anyInt(), anyMap()))
                .thenReturn(anomalyMap);
        
        // Mock the feature-permission mapping
        HashMap<String, Pair<String, String>> featureMap = new HashMap<>();
        featureMap.put("CAMERA_COUNT", new Pair<>("android.permission.CAMERA", "Camera"));
        modelInferenceManager.featurePermissionTagMap = featureMap;
        
        // Mock empty suggestion list for new suggestion creation
        when(mockAiPrivacyRepository.getSuggestionByPackageNameAndFeature(anyString(), anyString()))
                .thenReturn(new ArrayList<>());
    }
    
    private void setupMockForMixedInferenceResults() throws Exception {
        // Mock the model manager methods
        Triple<List<ByteBuffer>, Integer, Integer> mockTriple = 
                new Triple<>(List.of(ByteBuffer.allocate(10)), 1, 1);
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
                .thenReturn(mockTriple);
        
        // First call - return anomalies
        Map<String, Pair<Float, Float>> anomalyMap1 = new HashMap<>();
        anomalyMap1.put("CAMERA_COUNT", new Pair<>(0.8f, 0.9f));
        
        // Second call - return empty map (no anomalies)
        Map<String, Pair<Float, Float>> anomalyMap2 = new HashMap<>();
        
        // Third call - return anomalies again
        Map<String, Pair<Float, Float>> anomalyMap3 = new HashMap<>();
        anomalyMap3.put("LOCATION_COUNT", new Pair<>(0.7f, 0.85f));
        
        when(mockModelManager.runInference(any(), anyInt(), anyInt(), anyMap()))
                .thenReturn(anomalyMap1)
                .thenReturn(anomalyMap2)
                .thenReturn(anomalyMap3);
        
        // Mock the feature-permission mapping
        HashMap<String, Pair<String, String>> featureMap = new HashMap<>();
        featureMap.put("CAMERA_COUNT", new Pair<>("android.permission.CAMERA", "Camera"));
        featureMap.put("LOCATION_COUNT", new Pair<>("android.permission.ACCESS_FINE_LOCATION", "Location"));
        modelInferenceManager.featurePermissionTagMap = featureMap;
        
        // Mock empty suggestion list for new suggestion creation
        when(mockAiPrivacyRepository.getSuggestionByPackageNameAndFeature(anyString(), anyString()))
                .thenReturn(new ArrayList<>());
    }
    
    private void setupMockForNoAnomalies() throws Exception {
        // Mock the model manager methods
        Triple<List<ByteBuffer>, Integer, Integer> mockTriple = 
                new Triple<>(List.of(ByteBuffer.allocate(10)), 1, 1);
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
                .thenReturn(mockTriple);
        
        // Return empty anomaly map - no anomalies found
        Map<String, Pair<Float, Float>> emptyAnomalyMap = new HashMap<>();
        when(mockModelManager.runInference(any(), anyInt(), anyInt(), anyMap()))
                .thenReturn(emptyAnomalyMap);
    }
    
    private void setupMockForAnomalyWithExemptedPermissions() throws Exception {
        // Mock the model manager methods
        Triple<List<ByteBuffer>, Integer, Integer> mockTriple = 
                new Triple<>(List.of(ByteBuffer.allocate(10)), 1, 1);
        when(mockModelManager.prepareInputBatchesForInference(anyInt(), anyMap()))
                .thenReturn(mockTriple);
        
        // Create anomaly map with both normal and exempted anomalies
        Map<String, Pair<Float, Float>> anomalyMap = new HashMap<>();
        anomalyMap.put("CAMERA_COUNT", new Pair<>(0.8f, 0.9f));
        anomalyMap.put("EXEMPTED_PERMISSION_COUNT", new Pair<>(0.75f, 0.85f));
        
        when(mockModelManager.runInference(any(), anyInt(), anyInt(), anyMap()))
                .thenReturn(anomalyMap);
        
        // Mock the feature-permission mapping
        HashMap<String, Pair<String, String>> featureMap = new HashMap<>();
        featureMap.put("CAMERA_COUNT", new Pair<>("android.permission.CAMERA", "Camera"));
        featureMap.put("EXEMPTED_PERMISSION_COUNT", new Pair<>("android.permission.EXEMPTED", "Exempted"));
        modelInferenceManager.featurePermissionTagMap = featureMap;
        
        // Add exempted permission to the list
        AiPrivacyConstants.exemptedPermissions.add("android.permission.EXEMPTED");
        
        // Mock empty suggestion list for new suggestion creation
        when(mockAiPrivacyRepository.getSuggestionByPackageNameAndFeature(anyString(), anyString()))
                .thenReturn(new ArrayList<>());
    }
    
    private SuggestionEntity createMockSuggestion(String packageName) {
        SuggestionEntity suggestion = new SuggestionEntity();
        suggestion.packageName = packageName;
        suggestion.feature = SuggestionConstants.FEATURE_VAE;
        suggestion.lastTimestamp = System.currentTimeMillis() - 3600000; // 1 hour ago
        suggestion.event = new ArrayList<>();
        suggestion.event.add(new Event("android.permission.CAMERA", "Camera", 
                SuggestionConstants.FOREGROUND_STATE, 5, System.currentTimeMillis() - 3600000, 0, 0.7f, 0.8f));
        return suggestion;
    }
}