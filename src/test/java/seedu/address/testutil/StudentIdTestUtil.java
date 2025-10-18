package seedu.address.testutil;

import java.lang.reflect.Field;

import seedu.address.model.person.StudentId;

/**
 * A utility class for testing StudentId functionality.
 * Provides methods to manipulate StudentId state for testing purposes.
 *
 * <p><b>Why this is needed:</b></p>
 * StudentId uses a static counter (nextId) to auto-generate unique IDs. When tests create
 * PersonBuilder objects with hardcoded student IDs (like "0000"), the StudentId constructor
 * updates this static counter. Across hundreds of tests, this counter accumulates and can
 * eventually exceed the maximum value (9999), causing ExceedMaxStudentsException.
 *
 * <p><b>Usage:</b></p>
 * <ul>
 *   <li>Most test classes that use TypicalPersons don't need to do anything - the reset
 *       is handled automatically in TypicalPersons' static initializer.</li>
 *   <li>For test classes that DON'T use TypicalPersons but create many Person objects,
 *       call {@code resetStudentIdCounter()} in a {@code @BeforeEach} method.</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * public class MyTest {
 *     {@literal @}BeforeEach
 *     public void setUp() {
 *         StudentIdTestUtil.resetStudentIdCounter();
 *         // ... other setup
 *     }
 * }
 * </pre>
 */
public class StudentIdTestUtil {

    /**
     * Resets the StudentId counter to 0.
     * This should be called in test setup methods (e.g., @BeforeEach) to ensure test isolation.
     *
     * @throws RuntimeException if reflection fails (should not so happen in normal test execution)
     */
    public static void resetStudentIdCounter() {
        try {
            Field nextIdField = StudentId.class.getDeclaredField("nextId");
            nextIdField.setAccessible(true);
            nextIdField.setInt(null, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to reset StudentId counter", e);
        }
    }
}
