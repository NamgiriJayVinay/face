
@Entity(tableName = TABLE_AI_PERIODIC_DATA)
public class AiPeriodicDataEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "package_name")
    public String packageName;

    @ColumnInfo(name = "last_update_time")
    public long lastUpdateTime;

    @ColumnInfo(name = "access_date")
    public long accessDate;

    @ColumnInfo(name = "access_hour")
    public long accessHour;

    @ColumnInfo(name = "permission_used_camera_hourly")
    public int permissionUsedCameraHourly;

    @ColumnInfo(name = "ping_camera_hourly")
    public int pingCameraHourly;

    @ColumnInfo(name = "permission_used_microphone_hourly")
    public int permissionUsedMicrophoneHourly;

    @ColumnInfo(name = "ping_microphone_hourly")
    public int pingMicrophoneHourly;

    @ColumnInfo(name = "permission_used_get_location_hourly")
    public int permissionUsedGetLocationHourly;

    @ColumnInfo(name = "ping_get_location_hourly")
    public int pingGetLocationHourly;

    @ColumnInfo(name = "permission_used_coarse_location_hourly")
    public int permissionUsedCoarseLocationHourly;

    @ColumnInfo(name = "ping_coarse_location_hourly")
    public int pingCoarseLocationHourly;

    @ColumnInfo(name = "ping_read_sms_hourly")
    public int pingReadSMSHourly;

    @ColumnInfo(name = "ping_read_contacts_hourly")
    public int pingReadContactsHourly;

    @ColumnInfo(name = "ping_read_call_logs_hourly")
    public int pingReadCallLogsHourly;

    @ColumnInfo(name = "ping_phone_hourly")
    public int pingPhoneHourly;

    @ColumnInfo(name = "ping_near_by_devices_hourly")
    public int pingNearByDevicesHourly;

    @ColumnInfo(name = "ping_activity_recognition_hourly")
    public int pingActivityRecognitionHourly;

    @ColumnInfo(name = "ping_read_photos_hourly")
    public int pingReadPhotosHourly;

    @ColumnInfo(name = "ping_read_videos_hourly")
    public int pingReadVideosHourly;

    @ColumnInfo(name = "ping_storage_hourly")
    public int pingStorageHourly;

    @ColumnInfo(name = "ping_calendar_hourly")
    public int pingCalendarHourly;

    @ColumnInfo(name = "ping_read_media_aural_hourly")
    public int pingReadMediaAuralHourly;

    @ColumnInfo(name = "is_foreground")
    public int isForeground;

    @ColumnInfo(name = "app_category")
    public int appCategory;

    @ColumnInfo(name = "app_trust_level")
    public int appTrustLevel;

    @ColumnInfo(name = "granted_permission")
    public String grantedPermission;

    public AiPeriodicDataEntity(String packageName, long accessDate, long accessHour, int permissionUsedCameraHourly, int pingCameraHourly, int permissionUsedMicrophoneHourly, int pingMicrophoneHourly, int permissionUsedGetLocationHourly, int pingGetLocationHourly, int permissionUsedCoarseLocationHourly, int pingCoarseLocationHourly, int pingReadSMSHourly, int pingReadContactsHourly, int pingReadCallLogsHourly, int pingReadPhotosHourly, int pingReadVideosHourly, int pingStorageHourly, int pingCalendarHourly, int pingPhoneHourly, int pingNearByDevicesHourly, int pingActivityRecognitionHourly, int pingReadMediaAuralHourly, long lastUpdateTime, int isForeground, int appCategory, int appTrustLevel, String grantedPermission) {        this.packageName = packageName;
        this.accessDate = accessDate;
        this.accessHour = accessHour;
        this.permissionUsedCameraHourly = permissionUsedCameraHourly;
        this.pingCameraHourly = pingCameraHourly;
        this.permissionUsedMicrophoneHourly = permissionUsedMicrophoneHourly;
        this.pingMicrophoneHourly = pingMicrophoneHourly;
        this.permissionUsedGetLocationHourly = permissionUsedGetLocationHourly;
        this.pingGetLocationHourly = pingGetLocationHourly;
        this.permissionUsedCoarseLocationHourly = permissionUsedCoarseLocationHourly;
        this.pingCoarseLocationHourly = pingCoarseLocationHourly;
        this.pingReadSMSHourly = pingReadSMSHourly;
        this.pingReadContactsHourly = pingReadContactsHourly;
        this.pingReadCallLogsHourly = pingReadCallLogsHourly;
        this.lastUpdateTime = lastUpdateTime;
        this.isForeground = isForeground;
        this.appCategory = appCategory;
        this.appTrustLevel = appTrustLevel;
        this.grantedPermission = grantedPermission;
        this.pingPhoneHourly = pingPhoneHourly;
        this.pingNearByDevicesHourly = pingNearByDevicesHourly;
        this.pingActivityRecognitionHourly = pingActivityRecognitionHourly;
        this.pingReadPhotosHourly = pingReadPhotosHourly;
        this.pingReadVideosHourly = pingReadVideosHourly;
        this.pingStorageHourly = pingStorageHourly;
        this.pingCalendarHourly = pingCalendarHourly;
        this.pingReadMediaAuralHourly = pingReadMediaAuralHourly;
    }
}
