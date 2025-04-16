
public class AppInfo {
    private int uid;
    private int app_category_id;
    private int app_trust_level;

    public AppInfo(int uid, int app_category_id, int app_trust_level) {
        this.uid = uid;
        this.app_category_id = app_category_id;
        this.app_trust_level = app_trust_level;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAppCategoryId() {
        return app_category_id;
    }

    public void setAppCategoryId(int app_category_id) {
        this.app_category_id = app_category_id;
    }

    public int getAppTrustLevel() {
        return app_trust_level;
    }

    public void setAppTrustLevel(int app_trust_level) {
        this.app_trust_level = app_trust_level;
    }
}


public class Event {
    private String permission;
    private String type;
    private String state;
    private double number;
    private long timestamp;
    private double score;
    private Float reconstructionError;
    private Float reconstructionThreshold;
    private long totalBackgroundUsage;
    private long totalForegroundUsage;

    public Event(String permission, String type, String state, double number, long timestamp, double score, Float reconstructionError, Float reconstructionThreshold) {
        this.permission = permission;
        this.type = type;
        this.state = state;
        this.number = number;
        this.timestamp = timestamp;
        this.score = score;
        this.reconstructionError = reconstructionError;
        this.reconstructionThreshold = reconstructionThreshold;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getType() {
        return this.type;
    }

    public String getState() {
        return this.state;
    }

    public double getNumber() {
        return this.number;
    }

    public double getScore() {
        return this.score;
    }

    public Float getReconstructionError() {
        return this.reconstructionError;
    }

    public Float getReconstructionThreshold() {
        return this.reconstructionThreshold;
    }

    public void setTotalForegroundUsage(long totalForegroundUsage) { this.totalForegroundUsage = totalForegroundUsage; }

    public void setTotalBackgroundUsage(long totalBackgroundUsage) { this.totalBackgroundUsage = totalBackgroundUsage; }

    public long getTotalBackgroundUsage() {
        return totalBackgroundUsage;
    }

    public long getTotalForegroundUsage() {
        return totalForegroundUsage;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setReconstructionError(Float reconstructionError) {
        this.reconstructionError = reconstructionError;
    }

    public void setReconstructionThreshold(Float reconstructionThreshold) {
        this.reconstructionThreshold = reconstructionThreshold;
    }
}


public class MinutePackage {
    private final long access_minute;
    private final String packageNames;

    public MinutePackage(long access_minute, String packageNames) {
        this.access_minute = access_minute;
        this.packageNames = packageNames;
    }

    public long getAccess_minute() {
        return access_minute;
    }

    public String getPackageNames() {
        return packageNames;
    }
}


public class PackageDetails {
    private AppInfo appInfo;
    private String grantedPermissions;
    private String requestedPermissions;

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public String getGrantedPermissions() {
        return grantedPermissions;
    }

    public void setGrantedPermissions(String grantedPermissions) {
        this.grantedPermissions = grantedPermissions;
    }

    public String getRequestedPermissions() {
        return requestedPermissions;
    }

    public void setRequestedPermissions(String requestedPermissions) {
        this.requestedPermissions = requestedPermissions;
    }
}



