
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
