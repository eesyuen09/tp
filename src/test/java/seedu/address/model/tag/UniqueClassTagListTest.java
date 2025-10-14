package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.exceptions.ClassTagNotFoundException;
import seedu.address.model.tag.exceptions.DuplicateClassTagException;

public class UniqueClassTagListTest {

    private final UniqueClassTagList uniqueClassTagList = new UniqueClassTagList();
    private final ClassTag tag1 = new ClassTag("Sec3_Maths");
    private final ClassTag tag2 = new ClassTag("JC1_Physics");

    @Test
    public void contains_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassTagList.contains(null));
    }

    @Test
    public void contains_tagNotInList_returnsFalse() {
        assertFalse(uniqueClassTagList.contains(tag1));
    }

    @Test
    public void contains_tagInList_returnsTrue() {
        uniqueClassTagList.add(tag1);
        assertTrue(uniqueClassTagList.contains(tag1));
    }

    @Test
    public void contains_tagWithSameNameCaseInsensitive_returnsTrue() {
        uniqueClassTagList.add(tag1);
        ClassTag sameNameTag = new ClassTag("sec3_maths");
        assertTrue(uniqueClassTagList.contains(sameNameTag));
    }

    @Test
    public void add_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassTagList.add(null));
    }

    @Test
    public void add_duplicateTag_throwsDuplicateClassTagException() {
        uniqueClassTagList.add(tag1);
        assertThrows(DuplicateClassTagException.class, () -> uniqueClassTagList.add(tag1));
    }

    @Test
    public void add_duplicateTagNameCaseInsensitive_throwsDuplicateClassTagException() {
        uniqueClassTagList.add(tag1);
        ClassTag sameNameTag = new ClassTag("sec3_maths");
        assertThrows(DuplicateClassTagException.class, () -> uniqueClassTagList.add(sameNameTag));
    }

    @Test
    public void remove_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassTagList.remove(null));
    }

    @Test
    public void remove_tagDoesNotExist_throwsClassTagNotFoundException() {
        assertThrows(ClassTagNotFoundException.class, () -> uniqueClassTagList.remove(tag1));
    }

    @Test
    public void remove_existingTag_removesTag() {
        uniqueClassTagList.add(tag1);
        uniqueClassTagList.remove(tag1);
        UniqueClassTagList expectedList = new UniqueClassTagList();
        assertEquals(expectedList, uniqueClassTagList);
    }

    @Test
    public void setClassTags_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassTagList.setClassTags(null));
    }

    @Test
    public void setClassTags_list_replacesOwnListWithProvidedList() {
        uniqueClassTagList.add(tag1);
        List<ClassTag> classTagList = Collections.singletonList(tag2);
        uniqueClassTagList.setClassTags(classTagList);
        UniqueClassTagList expectedUniqueClassTagList = new UniqueClassTagList();
        expectedUniqueClassTagList.add(tag2);
        assertEquals(expectedUniqueClassTagList, uniqueClassTagList);
    }

    @Test
    public void setClassTags_listWithDuplicates_throwsDuplicateClassTagException() {
        List<ClassTag> listWithDuplicates = Arrays.asList(tag1, tag1);
        assertThrows(DuplicateClassTagException.class, () -> uniqueClassTagList.setClassTags(listWithDuplicates));
    }

    @Test
    public void setClassTags_validList_replacesList() {
        uniqueClassTagList.add(tag1);
        List<ClassTag> newList = Collections.singletonList(tag2);
        uniqueClassTagList.setClassTags(newList);
        UniqueClassTagList expectedList = new UniqueClassTagList();
        expectedList.add(tag2);
        assertEquals(expectedList, uniqueClassTagList);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueClassTagList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueClassTagList.asUnmodifiableObservableList().toString(), uniqueClassTagList.toString());
    }

}
