package seedu.address.model.tag;

/**
 * Represents a FeeTag that indicates if a student has paid.
 * For now, each student will have either a Paid or Unpaid FeeTag.
 * This class is independent from Tag for minimal impact.
 */
public class FeeTag {

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
        return isPaid ? "Paid" : "Unpaid";
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

}
