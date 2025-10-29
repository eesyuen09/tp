package seedu.address.model.fee;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.time.Month;

/**
 * Represents one row in a student's payment history view.
 */
public class FeeHistoryEntry {

    private final Month month;
    private final FeeState state;
    private final boolean explicit;

    /**
     * Constructs a {@code FeeHistoryEntry}.
     */
    public FeeHistoryEntry(Month month, FeeState state, boolean explicit) {
        this.month = requireNonNull(month);
        this.state = requireNonNull(state);
        this.explicit = explicit;
    }

    public Month getMonth() {
        return month;
    }

    public FeeState getState() {
        return state;
    }

    /**
     * Returns {@code true} if the status was explicitly set by the user.
     */
    public boolean isExplicit() {
        return explicit;
    }

    /**
     * Returns a short label describing how the state was derived.
     */
    public String getSourceLabel() {
        return explicit ? "marked" : "default";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FeeHistoryEntry)) {
            return false;
        }
        FeeHistoryEntry otherEntry = (FeeHistoryEntry) other;
        return month.equals(otherEntry.month)
                && state == otherEntry.state
                && explicit == otherEntry.explicit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, state, explicit);
    }
}
