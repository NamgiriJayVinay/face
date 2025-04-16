package com.example.app; // Replace with your actual package name

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

/**
 * Test class for PackageDetails
 * Contains 3 positive test cases and 15 negative test cases
 */
public class PackageDetailsTest {

    // Test constants
    private static final int VALID_UID = 10001;
    private static final int VALID_APP_CATEGORY_ID = 5;
    private static final int VALID_APP_TRUST_LEVEL = 2;
    private static final String VALID_GRANTED_PERMISSIONS = "android.permission.CAMERA,android.permission.MICROPHONE";
    private static final String VALID_REQUESTED_PERMISSIONS = "android.permission.CAMERA,android.permission.MICROPHONE,android.permission.LOCATION";
    
    private PackageDetails packageDetails;
    private AppInfo validAppInfo;
    
    @BeforeEach
    public void setUp() {
        // Initialize with valid values before each test
        packageDetails = new PackageDetails();
        validAppInfo = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, VALID_APP_TRUST_LEVEL);
    }

    /* ========== POSITIVE TEST CASES ========== */

    /**
     * POSITIVE TEST CASE 1: Test setting and getting all fields with valid values
     */
    @Test
    public void testSettingAndGettingAllFields() {
        // Set all fields with valid values
        packageDetails.setAppInfo(validAppInfo);
        packageDetails.setGrantedPermissions(VALID_GRANTED_PERMISSIONS);
        packageDetails.setRequestedPermissions(VALID_REQUESTED_PERMISSIONS);
        
        // Verify all fields have expected values
        assertEquals(validAppInfo, packageDetails.getAppInfo(), "AppInfo should match set value");
        assertEquals(VALID_GRANTED_PERMISSIONS, packageDetails.getGrantedPermissions(), "Granted permissions should match set value");
        assertEquals(VALID_REQUESTED_PERMISSIONS, packageDetails.getRequestedPermissions(), "Requested permissions should match set value");
    }

    /**
     * POSITIVE TEST CASE 2: Test with default initialization (null values)
     */
    @Test
    public void testDefaultInitialization() {
        // Newly created PackageDetails should have null values
        assertNull(packageDetails.getAppInfo(), "AppInfo should be null by default");
        assertNull(packageDetails.getGrantedPermissions(), "Granted permissions should be null by default");
        assertNull(packageDetails.getRequestedPermissions(), "Requested permissions should be null by default");
    }

    /**
     * POSITIVE TEST CASE 3: Test with multiple instances having different values
     */
    @Test
    public void testMultipleInstancesWithDifferentValues() {
        // Set values for first instance
        packageDetails.setAppInfo(validAppInfo);
        packageDetails.setGrantedPermissions(VALID_GRANTED_PERMISSIONS);
        
        // Create second instance with different values
        PackageDetails packageDetails2 = new PackageDetails();
        AppInfo differentAppInfo = new AppInfo(20002, 10, 3);
        String differentPermissions = "android.permission.LOCATION";
        packageDetails2.setAppInfo(differentAppInfo);
        packageDetails2.setGrantedPermissions(differentPermissions);
        
        // Verify first instance has original values
        assertEquals(validAppInfo, packageDetails.getAppInfo(), "First instance AppInfo should remain unchanged");
        assertEquals(VALID_GRANTED_PERMISSIONS, packageDetails.getGrantedPermissions(), "First instance granted permissions should remain unchanged");
        
        // Verify second instance has different values
        assertEquals(differentAppInfo, packageDetails2.getAppInfo(), "Second instance should have different AppInfo");
        assertEquals(differentPermissions, packageDetails2.getGrantedPermissions(), "Second instance should have different granted permissions");
    }

    /* ========== NEGATIVE TEST CASES ========== */

    /**
     * NEGATIVE TEST CASE 1: Test with null AppInfo
     */
    @Test
    public void testWithNullAppInfo() {
        // Set AppInfo to null
        packageDetails.setAppInfo(null);
        
        // Verify null was accepted
        assertNull(packageDetails.getAppInfo(), "Null AppInfo should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 2: Test with null granted permissions
     */
    @Test
    public void testWithNullGrantedPermissions() {
        // Set granted permissions to null
        packageDetails.setGrantedPermissions(null);
        
        // Verify null was accepted
        assertNull(packageDetails.getGrantedPermissions(), "Null granted permissions should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 3: Test with null requested permissions
     */
    @Test
    public void testWithNullRequestedPermissions() {
        // Set requested permissions to null
        packageDetails.setRequestedPermissions(null);
        
        // Verify null was accepted
        assertNull(packageDetails.getRequestedPermissions(), "Null requested permissions should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 4: Test with empty string for granted permissions
     */
    @Test
    public void testWithEmptyGrantedPermissions() {
        // Set granted permissions to empty string
        packageDetails.setGrantedPermissions("");
        
        // Verify empty string was accepted
        assertEquals("", packageDetails.getGrantedPermissions(), "Empty granted permissions should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 5: Test with empty string for requested permissions
     */
    @Test
    public void testWithEmptyRequestedPermissions() {
        // Set requested permissions to empty string
        packageDetails.setRequestedPermissions("");
        
        // Verify empty string was accepted
        assertEquals("", packageDetails.getRequestedPermissions(), "Empty requested permissions should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 6: Test with special characters in permissions
     */
    @Test
    public void testWithSpecialCharactersInPermissions() {
        // Create permissions with special characters
        String specialPermissions = "android.permission.CAMERA!@#$%^&*(),android.permission.MICROPHONE_+{}|:\"<>?[]\\;'./";
        
        // Set permissions with special characters
        packageDetails.setGrantedPermissions(specialPermissions);
        
        // Verify special characters were accepted
        assertEquals(specialPermissions, packageDetails.getGrantedPermissions(), "Special characters in permissions should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 7: Test comparing different instances with same values
     */
    @Test
    public void testCompareEqualInstances() {
        // Create two instances with the same values
        PackageDetails packageDetails1 = new PackageDetails();
        PackageDetails packageDetails2 = new PackageDetails();
        
        // Set same values for both
        AppInfo appInfo = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, VALID_APP_TRUST_LEVEL);
        packageDetails1.setAppInfo(appInfo);
        packageDetails1.setGrantedPermissions(VALID_GRANTED_PERMISSIONS);
        
        packageDetails2.setAppInfo(appInfo); // Same AppInfo instance
        packageDetails2.setGrantedPermissions(VALID_GRANTED_PERMISSIONS);
        
        // Verify objects are different even with the same values (equals() not overridden)
        assertNotEquals(packageDetails1, packageDetails2, "Different instances should not be equal");
    }

    /**
     * NEGATIVE TEST CASE 8: Test field access with reflection
     */
    @Test
    public void testFieldAccessWithReflection() throws Exception {
        // Set values first
        packageDetails.setAppInfo(validAppInfo);
        packageDetails.setGrantedPermissions(VALID_GRANTED_PERMISSIONS);
        packageDetails.setRequestedPermissions(VALID_REQUESTED_PERMISSIONS);
        
        // Access private fields with reflection
        Field appInfoField = PackageDetails.class.getDeclaredField("appInfo");
        Field grantedPermissionsField = PackageDetails.class.getDeclaredField("grantedPermissions");
        Field requestedPermissionsField = PackageDetails.class.getDeclaredField("requestedPermissions");
        
        // Make fields accessible
        appInfoField.setAccessible(true);
        grantedPermissionsField.setAccessible(true);
        requestedPermissionsField.setAccessible(true);
        
        // Verify field values match getter values
        assertEquals(packageDetails.getAppInfo(), appInfoField.get(packageDetails), "appInfo field value should match getter");
        assertEquals(packageDetails.getGrantedPermissions(), grantedPermissionsField.get(packageDetails), "grantedPermissions field value should match getter");
        assertEquals(packageDetails.getRequestedPermissions(), requestedPermissionsField.get(packageDetails), "requestedPermissions field value should match getter");
    }

    /**
     * NEGATIVE TEST CASE 9: Test field modification with reflection
     */
    @Test
    public void testFieldModificationWithReflection() throws Exception {
        // Access private fields with reflection
        Field grantedPermissionsField = PackageDetails.class.getDeclaredField("grantedPermissions");
        grantedPermissionsField.setAccessible(true);
        
        // Modify field directly
        String newPermissions = "android.permission.ACCESS_FINE_LOCATION";
        grantedPermissionsField.set(packageDetails, newPermissions);
        
        // Verify getter reflects the change
        assertEquals(newPermissions, packageDetails.getGrantedPermissions(), "Getter should reflect direct field modification");
    }

    /**
     * NEGATIVE TEST CASE 10: Test string representation
     */
    @Test
    public void testStringRepresentation() {
        // Get string representation
        String stringRepresentation = packageDetails.toString();
        
        // Verify it contains class name (default toString behavior)
        assertTrue(stringRepresentation.contains("PackageDetails@"), "String representation should contain class name");
    }

    /**
     * NEGATIVE TEST CASE 11: Test with very long permission strings
     */
    @Test
    public void testWithVeryLongPermissionStrings() {
        // Create very long permission string
        StringBuilder longPermissions = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longPermissions.append("android.permission.TEST").append(i).append(",");
        }
        
        // Set very long permission string
        packageDetails.setGrantedPermissions(longPermissions.toString());
        
        // Verify very long string was accepted
        assertEquals(longPermissions.toString(), packageDetails.getGrantedPermissions(), "Very long permission string should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 12: Test object casting
     */
    @Test
    public void testObjectCasting() {
        // Set values
        packageDetails.setAppInfo(validAppInfo);
        packageDetails.setGrantedPermissions(VALID_GRANTED_PERMISSIONS);
        
        // Cast to Object
        Object obj = packageDetails;
        
        // Cast back to PackageDetails
        PackageDetails castedPackageDetails = (PackageDetails) obj;
        
        // Verify fields preserved through casting
        assertEquals(validAppInfo, castedPackageDetails.getAppInfo(), "AppInfo should be preserved through casting");
        assertEquals(VALID_GRANTED_PERMISSIONS, castedPackageDetails.getGrantedPermissions(), "Granted permissions should be preserved through casting");
    }

    /**
     * NEGATIVE TEST CASE 13: Test invalid casting (should throw ClassCastException)
     */
    @Test
    public void testInvalidCasting() {
        // Cast to Object
        Object obj = packageDetails;
        
        // Attempt to cast to invalid type
        assertThrows(ClassCastException.class, () -> {
            String invalidCast = (String) obj;
        }, "Invalid casting should throw ClassCastException");
    }

    /**
     * NEGATIVE TEST CASE 14: Test updating AppInfo fields after setting
     */
    @Test
    public void testUpdatingAppInfoAfterSetting() {
        // Set AppInfo
        packageDetails.setAppInfo(validAppInfo);
        
        // Update AppInfo fields
        validAppInfo.setUid(20002);
        validAppInfo.setAppCategoryId(10);
        
        // Verify the AppInfo reference in packageDetails reflects the changes
        assertEquals(20002, packageDetails.getAppInfo().getUid(), "AppInfo UID change should be reflected");
        assertEquals(10, packageDetails.getAppInfo().getAppCategoryId(), "AppInfo category ID change should be reflected");
    }

    /**
     * NEGATIVE TEST CASE 15: Test with null reference
     */
    @Test
    public void testWithNullReference() {
        // Set reference to null
        PackageDetails nullPackageDetails = null;
        
        // Verify accessing methods on null reference throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            AppInfo appInfo = nullPackageDetails.getAppInfo();
        }, "Accessing method on null reference should throw NullPointerException");
    }
}