package datastructures;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class ArrayList<T> implements Iterable<T> {
    private Object[] elementData;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    public ArrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }
    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0){throw new IllegalArgumentException("Illegal Capacity");}
        this.elementData = new Object[initialCapacity];
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public boolean add(T element) {
        ensureCapacity(size + 1);
        elementData[size++] = element;
        return true;
    }
    public void add(int index, T element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }
    @SuppressWarnings("unchecked")
    public T get(int index) {
        rangeCheck(index);
        return (T) elementData[index];
    }
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        rangeCheck(index);
        T oldValue = (T) elementData[index];
        elementData[index] = element;
        return oldValue;
    }
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        rangeCheck(index);
        T oldValue = (T) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null; // Clear to let GC work
        return oldValue;
    }
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }
    private void ensureCapacity(int minCapacity) {
        if (minCapacity - elementData.length > 0) {
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x growth
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }
    private void rangeCheck(int index) {
        if (index < 0 || index >= size){throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);}
    }
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);}
    }
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            public boolean hasNext() { return cursor < size; }
            @SuppressWarnings("unchecked")
            public T next() {
                if (cursor >= size) throw new NoSuchElementException();
                return (T) elementData[cursor++];
            }
        };
    }
    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(elementData, size));
    }
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }
    public void removeDuplicates() {
        if (size <= 1){return;}
        int uniqueIndex = 0;
        for (int i = 1; i < size; i++) {
            if (!elementData[i].equals(elementData[uniqueIndex])) {
                uniqueIndex++;
                elementData[uniqueIndex] = elementData[i];
            }
        }
        for (int i = uniqueIndex + 1; i < size; i++) {
            elementData[i] = null;
        }
        size = uniqueIndex + 1;
    }
}