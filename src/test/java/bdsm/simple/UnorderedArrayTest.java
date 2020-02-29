package bdsm.simple;

import beans.Person;
import org.junit.Test;

import java.util.HashSet;

import static bdsm.simple.UnorderedArray.DEFAULT_SIZE;
import static bdsm.simple.UnorderedArray.NOT_IN_ARRAY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnorderedArrayTest {
    /* Note 1:
     * The UnorderedArray#get(index) is effectively tested since it appears in all the add__ tests.
     */
    UnorderedArray<Integer> integers = new UnorderedArray<>();
    UnorderedArray<Person> people = new UnorderedArray<>();

    //<editor-fold desc="Constructor tests">
    @Test
    public void defaultConstructor() {
        assertEquals(DEFAULT_SIZE, integers.capacity());
    }

    @Test
    public void sizedConstructor() {
        int cap = 100;
        UnorderedArray<Integer> ints = new UnorderedArray<>(cap);
        assertEquals(cap, ints.capacity());
    }

    @Test
    public void copyConstructor() {
        integers.add(1, -2, 3);
        UnorderedArray<Integer> array = new UnorderedArray<>(integers);
        assertEquals(integers.size(), array.size());
        assertEquals(integers.size(), array.capacity());
        assertEquals(integers, array);
    }

    @Test
    public void copyConstructorBasic() {
        Integer[] array = {-1, 2, 0};
        UnorderedArray<Integer> copy = new UnorderedArray<>(array);
        assertEquals(array.length, copy.size());
        assertEquals(array.length, copy.capacity());
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], copy.get(i));
        }
    }

    @Test
    public void copyConstructorBasicFromIndex() {
        Integer[] array = {-1, 3, 0, -12};
        int numberOfItemsToCopy = 2;
        int start = 1;
        UnorderedArray<Integer> copy = new UnorderedArray<>(array, start, numberOfItemsToCopy);
        assertEquals(numberOfItemsToCopy, copy.size());
        assertEquals(numberOfItemsToCopy, copy.capacity());
        for (int i = 0; i < numberOfItemsToCopy; i++) {
            assertEquals(array[i + start], copy.get(i));
        }
    }
    //</editor-fold>

    //<editor-fold desc="Resize and emptiness tests">
    @Test
    public void resize() {
        int cap = 10;
        integers.resize(cap);
        assertEquals(cap, integers.capacity());
    }

    @Test
    public void expandBackingArrayFromEmptyArray() {
        integers.expandBackingArray();
        assertEquals(DEFAULT_SIZE / 2, integers.capacity());
    }

    @Test
    public void emptiness() {
        assertTrue(integers.isEmpty());
        assertFalse(integers.isNotEmpty());
        integers.add(10, 20);
        assertFalse(integers.isEmpty());
        assertTrue(integers.isNotEmpty());
    }
    //</editor-fold>

    //<editor-fold desc="Add elements tests">
    @Test
    public void insert() {
        integers.add(0, 1, 2, 3);
        int newValue = 10;
        int index = 1;
        integers.insert(index, newValue);
        assertEquals(newValue, ((int) integers.get(index)));
        assertEquals(5, integers.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void insertOutOfBounds() {
        integers.insert(1, 10);
    }

    @Test
    public void insertLast() {
        UnorderedArray<Integer> vs = new UnorderedArray<>(1);
        vs.add(0);
        vs.insert(1, 10);
        assertEquals(10, (int) vs.get(1));
        assertEquals(2, vs.size());
    }

    @Test
    public void addOne() {
        integers.add(10);
        assertEquals(1, integers.size());
        assertEquals(10, ((int) integers.get(0)));
    }

    @Test
    public void addTwo() {
        integers.add(1, 5);
        assertEquals(2, integers.size());
        assertEquals(1, ((int) integers.get(0)));
        assertEquals(5, ((int) integers.get(1)));
    }

    @Test
    public void addThree() {
        integers.add(1, 2, 2);
        assertEquals(3, integers.size());
        assertEquals(1, ((int) integers.get(0)));
        assertEquals(2, ((int) integers.get(1)));
        assertEquals(2, ((int) integers.get(2)));
    }

    @Test
    public void addFour() {
        integers.add(1, 1, 3, 3);
        assertEquals(4, integers.size());
        assertEquals(1, ((int) integers.get(0)));
        assertEquals(1, ((int) integers.get(1)));
        assertEquals(3, ((int) integers.get(2)));
        assertEquals(3, ((int) integers.get(3)));
    }

    @Test
    public void addAllBasic() {
        int numberOfItemsToAdd = 3;
        Integer[] array = {-1, -2, -3, -4};
        int start = 1;
        int oldSize = integers.size();
        integers.addAll(array, start, numberOfItemsToAdd);
        assertEquals(numberOfItemsToAdd + oldSize, integers.size());
        for (int i = 0; i < numberOfItemsToAdd; i++) {
            assertEquals(array[i + start], integers.get(i + oldSize));
        }
    }

    @Test
    public void addAllBasicAll() {
        Integer[] array = {-1, -2, -3, -4};
        int oldSize = integers.size();
        integers.addAll(array);
        assertEquals(array.length + oldSize, integers.size());
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], integers.get(i + oldSize));
        }
    }

    @Test
    public void addAllUnordered() {
        UnorderedArray<Integer> array = new UnorderedArray<>();
        array.add(1, -2, -3);
        int oldSize = integers.size();
        integers.addAll(array);
        assertEquals(array.size() + oldSize, integers.size());
        for (int i = 0; i < array.size(); i++) {
            assertEquals(array.get(i), integers.get(i + oldSize));
        }
    }

    @Test
    public void addAllUnorderedFromIndex() {
        UnorderedArray<Integer> array = new UnorderedArray<>();
        array.addAll(12, -3, 105, 506, -34);
        int numberOfItemsToCopy = 3;
        int oldSize = integers.size();
        int start = 1;
        integers.addAll(array, start, numberOfItemsToCopy);
        assertEquals(oldSize + numberOfItemsToCopy, integers.size());
        for (int i = 0; i < numberOfItemsToCopy; i++) {
            assertEquals(array.get(i + start), integers.get(i + oldSize));
        }
    }
    //</editor-fold>

    //<editor-fold desc="Retrieval tests">
    @Test(expected = IndexOutOfBoundsException.class)
    public void getIndexOutOfBounds() {
        integers.add(10);
        integers.get(1);
    }

    @Test
    public void first() {
        integers.add(10, 20, 30);
        assertEquals(10, ((int) integers.first()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void firstEmpty() {
        integers.first();
    }

    @Test
    public void last() {
        integers.add(10, 20, 30);
        assertEquals(30, ((int) integers.last()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void lastEmpty() {
        integers.last();
    }
    //</editor-fold>

    //<editor-fold desc="indexOf and contains tests">
    @Test
    public void indexOfNull() {
        assertEquals(NOT_IN_ARRAY, integers.indexOfIdentity(null));
        assertEquals(NOT_IN_ARRAY, integers.indexOf(null));
        integers.add(1, null, 10);
        assertEquals(1, integers.indexOfIdentity(null));
        assertEquals(1, integers.indexOf(null));
    }

    @Test
    public void indexOfProper() {
        Person george = new Person("George", 23);
        Person georgeNextYear = new Person("George", 24);
        assertEquals(NOT_IN_ARRAY, people.indexOf(george));
        people.add(george);
        assertEquals(0, people.indexOf(george));
        assertEquals(0, people.indexOf(georgeNextYear));
        assertEquals(0, people.indexOfIdentity(george));
        assertEquals(NOT_IN_ARRAY, people.indexOfIdentity(georgeNextYear));
    }

    @Test
    public void containsNull() {
        assertFalse(integers.contains(null));
        integers.add(1, null, 23, null);
        assertTrue(integers.contains(null));
    }

    @Test
    public void containsProper() {
        Person george = new Person("George", 23);
        Person georgeNextYear = new Person("George", 24);
        assertFalse(people.contains(george));
        people.add(george);
        assertTrue(people.containsIdentity(george));
        assertTrue(people.contains(georgeNextYear));
        assertFalse(people.containsIdentity(georgeNextYear));
    }
    //</editor-fold>

    //<editor-fold desc="Remove elements tests">
    @Test
    public void removeIndex() {
        integers.add(10, 20, 30);
        assertEquals(20, ((int) integers.removeIndex(1)));
        assertEquals(2, integers.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIndexOutOfBounds() {
        integers.removeIndex(1);
    }

    @Test
    public void removeValueNotInArray() {
        integers.add(10, 20, 30);
        assertFalse(integers.removeValue(40));
        assertEquals(3, integers.size());
    }

    @Test
    public void removeValue() {
        Person george = new Person("George", 23);
        Person futureGeorge = new Person("George", 24);
        people.add(george, futureGeorge);
        assertTrue(people.removeValue(futureGeorge));
        assertEquals(1, people.size());
        assertTrue(people.containsIdentity(futureGeorge));
    }

    @Test
    public void removeValueIdentity() {
        Person george = new Person("George", 23);
        Person futureGeorge = new Person("George", 24);
        people.add(george, futureGeorge);
        assertTrue(people.removeValueIdentity(futureGeorge));
        assertEquals(1, people.size());
        assertFalse((people.containsIdentity(futureGeorge)));
    }

    @Test
    public void removeValueIdentityNotInArray() {
        Person george = new Person("George", 23);
        Person futureGeorge = new Person("George", 24);
        people.add(george);
        assertFalse(people.removeValueIdentity(futureGeorge));
    }

    @Test
    public void clear() {
        integers.add(10, 20, 30);
        assertTrue(integers.isNotEmpty());
        integers.clear();
        assertTrue(integers.isEmpty());
    }
    //</editor-fold>

    @Test
    public void iteration() {
        integers.add(-10, -10, 20, 35);
        HashSet<Integer> vals = new HashSet<>();
        vals.add(-10);
        vals.add(20);
        vals.add(35);
        for (int v : integers) {
            assertTrue(vals.contains(v));
        }
    }
}