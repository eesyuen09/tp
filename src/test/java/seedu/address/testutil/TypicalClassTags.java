package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.tag.ClassTag;

/**
 * A utility class containing a list of {@code ClassTag} objects to be used in tests.
 */
public class TypicalClassTags {

    public static final ClassTag SEC3_MATHS = new ClassTag("Sec3_Maths");
    public static final ClassTag JC1_PHYSICS = new ClassTag("JC1_Physics");
    public static final ClassTag FRIENDS = new ClassTag("friends");

    private TypicalClassTags() {} // prevents instantiation

    /**
     * Returns a list with all the typical class tags.
     */
    public static List<ClassTag> getTypicalClassTags() {
        return new ArrayList<>(Arrays.asList(SEC3_MATHS, JC1_PHYSICS, FRIENDS));
    }
}
