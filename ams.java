package com.example.app; // Replace with your actual package name

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Test class for MinutePackage
 * Contains 3 positive test cases and 15 negative test cases
 */
public class MinutePackageTest {

    // Test constants
    private static final long VALID_ACCESS_MINUTE = 1650000000000L;
    private static final String VALID_PACKAGE_NAMES = "com.example.app1,com.example.app2";

    /* ========== POSITIVE TEST CASES ========== */

    /**
     * POSITIVE TEST CASE 1: Verify constructor initializes object with correct values
     */
    @Test
    public void testConstructorInitializesWithCorrectValues() {
        // Create instance with valid data
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Verify fields have expected values
        assertEquals(VALID_ACCESS_MINUTE, minutePackage.getAccess_minute(), "Access minute should match constructor value");
        assertEquals(VALID_PACKAGE_NAMES, minutePackage.getPackageNames(), "Package names should match constructor value");
    }

    /**
     * POSITIVE TEST CASE 2: Test with multiple comma-separated package names
     */
    @Test
    public void testWithMultiplePackageNames() {
        // Create string with multiple package names
        String multiplePackages = "com.example.app1,com.example.app2,com.example.app3,com.example.app4";
        
        // Create instance with multiple packages
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, multiplePackages);
        
        // Verify package names field contains all packages
        assertEquals(multiplePackages, minutePackage.getPackageNames(), "Package names should contain all packages");
    }

    /**
     * POSITIVE TEST CASE 3: Test with multiple instances having different values
     */
    @Test
    public void testMultipleInstancesWithDifferentValues() {
        // Create first instance
        MinutePackage minutePackage1 = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Create second instance with different values
        long differentMinute = VALID_ACCESS_MINUTE + 60000; // +1 minute
        String differentPackages = "com.example.different";
        MinutePackage minutePackage2 = new MinutePackage(differentMinute, differentPackages);
        
        // Verify first instance has original values
        assertEquals(VALID_ACCESS_MINUTE, minutePackage1.getAccess_minute(), "First instance access minute should remain unchanged");
        assertEquals(VALID_PACKAGE_NAMES, minutePackage1.getPackageNames(), "First instance package names should remain unchanged");
        
        // Verify second instance has different values
        assertEquals(differentMinute, minutePackage2.getAccess_minute(), "Second instance should have different access minute");
        assertEquals(differentPackages, minutePackage2.getPackageNames(), "Second instance should have different package names");
    }

    /* ========== NEGATIVE TEST CASES ========== */

    /**
     * NEGATIVE TEST CASE 1: Test with negative access_minute value
     */
    @Test
    public void testWithNegativeAccessMinute() {
        // Create instance with negative access minute
        long negativeMinute = -1000L;
        MinutePackage minutePackage = new MinutePackage(negativeMinute, VALID_PACKAGE_NAMES);
        
        // Verify negative value was accepted
        assertEquals(negativeMinute, minutePackage.getAccess_minute(), "Negative access minute should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 2: Test with empty package names string
     */
    @Test
    public void testWithEmptyPackageNames() {
        // Create instance with empty package names
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, "");
        
        // Verify empty string was accepted
        assertEquals("", minutePackage.getPackageNames(), "Empty package names should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 3: Test with null package names string
     */
    @Test
    public void testWithNullPackageNames() {
        // Create instance with null package names
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, null);
        
        // Verify null was accepted
        assertNull(minutePackage.getPackageNames(), "Null package names should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 4: Test with extreme access_minute values
     */
    @Test
    public void testWithExtremeAccessMinuteValues() {
        // Test with maximum long value
        MinutePackage maxMinutePackage = new MinutePackage(Long.MAX_VALUE, VALID_PACKAGE_NAMES);
        assertEquals(Long.MAX_VALUE, maxMinutePackage.getAccess_minute(), "Maximum long value should be accepted for access minute");
        
        // Test with minimum long value
        MinutePackage minMinutePackage = new MinutePackage(Long.MIN_VALUE, VALID_PACKAGE_NAMES);
        assertEquals(Long.MIN_VALUE, minMinutePackage.getAccess_minute(), "Minimum long value should be accepted for access minute");
    }

    /**
     * NEGATIVE TEST CASE 5: Test with special characters in package names
     */
    @Test
    public void testWithSpecialCharactersInPackageNames() {
        // Create package names with special characters
        String specialPackageNames = "com.example.app!@#$%^&*(),com.example.app_+{}|:\"<>?[]\\;'./";
        
        // Create instance with special characters
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, specialPackageNames);
        
        // Verify special characters were accepted
        assertEquals(specialPackageNames, minutePackage.getPackageNames(), "Special characters in package names should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 6: Test comparing different instances with same values
     */
    @Test
    public void testCompareEqualInstances() {
        // Create two instances with the same values
        MinutePackage minutePackage1 = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        MinutePackage minutePackage2 = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Verify objects are different even with the same values (equals() not overridden)
        assertNotEquals(minutePackage1, minutePackage2, "Different instances should not be equal");
    }

    /**
     * NEGATIVE TEST CASE 7: Test field access with reflection
     */
    @Test
    public void testFieldAccessWithReflection() throws Exception {
        // Create instance
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Access private fields with reflection
        Field accessMinuteField = MinutePackage.class.getDeclaredField("access_minute");
        Field packageNamesField = MinutePackage.class.getDeclaredField("packageNames");
        
        // Make fields accessible
        accessMinuteField.setAccessible(true);
        packageNamesField.setAccessible(true);
        
        // Verify field values match getter values
        assertEquals(minutePackage.getAccess_minute(), accessMinuteField.getLong(minutePackage), "access_minute field value should match getter");
        assertEquals(minutePackage.getPackageNames(), packageNamesField.get(minutePackage), "packageNames field value should match getter");
    }

    /**
     * NEGATIVE TEST CASE 8: Test field immutability (final modifier)
     */
    @Test
    public void testFieldImmutability() throws Exception {
        // Access field modifiers
        Field accessMinuteField = MinutePackage.class.getDeclaredField("access_minute");
        Field packageNamesField = MinutePackage.class.getDeclaredField("packageNames");
        
        // Verify fields have final modifier
        assertTrue(Modifier.isFinal(accessMinuteField.getModifiers()), "access_minute field should be final");
        assertTrue(Modifier.isFinal(packageNamesField.getModifiers()), "packageNames field should be final");
    }

    /**
     * NEGATIVE TEST CASE 9: Test field modification with reflection (should fail for final fields)
     */
    @Test
    public void testFieldModificationWithReflection() {
        // Create instance
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Attempt to modify final fields with reflection (should throw exception)
        assertThrows(Exception.class, () -> {
            Field accessMinuteField = MinutePackage.class.getDeclaredField("access_minute");
            accessMinuteField.setAccessible(true);
            accessMinuteField.setLong(minutePackage, VALID_ACCESS_MINUTE + 1);
        }, "Modifying final field should throw exception");
    }

    /**
     * NEGATIVE TEST CASE 10: Test string representation
     */
    @Test
    public void testStringRepresentation() {
        // Create instance
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Get string representation
        String stringRepresentation = minutePackage.toString();
        
        // Verify it contains class name (default toString behavior)
        assertTrue(stringRepresentation.contains("MinutePackage@"), "String representation should contain class name");
    }

    /**
     * NEGATIVE TEST CASE 11: Test with very long package names string
     */
    @Test
    public void testWithVeryLongPackageNames() {
        // Create very long package names string
        StringBuilder longPackageNames = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longPackageNames.append("com.example.app").append(i).append(",");
        }
        
        // Create instance with very long package names
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, longPackageNames.toString());
        
        // Verify very long string was accepted
        assertEquals(longPackageNames.toString(), minutePackage.getPackageNames(), "Very long package names should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 12: Test object casting
     */
    @Test
    public void testObjectCasting() {
        // Create instance
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Cast to Object
        Object obj = minutePackage;
        
        // Cast back to MinutePackage
        MinutePackage castedMinutePackage = (MinutePackage) obj;
        
        // Verify fields preserved through casting
        assertEquals(VALID_ACCESS_MINUTE, castedMinutePackage.getAccess_minute(), "access_minute should be preserved through casting");
        assertEquals(VALID_PACKAGE_NAMES, castedMinutePackage.getPackageNames(), "packageNames should be preserved through casting");
    }

    /**
     * NEGATIVE TEST CASE 13: Test invalid casting (should throw ClassCastException)
     */
    @Test
    public void testInvalidCasting() {
        // Create instance
        MinutePackage minutePackage = new MinutePackage(VALID_ACCESS_MINUTE, VALID_PACKAGE_NAMES);
        
        // Cast to Object
        Object obj = minutePackage;
        
        // Attempt to cast to invalid type
        assertThrows(ClassCastException.class, () -> {
            String invalidCast = (String) obj;
        }, "Invalid casting should throw ClassCastException");
    }

    /**
     * NEGATIVE TEST CASE 14: Test with zero access_minute
     */
    @Test
    public void testWithZeroAccessMinute() {
        // Create instance with zero access minute
        MinutePackage minutePackage = new MinutePackage(0L, VALID_PACKAGE_NAMES);
        
        // Verify zero was accepted
        assertEquals(0L, minutePackage.getAccess_minute(), "Zero access minute should be accepted");
    }

    /**
     * NEGATIVE TEST CASE 15: Test with null reference
     */
    @Test
    public void testWithNullReference() {
        // Set reference to null
        MinutePackage nullMinutePackage = null;
        
        // Verify accessing methods on null reference throws NullPointerException
        assertThrows(NullPointerException.class, () -> {
            long accessMinute = nullMinutePackage.getAccess_minute();
        }, "Accessing method on null reference should throw NullPointerException");
    }
}