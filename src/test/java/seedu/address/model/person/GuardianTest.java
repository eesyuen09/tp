package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GuardianTest {

    private static final Name ALICE_NAME = new Name("Alice Tan");
    private static final Name BOB_NAME = new Name("Bob Lim");
    private static final Phone ALICE_PHONE = new Phone("91234567");
    private static final Phone BOB_PHONE = new Phone("98765432");

    private final Guardian aliceGuardian = new Guardian(ALICE_NAME, ALICE_PHONE);
    private final Guardian bobGuardian = new Guardian(BOB_NAME, BOB_PHONE);

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(aliceGuardian.isSamePerson(aliceGuardian));

        // null -> returns false
        assertFalse(aliceGuardian.isSamePerson(null));

        // same name, different phone -> returns false
        Guardian editedAlice = new Guardian(ALICE_NAME, BOB_PHONE);
        assertFalse(aliceGuardian.isSamePerson(editedAlice));

        // different name -> returns false
        assertFalse(aliceGuardian.isSamePerson(bobGuardian));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Guardian aliceCopy = new Guardian(ALICE_NAME, ALICE_PHONE);
        assertTrue(aliceGuardian.equals(aliceCopy));

        // same object -> returns true
        assertTrue(aliceGuardian.equals(aliceGuardian));

        // null -> returns false
        assertFalse(aliceGuardian.equals(null));

        // different type -> returns false
        assertFalse(aliceGuardian.equals(5));

        // different guardian -> returns false
        assertFalse(aliceGuardian.equals(bobGuardian));

        // different name -> returns false
        Guardian editedAlice = new Guardian(BOB_NAME, ALICE_PHONE);
        assertFalse(aliceGuardian.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new Guardian(ALICE_NAME, BOB_PHONE);
        assertFalse(aliceGuardian.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Guardian.class.getCanonicalName() + "{name=" + ALICE_NAME
                + ", phone=" + ALICE_PHONE + "}";
        assertEquals(expected, aliceGuardian.toString());
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        Guardian aliceCopy = new Guardian(ALICE_NAME, ALICE_PHONE);
        assertEquals(aliceGuardian.hashCode(), aliceCopy.hashCode());
    }
}
