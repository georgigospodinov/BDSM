package bdsm.simple;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnorderedArray<T> {
    T[] items;
    int size;

    //<editor-fold description=""Constructors">
    public UnorderedArray(int capacity) {
        items = (T[]) new Object[capacity];
    }

    public UnorderedArray() {
        this(16);
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

    protected void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];
        int numberOfElementsToCopy = Math.min(size, newSize);
        System.arraycopy(items, 0, newItems, 0, numberOfElementsToCopy);
        items = newItems;
    }

    protected void expandBackingArray() {
        resize(Math.max(8, (int) (size * 1.8)));
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
}
