package seedu.address.model.tag;

import java.util.Optional;

/**
 * Represents a FeeTag that indicates if a student has paid.
 * For now, each student will have either a Paid or Unpaid FeeTag.
 * This class is independent from Tag for minimal impact.
 */
public class FeeTag {

    public static final String TAG_PAID = "Paid";
    public static final String TAG_UNPAID = "Unpaid";
    private final boolean isPaid;

    /**
     * Creates a FeeTag with the given payment status.
     */
    public FeeTag(boolean isPaid) {
        this.isPaid = isPaid;
    }

    /**
     * Returns true if the student is marked as paid.
     */
    public boolean isPaid() {

        return isPaid;
    }

    /**
     * Returns the tag name for display (either "Paid" or "Unpaid").
     */
    public String getDisplayName() {
        return isPaid
                ? "Paid"
                : "Unpaid";
    }

    @Override
    public String toString() {
        return "[" + getDisplayName() + "]";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FeeTag && ((FeeTag) other).isPaid == this.isPaid;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(isPaid);
    }

    /** Returns true if the tag represents a fee status (Paid/Unpaid). */
    public static boolean isFeeTag(Tag tag) {
        String name = tag.tagName;
        return name.equalsIgnoreCase(TAG_PAID) || name.equalsIgnoreCase(TAG_UNPAID);
    }

    /** Converts a generic Tag into a FeeTag if possible. */
    public static Optional<FeeTag> tagToFeeTag(Tag tag) {
        if (tag.tagName.equalsIgnoreCase(TAG_PAID)) {
            return Optional.of(new FeeTag(true));
        } else if (tag.tagName.equalsIgnoreCase(TAG_UNPAID)) {
            return Optional.of(new FeeTag(false));
        }
        return Optional.empty();
    }

    /** Converts this FeeTag back into a generic Tag object. */
    public Tag feeTagToTag() {
        return new Tag(this.isPaid ? TAG_PAID : TAG_UNPAID);
    }

    /** Returns a FeeTag representing Paid. */
    public static FeeTag ofPaid() {
        return new FeeTag(true);
    }

    /** Returns a FeeTag representing Unpaid. */
    public static FeeTag ofUnpaid() {
        return new FeeTag(false);
    }


}
