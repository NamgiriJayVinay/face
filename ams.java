
public class MinuteWiseInferenceDataPoint {

    private static final String TAG = "MinuteWiseInferenceDataPoint";

    public AiMinuteDataEntity minuteWisePreProcessEntityForVAE; // minute wise usage
    public PeriodicUsageDataPoint periodicUsageDataPointForVAE; // periodic usage
    public HashMap<AIModelAttributeNames, Integer> featureValuesMap = new HashMap<AIModelAttributeNames, Integer>();

    public MinuteWiseInferenceDataPoint(
            AiMinuteDataEntity minuteWisePreProcessEntity,
            PeriodicUsageDataPoint periodicUsageDataPoint
    ) {
        this.minuteWisePreProcessEntityForVAE = minuteWisePreProcessEntity;
        this.periodicUsageDataPointForVAE = periodicUsageDataPoint;

        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_CAMERA, this.minuteWisePreProcessEntityForVAE.permissionUsedCamera);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_MICROPHONE, this.minuteWisePreProcessEntityForVAE.permissionUsedMicrophone);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_GET_LOCATION, this.minuteWisePreProcessEntityForVAE.permissionUsedGetLocation);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_COARSE_LOCATION, this.minuteWisePreProcessEntityForVAE.permissionUsedCoarseLocation);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_SEND_SMS, this.minuteWisePreProcessEntityForVAE.permissionUsedSendSMS);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_CAMERA_HOURLY, this.periodicUsageDataPointForVAE.permissionUsedCameraHourly);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_CAMERA_DAILY, this.periodicUsageDataPointForVAE.permissionUsedCameraDaily);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_MICROPHONE_HOURLY, this.periodicUsageDataPointForVAE.permissionUsedMicrophoneHourly);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_MICROPHONE_DAILY, this.periodicUsageDataPointForVAE.permissionUsedMicrophoneDaily);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_GET_LOCATION_HOURLY, this.periodicUsageDataPointForVAE.permissionUsedGetLocationHourly);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_GET_LOCATION_DAILY, this.periodicUsageDataPointForVAE.permissionUsedGetLocationDaily);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_COARSE_LOCATION_HOURLY, this.periodicUsageDataPointForVAE.permissionUsedCoarseLocationHourly);
        featureValuesMap.put(AIModelAttributeNames.PERMISSION_USED_COARSE_LOCATION_DAILY, this.periodicUsageDataPointForVAE.permissionUsedCoarseLocationDaily);

        featureValuesMap.put(AIModelAttributeNames.CAMERA_DURATION, (int) this.minuteWisePreProcessEntityForVAE.cameraDuration);
        featureValuesMap.put(AIModelAttributeNames.LOCATION_DURATION, (int) this.minuteWisePreProcessEntityForVAE.locationDuration);
        featureValuesMap.put(AIModelAttributeNames.MICROPHONE_DURATION, (int) this.minuteWisePreProcessEntityForVAE.microphoneDuration);
        featureValuesMap.put(AIModelAttributeNames.PING_CAMERA_HOURLY, this.periodicUsageDataPointForVAE.pingCameraHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_CAMERA_DAILY, this.periodicUsageDataPointForVAE.pingCameraDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_MICROPHONE_HOURLY, this.periodicUsageDataPointForVAE.pingMicrophoneHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_MICROPHONE_DAILY, this.periodicUsageDataPointForVAE.pingMicrophoneDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_GET_LOCATION_HOURLY, this.periodicUsageDataPointForVAE.pingGetLocationHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_GET_LOCATION_DAILY, this.periodicUsageDataPointForVAE.pingGetLocationDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_COARSE_LOCATION_HOURLY, this.periodicUsageDataPointForVAE.pingCoarseLocationHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_COARSE_LOCATION_DAILY, this.periodicUsageDataPointForVAE.pingCoarseLocationDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_SMS_HOURLY, this.periodicUsageDataPointForVAE.pingReadSMSHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_SMS_DAILY, this.periodicUsageDataPointForVAE.pingReadSMSDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_CONTACTS_HOURLY, this.periodicUsageDataPointForVAE.pingReadContactsHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_CONTACTS_DAILY, this.periodicUsageDataPointForVAE.pingReadContactsDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_CALL_LOGS_HOURLY, this.periodicUsageDataPointForVAE.pingReadCallLogsHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_CALL_LOGS_DAILY, this.periodicUsageDataPointForVAE.pingReadCallLogsDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_PHOTOS_HOURLY, this.periodicUsageDataPointForVAE.pingReadPhotosHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_PHOTOS_DAILY, this.periodicUsageDataPointForVAE.pingReadPhotosDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_VIDEOS_HOURLY, this.periodicUsageDataPointForVAE.pingReadVideosHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_VIDEOS_DAILY, this.periodicUsageDataPointForVAE.pingReadVideosDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_PHONE_HOURLY, this.periodicUsageDataPointForVAE.pingPhoneHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_PHONE_DAILY, this.periodicUsageDataPointForVAE.pingPhoneDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_NEAR_BY_DEVICES_HOURLY, this.periodicUsageDataPointForVAE.pingNearByDevicesHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_NEAR_BY_DEVICES_DAILY, this.periodicUsageDataPointForVAE.pingNearByDevicesDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_ACTIVITY_RECOGNITION_HOURLY, this.periodicUsageDataPointForVAE.pingActivityRecognitionHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_ACTIVITY_RECOGNITION_DAILY, this.periodicUsageDataPointForVAE.pingActivityRecognitionDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_MEDIA_AURAL_HOURLY, this.periodicUsageDataPointForVAE.pingReadMediaAuralHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_READ_MEDIA_AURAL_DAILY, this.periodicUsageDataPointForVAE.pingReadMediaAuralDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_STORAGE_HOURLY, this.periodicUsageDataPointForVAE.pingStorageHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_STORAGE_DAILY, this.periodicUsageDataPointForVAE.pingStorageDaily);
        featureValuesMap.put(AIModelAttributeNames.PING_CALENDAR_HOURLY, this.periodicUsageDataPointForVAE.pingCalendarHourly);
        featureValuesMap.put(AIModelAttributeNames.PING_CALENDAR_DAILY, this.periodicUsageDataPointForVAE.pingCalendarDaily);
        featureValuesMap.put(AIModelAttributeNames.BACKGROUND_FEATURE, this.minuteWisePreProcessEntityForVAE.isForeground);
        featureValuesMap.put(AIModelAttributeNames.APP_CATEGORY, this.minuteWisePreProcessEntityForVAE.appCategory);
        featureValuesMap.put(AIModelAttributeNames.APP_TRUST_LEVEL, this.minuteWisePreProcessEntityForVAE.appTrustLevel);
        featureValuesMap.put(AIModelAttributeNames.ACCESS_HOURS, this.minuteWisePreProcessEntityForVAE.accessHour);

    }

    public int getFeatureValue(AIModelAttributeNames feature) {
        if (featureValuesMap.containsKey(feature)) {
            return featureValuesMap.get(feature);
        } else {
            return 0;
        }
    }
}
