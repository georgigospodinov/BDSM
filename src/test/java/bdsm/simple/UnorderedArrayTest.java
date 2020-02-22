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
    UnorderedArray<Integer> integers = new UnorderedArray<>();
    UnorderedArray<Person> people = new UnorderedArray<>();

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