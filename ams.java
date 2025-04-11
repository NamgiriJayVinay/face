
@Entity(tableName = TABLE_AI_MINUTE_DATA)
public class AiMinuteDataEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "package_name")
    public String packageName;

    @ColumnInfo(name = "app_category")
    public int appCategory;

    @ColumnInfo(name = "app_trust_level")
    public int appTrustLevel;

    @ColumnInfo(name = "access_minute")
    public long accessMinute;

    @ColumnInfo(name = "access_hour")
    public int accessHour;

    @ColumnInfo(name = "access_date")
    public int accessDate;

    @ColumnInfo(name = "granted_permissions")
    public String grantedPermissions;

    @ColumnInfo(name = "requested_permissions")
    public String requestedPermissions;

    @ColumnInfo(name = "is_foreground")
    public int isForeground;

    @ColumnInfo(name = "permission_used_camera")
    public int permissionUsedCamera;

    @ColumnInfo(name = "permission_used_microphone")
    public int permissionUsedMicrophone;

    @ColumnInfo(name = "permission_used_get_location")
    public int permissionUsedGetLocation;

    @ColumnInfo(name = "permission_used_coarse_location")
    public int permissionUsedCoarseLocation;

    @ColumnInfo(name = "permission_used_storage")
    public int permissionUsedStorage;

    @ColumnInfo(name = "permission_used_read_media_aural")
    public int permissionUsedReadMediaAural;

    @ColumnInfo(name = "permission_used_read_media_visual")
    public int permissionUsedReadMediaVisual;

    @ColumnInfo(name = "permission_used_access_videos")
    public int permissionUsedAccessVideos;

    @ColumnInfo(name = "permission_used_access_photos")
    public int permissionUsedAccessPhotos;

    @ColumnInfo(name = "permission_used_read_contacts")
    public int permissionUsedReadContacts;

    @ColumnInfo(name = "permission_used_edit_and_delete_contacts")
    public int permissionUsedEditAndDeleteContacts;

    @ColumnInfo(name = "permission_used_send_sms")
    public int permissionUsedSendSMS;

    @ColumnInfo(name = "permission_used_receive_sms")
    public int permissionUsedReceiveSMS;

    @ColumnInfo(name = "permission_used_read_sms")
    public int permissionUsedReadSMS;

    @ColumnInfo(name = "permission_used_near_by_devices")
    public int permissionUsedNearByDevices;

    @ColumnInfo(name = "permission_used_activity_recognition")
    public int permissionUsedActivityRecognition;

    @ColumnInfo(name = "permission_used_calendar")
    public int permissionUsedCalendar;

    @ColumnInfo(name = "permission_used_call_logs")
    public int permissionUsedCallLogs;

    @ColumnInfo(name = "permission_used_phone")
    public int permissionUsedPhone;

    @ColumnInfo(name = "camera_duration")
    public long cameraDuration;

    @ColumnInfo(name = "microphone_duration")
    public long microphoneDuration;

    @ColumnInfo(name = "location_duration")
    public long locationDuration;

    public AiMinuteDataEntity() {
        // Empty constructor
    }

    public AiMinuteDataEntity(long timestamp, int uid, String packageName, int appCategory, int appTrustLevel, long accessMinute, int accessHour, int accessDate, String grantedPermissions, String requestedPermissions, int isForeground, PermissionUsedCount permissionUsedCount, SensorDuration sensorDuration) {
        this.timestamp = timestamp;
        this.uid = uid;
        this.packageName = packageName;
        this.appCategory = appCategory;
        this.appTrustLevel = appTrustLevel;
        this.accessMinute = accessMinute;
        this.accessHour = accessHour;
        this.accessDate = accessDate;
        this.grantedPermissions = grantedPermissions;
        this.requestedPermissions = requestedPermissions;
        this.isForeground = isForeground;
        // all permission
        this.permissionUsedCamera = permissionUsedCount.getPermissionUsedCamera();
        this.permissionUsedMicrophone = permissionUsedCount.getPermissionUsedMicrophone();
        this.permissionUsedGetLocation = permissionUsedCount.getPermissionUsedGetLocation();
        this.permissionUsedCoarseLocation = permissionUsedCount.getPermissionUsedCoarseLocation();
        this.permissionUsedStorage = permissionUsedCount.getPermissionUsedStorage();
        this.permissionUsedReadMediaAural = permissionUsedCount.getPermissionUsedReadMediaAural();
        this.permissionUsedReadMediaVisual = permissionUsedCount.getPermissionUsedReadMediaVisual();
        this.permissionUsedAccessVideos = permissionUsedCount.getPermissionUsedAccessVideos();
        this.permissionUsedAccessPhotos = permissionUsedCount.getPermissionUsedAccessPhotos();
        this.permissionUsedReadContacts = permissionUsedCount.getPermissionUsedReadContacts();
        this.permissionUsedEditAndDeleteContacts = permissionUsedCount.getPermissionUsedEditAndDeleteContacts();
        this.permissionUsedSendSMS = permissionUsedCount.getPermissionUsedSendSMS();
        this.permissionUsedReceiveSMS = permissionUsedCount.getPermissionUsedReceiveSMS();
        this.permissionUsedReadSMS = permissionUsedCount.getPermissionUsedReadSMS();
        this.permissionUsedNearByDevices = permissionUsedCount.getPermissionUsedNearByDevices();
        this.permissionUsedActivityRecognition = permissionUsedCount.getPermissionUsedActivityRecognition();
        this.permissionUsedCalendar = permissionUsedCount.getPermissionUsedCalendar();
        this.permissionUsedCallLogs = permissionUsedCount.getPermissionUsedCallLogs();
        this.permissionUsedPhone = permissionUsedCount.getPermissionUsedPhone();
        // sensor duration
        this.cameraDuration = sensorDuration.getCameraDuration();
        this.microphoneDuration = sensorDuration.getMicrophoneDuration();
        this.locationDuration = sensorDuration.getLocationDuration();
    }
} and 
public class PermissionUsedCount {
    private int permissionUsedCamera;
    private int permissionUsedMicrophone;
    private int permissionUsedGetLocation;
    private int permissionUsedCoarseLocation;
    private int permissionUsedStorage;
    private int permissionUsedReadMediaAural;
    private int permissionUsedReadMediaVisual;
    private int permissionUsedAccessVideos;
    private int permissionUsedAccessPhotos;
    private int permissionUsedReadContacts;
    private int permissionUsedEditAndDeleteContacts;
    private int permissionUsedSendSMS;
    private int permissionUsedReceiveSMS;
    private int permissionUsedReadSMS;
    private int permissionUsedNearByDevices;
    private int permissionUsedActivityRecognition;
    private int permissionUsedCalendar;
    private int permissionUsedCallLogs;
    private int permissionUsedPhone;

    public int getPermissionUsedCamera() {
        return permissionUsedCamera;
    }

    public void setPermissionUsedCamera(int permissionUsedCamera) {
        this.permissionUsedCamera = permissionUsedCamera;
    }

    public int getPermissionUsedMicrophone() {
        return permissionUsedMicrophone;
    }

    public void setPermissionUsedMicrophone(int permissionUsedMicrophone) {
        this.permissionUsedMicrophone = permissionUsedMicrophone;
    }

    public int getPermissionUsedGetLocation() {
        return permissionUsedGetLocation;
    }

    public void setPermissionUsedGetLocation(int permissionUsedGetLocation) {
        this.permissionUsedGetLocation = permissionUsedGetLocation;
    }

    public int getPermissionUsedCoarseLocation() {
        return permissionUsedCoarseLocation;
    }

    public void setPermissionUsedCoarseLocation(int permissionUsedCoarseLocation) {
        this.permissionUsedCoarseLocation = permissionUsedCoarseLocation;
    }

    public int getPermissionUsedStorage() {
        return permissionUsedStorage;
    }

    public void setPermissionUsedStorage(int permissionUsedStorage) {
        this.permissionUsedStorage = permissionUsedStorage;
    }

    public int getPermissionUsedReadMediaAural() {
        return permissionUsedReadMediaAural;
    }

    public void setPermissionUsedReadMediaAural(int permissionUsedReadMediaAural) {
        this.permissionUsedReadMediaAural = permissionUsedReadMediaAural;
    }

    public int getPermissionUsedReadMediaVisual() {
        return permissionUsedReadMediaVisual;
    }

    public void setPermissionUsedReadMediaVisual(int permissionUsedReadMediaVisual) {
        this.permissionUsedReadMediaVisual = permissionUsedReadMediaVisual;
    }

    public int getPermissionUsedAccessVideos() {
        return permissionUsedAccessVideos;
    }

    public void setPermissionUsedAccessVideos(int permissionUsedAccessVideos) {
        this.permissionUsedAccessVideos = permissionUsedAccessVideos;
    }

    public int getPermissionUsedAccessPhotos() {
        return permissionUsedAccessPhotos;
    }

    public void setPermissionUsedAccessPhotos(int permissionUsedAccessPhotos) {
        this.permissionUsedAccessPhotos = permissionUsedAccessPhotos;
    }

    public int getPermissionUsedReadContacts() {
        return permissionUsedReadContacts;
    }

    public void setPermissionUsedReadContacts(int permissionUsedReadContacts) {
        this.permissionUsedReadContacts = permissionUsedReadContacts;
    }

    public int getPermissionUsedEditAndDeleteContacts() {
        return permissionUsedEditAndDeleteContacts;
    }

    public void setPermissionUsedEditAndDeleteContacts(int permissionUsedEditAndDeleteContacts) {
        this.permissionUsedEditAndDeleteContacts = permissionUsedEditAndDeleteContacts;
    }

    public int getPermissionUsedSendSMS() {
        return permissionUsedSendSMS;
    }

    public void setPermissionUsedSendSMS(int permissionUsedSendSMS) {
        this.permissionUsedSendSMS = permissionUsedSendSMS;
    }

    public int getPermissionUsedReceiveSMS() {
        return permissionUsedReceiveSMS;
    }

    public void setPermissionUsedReceiveSMS(int permissionUsedReceiveSMS) {
        this.permissionUsedReceiveSMS = permissionUsedReceiveSMS;
    }

    public int getPermissionUsedReadSMS() {
        return permissionUsedReadSMS;
    }

    public void setPermissionUsedReadSMS(int permissionUsedReadSMS) {
        this.permissionUsedReadSMS = permissionUsedReadSMS;
    }

    public int getPermissionUsedNearByDevices() {
        return permissionUsedNearByDevices;
    }

    public void setPermissionUsedNearByDevices(int permissionUsedNearByDevices) {
        this.permissionUsedNearByDevices = permissionUsedNearByDevices;
    }

    public int getPermissionUsedActivityRecognition() {
        return permissionUsedActivityRecognition;
    }

    public void setPermissionUsedActivityRecognition(int permissionUsedActivityRecognition) {
        this.permissionUsedActivityRecognition = permissionUsedActivityRecognition;
    }

    public int getPermissionUsedCalendar() {
        return permissionUsedCalendar;
    }

    public void setPermissionUsedCalendar(int permissionUsedCalendar) {
        this.permissionUsedCalendar = permissionUsedCalendar;
    }

    public int getPermissionUsedCallLogs() {
        return permissionUsedCallLogs;
    }

    public void setPermissionUsedCallLogs(int permissionUsedCallLogs) {
        this.permissionUsedCallLogs = permissionUsedCallLogs;
    }

    public int getPermissionUsedPhone() {
        return permissionUsedPhone;
    }

    public void setPermissionUsedPhone(int permissionUsedPhone) {
        this.permissionUsedPhone = permissionUsedPhone;
    }
}

public class SensorDuration {
    private long cameraDuration;
    private long microphoneDuration;
    private long locationDuration;

    public SensorDuration() {

    }

    public SensorDuration(long cameraDuration, long microphoneDuration, long locationDuration) {
        this.cameraDuration = cameraDuration;
        this.microphoneDuration = microphoneDuration;
        this.locationDuration = locationDuration;
    }

    public long getCameraDuration() {
        return cameraDuration;
    }

    public void setCameraDuration(long cameraDuration) {
        this.cameraDuration = cameraDuration;
    }

    public long getMicrophoneDuration() {
        return microphoneDuration;
    }

    public void setMicrophoneDuration(long microphoneDuration) {
        this.microphoneDuration = microphoneDuration;
    }

    public long getLocationDuration() {
        return locationDuration;
    }

    public void setLocationDuration(long locationDuration) {
        this.locationDuration = locationDuration;
    }
}
