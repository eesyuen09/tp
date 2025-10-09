package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FeeTagTest {

    @Test
    public void constructorAndIsPaidSuccess() {
        FeeTag paidTag = new FeeTag(true);
        FeeTag unpaidTag = new FeeTag(false);

        assertTrue(paidTag.isPaid());
        assertFalse(unpaidTag.isPaid());
    }

    @Test
    public void equalsAndHashCode() {
        FeeTag tag1 = new FeeTag(true);
        FeeTag tag2 = new FeeTag(true);
        FeeTag tag3 = new FeeTag(false);

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toStringReturnsExpectedFormat() {
        assertEquals("[Paid]", new FeeTag(true).toString());
        assertEquals("[Unpaid]", new FeeTag(false).toString());
    }

}
