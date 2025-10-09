package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FeeTagTest {

    @Test
    public void constructor_and_isPaid_success() {
        FeeTag paidTag = new FeeTag(true);
        FeeTag unpaidTag = new FeeTag(false);

        assertTrue(paidTag.isPaid());
        assertFalse(unpaidTag.isPaid());
    }

    @Test
    public void equals_and_hashCode() {
        FeeTag tag1 = new FeeTag(true);
        FeeTag tag2 = new FeeTag(true);
        FeeTag tag3 = new FeeTag(false);

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toString_returnsExpectedFormat() {
        assertEquals("[Paid]", new FeeTag(true).toString());
        assertEquals("[Unpaid]", new FeeTag(false).toString());
    }
}