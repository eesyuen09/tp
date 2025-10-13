package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.ClassTagNotFoundException;
import seedu.address.model.person.exceptions.DuplicateClassTagException;


/**
 * A list of class tags that enforces uniqueness between its elements and does not allow nulls.
 * A class tag is considered unique by comparing using {@code ClassTag#equals(Object)}.
 *
 * Supports a minimal set of list operations.
 */
public class UniqueClassTagList implements Iterable<ClassTag> {

    private final ObservableList<ClassTag> internalList = FXCollections.observableArrayList();
    private final ObservableList<ClassTag> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent class tag as the given argument.
     */
    public boolean contains(ClassTag toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a class tag to the list.
     * The tag must not already exist in the list.
     */
    public void add(ClassTag toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateClassTagException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent class tag from the list.
     * The tag must exist in the list.
     */
    public void remove(ClassTag toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ClassTagNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code classTags}.
     * {@code classTags} must not contain duplicate tags.
     */
    public void setClassTags(List<ClassTag> classTags) {
        requireAllNonNull(classTags);
        if (!classTagsAreUnique(classTags)) {
            throw new DuplicateClassTagException();
        }
        internalList.setAll(classTags);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ClassTag> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<ClassTag> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueClassTagList)) {
            return false;
        }

        UniqueClassTagList otherUniqueClassTagList = (UniqueClassTagList) other;
        return internalList.equals(otherUniqueClassTagList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code classTags} contains only unique tags.
     */
    private boolean classTagsAreUnique(List<ClassTag> classTags) {
        for (int i = 0; i < classTags.size() - 1; i++) {
            for (int j = i + 1; j < classTags.size(); j++) {
                if (classTags.get(i).equals(classTags.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
