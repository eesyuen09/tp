package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.ClassTag;

/**
 * Jackson-friendly version of {@link ClassTag}.
 */
public class JsonAdaptedClassTag {

    private final String tagName;

    /**
     * Constructs a {@code JsonAdaptedClassTag} with the given {@code tagName}.
     */
    @JsonCreator
    public JsonAdaptedClassTag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Converts a given {@code ClassTag} into this class for Jackson use.
     */
    public JsonAdaptedClassTag(ClassTag source) {
        tagName = source.tagName;
    }

    @JsonValue
    public String getTagName() {
        return tagName;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code ClassTag} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public ClassTag toModelType() throws IllegalValueException {

        if (tagName == null) {
            throw new IllegalValueException(ClassTag.MESSAGE_CONSTRAINTS);
        }

        if (!ClassTag.isValidTagName(tagName)) {
            throw new IllegalValueException(ClassTag.MESSAGE_CONSTRAINTS);
        }
        return new ClassTag(tagName);
    }

}
