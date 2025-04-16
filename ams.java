package com.example.app; // Replace with your actual package name

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

/**
 * Test class for AppInfo
 * Contains 3 positive test cases and 15 negative test cases
 */
public class AppInfoTest {

    // Test constants
    private static final int VALID_UID = 10001;
    private static final int VALID_APP_CATEGORY_ID = 5;
    private static final int VALID_APP_TRUST_LEVEL = 2;
    
    private AppInfo appInfo;
    
    @BeforeEach
    public void setUp() {
        // Initialize with valid values before each test
        appInfo = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, VALID_APP_TRUST_LEVEL);
    }

    /* ========== POSITIVE TEST CASES ========== */

    /**
     * POSITIVE TEST CASE 1: Verify constructor initializes object with correct values
     */
    @Test
    public void testConstructorInitializesWithCorrectValues() {
        // Verify all fields have expected values
        assertEquals(VALID_UID, appInfo.getUid(), "UID should match constructor value");
        assertEquals(VALID_APP_CATEGORY_ID, appInfo.getAppCategoryId(), "App category ID should match constructor value");
        assertEquals(VALID_APP_TRUST_LEVEL, appInfo.getAppTrustLevel(), "App trust level should match constructor value");
    }

    /**
     * POSITIVE TEST CASE 2: Verify setter methods update values correctly
     */
    @Test
    public void testSettersUpdateValuesCorrectly() {
        // New test values
        int newUid = 20002;
        int newCategoryId = 8;
        int newTrustLevel = 3;
        
        // Set new values
        appInfo.setUid(newUid);
        appInfo.setAppCategoryId(newCategoryId);
        appInfo.setAppTrustLevel(newTrustLevel);
        
        // Verify values were updated
        assertEquals(newUid, appInfo.getUid(), "UID should be updated");
        assertEquals(newCategoryId, appInfo.getAppCategoryId(), "App category ID should be updated");
        assertEquals(newTrustLevel, appInfo.getAppTrustLevel(), "App trust level should be updated");
    }

    /**
     * POSITIVE TEST CASE 3: Test with multiple instances having different values
     */
    @Test
    public void testMultipleInstancesWithDifferentValues() {
        // Create a second instance with different values
        AppInfo appInfo2 = new AppInfo(30003, 10, 4);
        
        // Verify first instance still has original values
        assertEquals(VALID_UID, appInfo.getUid(), "First instance UID should remain unchanged");
        assertEquals(VALID_APP_CATEGORY_ID, appInfo.getAppCategoryId(), "First instance app category ID should remain unchanged");
        assertEquals(VALID_APP_TRUST_LEVEL, appInfo.getAppTrustLevel(), "First instance app trust level should remain unchanged");
        
        // Verify second instance has its own values
        assertEquals(30003, appInfo2.getUid(), "Second instance should have its own UID");
        assertEquals(10, appInfo2.getAppCategoryId(), "Second instance should have its own app category ID");
        assertEquals(4, appInfo2.getAppTrustLevel(), "Second instance should have its own app trust level");
        
        // Change value in first instance and verify it doesn't affect second instance
        appInfo.setUid(40004);
        assertEquals(40004, appInfo.getUid(), "First instance UID should be updated");
        assertEquals(30003, appInfo2.getUid(), "Second instance UID should remain unchanged");
    }

    /* ========== NEGATIVE TEST CASES ========== */

    /**
     * NEGATIVE TEST CASE 1: Test with negative UID value
     */
    @Test
    public void testWithNegativeUid() {
        int negativeUid = -1000;
        appInfo.setUid(negativeUid);
        
        // Verify negative value was accepted
        assertEquals(negativeUid, appInfo.getUid(), "Negative UID should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 2: Test with negative category ID
     */
    @Test
    public void testWithNegativeCategoryId() {
        int negativeCategoryId = -5;
        appInfo.setAppCategoryId(negativeCategoryId);
        
        // Verify negative value was accepted
        assertEquals(negativeCategoryId, appInfo.getAppCategoryId(), "Negative category ID should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 3: Test with negative trust level
     */
    @Test
    public void testWithNegativeTrustLevel() {
        int negativeTrustLevel = -2;
        appInfo.setAppTrustLevel(negativeTrustLevel);
        
        // Verify negative value was accepted
        assertEquals(negativeTrustLevel, appInfo.getAppTrustLevel(), "Negative trust level should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 4: Test with extreme integer values for UID
     */
    @Test
    public void testWithExtremeUidValues() {
        // Test with maximum integer value
        appInfo.setUid(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, appInfo.getUid(), "Maximum integer UID should be accepted");
        
        // Test with minimum integer value
        appInfo.setUid(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, appInfo.getUid(), "Minimum integer UID should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 5: Test with extreme integer values for category ID
     */
    @Test
    public void testWithExtremeCategoryIdValues() {
        // Test with maximum integer value
        appInfo.setAppCategoryId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, appInfo.getAppCategoryId(), "Maximum integer category ID should be accepted");
        
        // Test with minimum integer value
        appInfo.setAppCategoryId(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, appInfo.getAppCategoryId(), "Minimum integer category ID should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 6: Test with extreme integer values for trust level
     */
    @Test
    public void testWithExtremeTrustLevelValues() {
        // Test with maximum integer value
        appInfo.setAppTrustLevel(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, appInfo.getAppTrustLevel(), "Maximum integer trust level should be accepted");
        
        // Test with minimum integer value
        appInfo.setAppTrustLevel(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, appInfo.getAppTrustLevel(), "Minimum integer trust level should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 7: Test comparing different instances with same values
     */
    @Test
    public void testCompareEqualInstances() {
        // Create two instances with the same values
        AppInfo appInfo1 = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, VALID_APP_TRUST_LEVEL);
        AppInfo appInfo2 = new AppInfo(VALID_UID, VALID_APP_CATEGORY_ID, VALID_APP_TRUST_LEVEL);
        
        // Verify objects are different even with the same values (equals() not overridden)
        assertNotEquals(appInfo1, appInfo2, "Different instances should not be equal");
    }

    /**
     * NEGATIVE TEST CASE 8: Test field access with reflection
     */
    @Test
    public void testFieldAccessWithReflection() throws Exception {
        // Access private fields with reflection
        Field uidField = AppInfo.class.getDeclaredField("uid");
        Field categoryField = AppInfo.class.getDeclaredField("app_category_id");
        Field trustField = AppInfo.class.getDeclaredField("app_trust_level");
        
        // Make fields accessible
        uidField.setAccessible(true);
        categoryField.setAccessible(true);
        trustField.setAccessible(true);
        
        // Verify field values match getter values
        assertEquals(appInfo.getUid(), uidField.getInt(appInfo), "UID field value should match getter");
        assertEquals(appInfo.getAppCategoryId(), categoryField.getInt(appInfo), "Category ID field value should match getter");
        assertEquals(appInfo.getAppTrustLevel(), trustField.getInt(appInfo), "Trust level field value should match getter");
    }

    /**
     * NEGATIVE TEST CASE 9: Test field modification with reflection
     */
    @Test
    public void testFieldModificationWithReflection() throws Exception {
        // Access private fields with reflection
        Field uidField = AppInfo.class.getDeclaredField("uid");
        uidField.setAccessible(true);
        
        // Modify field directly
        int newUid = 50005;
        uidField.setInt(appInfo, newUid);
        
        // Verify getter reflects the change
        assertEquals(newUid, appInfo.getUid(), "UID getter should reflect direct field modification");
    }

    /**
     * NEGATIVE TEST CASE 10: Test string representation
     */
    @Test
    public void testStringRepresentation() {
        // Get string representation
        String stringRepresentation = appInfo.toString();
        
        // Verify it contains class name (default toString behavior)
        assertTrue(stringRepresentation.contains("AppInfo@"), "String representation should contain class name");
    }

    /**
     * NEGATIVE TEST CASE 11: Test object casting
     */
    @Test
    public void testObjectCasting() {
        // Cast to Object
        Object obj = appInfo;
        
        // Cast back to AppInfo
        AppInfo castedAppInfo = (AppInfo) obj;
        
        // Verify fields preserved through casting
        assertEquals(VALID_UID, castedAppInfo.getUid(), "UID should be preserved through casting");
        assertEquals(VALID_APP_CATEGORY_ID, castedAppInfo.getAppCategoryId(), "Category ID should be preserved through casting");
        assertEquals(VALID_APP_TRUST_LEVEL, castedAppInfo.getAppTrustLevel(), "Trust level should be preserved through casting");
    }

    /**
     * NEGATIVE TEST CASE 12: Test invalid casting (should throw ClassCastException)
     */
    @Test
    public void testInvalidCasting() {
        // Cast to Object
        Object obj = appInfo;
        
        // Attempt to cast to invalid type
        assertThrows(ClassCastException.class, () -> {
            String invalidCast = (String) obj;
        }, "Invalid casting should throw ClassCastException");
    }

    /**
     * NEGATIVE TEST CASE 13: Test binary operations on field values
     */
    @Test
    public void testBinaryOperations() {
        // Set specific values for binary operations
        appInfo.setUid(10);        // 1010 in binary
        appInfo.setAppCategoryId(6); // 0110 in binary
        
        // Perform binary operations
        int bitwiseAnd = appInfo.getUid() & appInfo.getAppCategoryId(); // 1010 & 0110 = 0010 = 2
        int bitwiseOr = appInfo.getUid() | appInfo.getAppCategoryId();  // 1010 | 0110 = 1110 = 14
        int bitwiseXor = appInfo.getUid() ^ appInfo.getAppCategoryId(); // 1010 ^ 0110 = 1100 = 12
        
        // Verify results
        assertEquals(2, bitwiseAnd, "Bitwise AND should yield correct result");
        assertEquals(14, bitwiseOr, "Bitwise OR should yield correct result");
        assertEquals(12, bitwiseXor, "Bitwise XOR should yield correct result");
    }

    /**
     * NEGATIVE TEST CASE 14: Test integer overflow
     */
    @Test
    public void testIntegerOverflow() {
        // Set value to integer maximum
        appInfo.setUid(Integer.MAX_VALUE);
        int initialValue = appInfo.getUid();
        
        // Increment to cause overflow
        appInfo.setUid(initialValue + 1);
        
        // Verify overflow occurred
        assertEquals(Integer.MIN_VALUE, appInfo.getUid(), "Adding 1 to MAX_VALUE should cause overflow to MIN_VALUE");
    }

    /**
     * NEGATIVE TEST CASE 15: Test with null reference
     */
    @Test
    public void testWithNullReference() {
        // Set reference to null
        AppInfo nullAppInfo = null;
        
        // Verify accessing methods on null reference throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            int uid = nullAppInfo.getUid();
        }, "Accessing method on null reference should throw NullPointerException");
    }
}