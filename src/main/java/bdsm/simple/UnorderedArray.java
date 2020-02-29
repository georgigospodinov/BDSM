package bdsm.simple;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Iterator;

@SuppressWarnings("unchecked")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
// TODO [2020 Feb 22 Sat 18:50]: Implement collection!
// TODO [2020 Feb 29 Sat 16:17]: Static constructors, toString
public class UnorderedArray<T> implements Iterable<T> {
    public static final int DEFAULT_SIZE = 16;
    public static final double RESIZE_FACTOR = 1.8;
    public static final int NOT_IN_ARRAY = -1;
    T[] items;
    @Getter
    int size;

    //<editor-fold desc="Constructors">
    public UnorderedArray(int capacity) {
        items = (T[]) new Object[capacity];
    }

    public UnorderedArray() {
        this(DEFAULT_SIZE);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public UnorderedArray(UnorderedArray<? extends T> array) {
        this(array.size);
        size = array.size;
        System.arraycopy(array.items, 0, items, 0, size);
    }

    public UnorderedArray(T[] array, int start, int count) {
        this(count);
        size = count;
        System.arraycopy(array, start, items, 0, size);
    }

    public UnorderedArray(T[] array) {
        this(array, 0, array.length);
    }
    //</editor-fold>

    //<editor-fold desc="Size operations">
    public int capacity() {
        return items.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isNotEmpty() {
        return size > 0;
    }

    protected void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];
        int numberOfElementsToCopy = Math.min(size, newSize);
        System.arraycopy(items, 0, newItems, 0, numberOfElementsToCopy);
        items = newItems;
    }

    protected void expandBackingArray() {
        resize(Math.max(DEFAULT_SIZE / 2, (int) (size * RESIZE_FACTOR)));
    }
    //</editor-fold>

    //<editor-fold desc="Add operations">
    //<editor-fold desc="single item adds">
    public void insert(int index, T value) {
        if (index > size) {
            throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        }
        if (size == items.length) {
            expandBackingArray();
        }
        items[size] = items[index];
        size++;
        items[index] = value;
    }

    public void add(T value) {
        if (size == items.length) {
            expandBackingArray();
        }
        items[size] = value;
        size++;
    }

    public void add(T val1, T val2) {
        if (size + 1 >= items.length) {
            expandBackingArray();
        }
        items[size] = val1;
        items[size + 1] = val2;
        size += 2;
    }

    public void add(T val1, T val2, T val3) {
        if (size + 2 >= items.length) {
            expandBackingArray();
        }
        items[size] = val1;
        items[size + 1] = val2;
        items[size + 2] = val3;
        size += 3;
    }

    public void add(T val1, T val2, T val3, T val4) {
        if (size + 3 >= items.length) {
            expandBackingArray();
        }
        items[size] = val1;
        items[size + 1] = val2;
        items[size + 2] = val3;
        items[size + 3] = val4;
        size += 4;
    }
    //</editor-fold>

    //<editor-fold desc="Add All">
    public void addAll(T[] array, int start, int count) {
        int requiredCapacity = size + count;
        if (requiredCapacity > items.length) {
            expandBackingArray();
        }
        System.arraycopy(array, start, items, size, count);
        size += count;
    }

    public void addAll(T... array) {
        addAll(array, 0, array.length);
    }

    public void addAll(UnorderedArray<? extends T> array) {
        addAll(array.items, 0, array.size);
    }

    public void addAll(UnorderedArray<? extends T> array, int start, int count) {
        if (start + count > array.size) {
            String errorDescription = "start + count must be <= array.size: ";
            String errorParameters = start + " + " + count + " <= " + array.size;
            throw new IllegalArgumentException(errorDescription + errorParameters);
        }
        addAll(array.items, start, count);
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="Retrieval operations">
    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        }
        return items[index];
    }

    public T first() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Array is empty.");
        }
        return items[0];
    }

    public T last() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Array is empty.");
        }
        return items[size - 1];
    }
    //</editor-fold>
    //<editor-fold desc="Index Of & Contains">

    /**
     * Searches for the given value in {@link #items}, comparing identities (==).
     * This should be used when searching for null.
     *
     * @param value the value to search for
     *
     * @return the first index at which the value is found
     */
    public int indexOfIdentity(T value) {
        for (int i = 0; i < size; i++) {
            if (value == items[i]) {
                return i;
            }
        }
        return NOT_IN_ARRAY;
    }

    /**
     * Searches for the given value in {@link #items}, using T's .equals() method for comparison.
     * If the value is null, it invokes and returns {@link #indexOfIdentity(T)}.
     *
     * @param value the value to search for
     *
     * @return the first index at which the value is found
     */
    public int indexOf(T value) {
        if (value == null) {
            return indexOfIdentity(null);
        }
        for (int i = 0; i < size; i++) {
            if (value.equals(items[i])) {
                return i;
            }
        }
        return NOT_IN_ARRAY;
    }

    public boolean containsIdentity(T value) {
        return indexOfIdentity(value) != NOT_IN_ARRAY;
    }

    public boolean contains(T value) {
        return indexOf(value) != NOT_IN_ARRAY;
    }

    public boolean containsIdentity(T val1, T val2) {
        return containsIdentity(val1) && containsIdentity(val2);
    }

    public boolean contains(T val1, T val2) {
        return contains(val1) && contains(val2);
    }

    public boolean containsIdentity(T val1, T val2, T val3) {
        return containsIdentity(val1) & containsIdentity(val2) && containsIdentity(val3);
    }

    public boolean contains(T val1, T val2, T val3) {
        return contains(val1) && contains(val2) && contains(val3);
    }

    public boolean containsIdentity(T val1, T val2, T val3, T val4) {
        return containsIdentity(val1) && containsIdentity(val2) &&
                containsIdentity(val3) && containsIdentity(val4);
    }

    public boolean contains(T val1, T val2, T val3, T val4) {
        return contains(val1) && contains(val2) && contains(val3) && contains(val4);
    }

    public boolean containsAllIdentity(T... values) {
        for (T value : values) {
            if (!containsIdentity(value)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(T... values) {
        for (T value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }
    //</editor-fold>

    //<editor-fold desc="Remove operations">
    public T removeIndex(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        }
        T value = items[index];
        size--;
        items[index] = items[size];
        items[size] = null;
        return value;
    }

    public boolean removeValue(T value) {
        int index = indexOf(value);
        if (index == NOT_IN_ARRAY) {
            return false;
        }
        removeIndex(index);
        return true;
    }

    public boolean removeValueIdentity(T value) {
        int index = indexOfIdentity(value);
        if (index == NOT_IN_ARRAY) {
            return false;
        }
        removeIndex(index);
        return true;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            items[i] = null;
        }
        size = 0;
    }
    //</editor-fold>

    public int countIdentity(T value) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (value == items[i]) {
                count++;
            }
        }
        return count;
    }

    public int count(T value) {
        if (value == null) {
            return countIdentity(null);
        }
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (value.equals(items[i])) {
                count++;
            }
        }
        return count;
    }

    //<editor-fold desc="Equals and hashCode">
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnorderedArray)) {
            return false;
        }
        UnorderedArray<T> that = (UnorderedArray<T>) obj;
        if (this.size != that.size) {
            return false;
        }
        return deepEquals(that);
//        String sadError = "I realized I need to implement lots of stuff before comparing unordered arrays";
//        throw new UnsupportedOperationException(sadError);
    }

    public boolean equalsIdentities(UnorderedArray<T> that) {
        if (this.size != that.size) {
            return false;
        }
        return deepEqualsIdentity(that);
    }

    private boolean deepEqualsIdentity(UnorderedArray<T> that) {
        HashSet<Integer> usedIndices = new HashSet<>(size);
        for (T thatItem : that) {
            int index = firstFreeIndexWithIdentity(usedIndices, thatItem);
            if (index == NOT_IN_ARRAY) {
                return false;
            } else {
                usedIndices.add(index);
            }
        }
        return true;
    }

    private int firstFreeIndexWithIdentity(HashSet<Integer> usedIndices, T value) {
        for (int i = 0; i < size; i++) {
            if (!usedIndices.contains(i) && value == items[i]) {
                return i;
            }
        }
        return NOT_IN_ARRAY;
    }

    private boolean deepEquals(UnorderedArray<T> that) {
        HashSet<Integer> usedIndices = new HashSet<>(size);
        for (T thatItem : that) {
            int index = firstFreeIndexWith(usedIndices, thatItem);
            if (index == NOT_IN_ARRAY) {
                return false;
            } else {
                usedIndices.add(index);
            }
        }
        return true;
    }

    private int firstFreeIndexWith(HashSet<Integer> usedIndices, T value) {
        if (value == null) {
            return firstFreeIndexWithIdentity(usedIndices, null);
        }
        for (int i = 0; i < size; i++) {
            if (!usedIndices.contains(i) && value.equals(items[i])) {
                return i;
            }
        }
        return NOT_IN_ARRAY;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < size; i++) {
            // Same item multiset, in any order, should produce the same hash code.
            hash += items[i].hashCode();
        }
        return hash;
    }
    //</editor-fold>

    @Override
    public Iterator<T> iterator() {
        return new UAIterator();
    }

    // TODO [2020 Feb 22 Sat 18:45]: Figure out how LibGDX re-uses iterators and apply it here.
    // Current problem: I can't just check if the iterator has reached the end.
    // A search operation can end early.
    // So when is the iterator free to be reset?
    public class UAIterator implements Iterator<T> {
        private int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public T next() {
            T item = (T) items[nextIndex];
            nextIndex++;
            return item;
        }
    }
}
