package bdsm.simple;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Iterator;

@SuppressWarnings("unchecked")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
// TODO [2020 Feb 22 Sat 18:50]: Implement collection!
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

    public int capacity() {
        return items.length;
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

    //<editor-fold desc="Add operations">
    //<editor-fold desc="single item adds">
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

    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        }
        return items[index];
    }
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
        String sadError = "I realized I need to implement lots of stuff before comparing unordered arrays";
        throw new UnsupportedOperationException(sadError);
    }

    @Override
    public Iterator<T> iterator() {
        return new UAIterator<>();
    }

    // TODO [2020 Feb 22 Sat 18:45]: Figure out how LibGDX re-uses iterators and apply it here.
    // Current problem: I can't just check if the iterator has reached the end.
    // A search operation can end early.
    // So when is the iterator free to be reset?
    public class UAIterator<T> implements Iterator<T> {
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
