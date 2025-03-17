package cc.allio.uno.core.util.list;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * base on CAS Lock Free ArrayList. inside used {@link AtomicReferenceArray} as implementation.
 *
 * <p>features:</p>
 *
 * <ul>
 *     <li>automatic expand capacity.{@link #grow(int)}</li>
 *     <li>automatic shrink capacity.{@link #eliminate(int)}</li>
 *     <li>used {@link AtomicReferenceArray}</li>
 * </ul>
 *
 * @author j.x
 * @since 1.2.1
 */
public class LockFreeArrayList<T> extends AbstractList<T> {

    private AtomicReferenceArray<T> array;
    private final AtomicInteger length;
    private final AtomicInteger size;

    public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    public LockFreeArrayList(int length) {
        this.array = new AtomicReferenceArray<>(length);
        this.length = new AtomicInteger(length);
        this.size = new AtomicInteger(0);
    }

    public LockFreeArrayList(T[] array) {
        this.array = new AtomicReferenceArray<>(array);
        this.length = new AtomicInteger(array.length);
        this.size = new AtomicInteger(0);
    }

    public LockFreeArrayList(List<T> list) {
        this.array = new AtomicReferenceArray<>(list.size());
        this.length = new AtomicInteger(list.size());
        this.size = new AtomicInteger(0);
        for (int i = 0; i < list.size(); i++) {
            add(i, list.get(i));
        }
    }

    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, size.get());
        T oldValue = array.get(index);
        array.set(index, element);
        return oldValue;
    }

    @Override
    public void add(int index, T element) {
        int oldSize = size.get();
        if (oldSize == length.get()) {
            grow();
        }

        // +1
        final int newSize = oldSize + 1;
        casToSpin(size, oldSize, newSize);
        array.set(index, element);
    }

    @Override
    public T remove(int index) {
        Objects.checkIndex(index, size.get());
        T oldValue = array.get(index);
        eliminate(index);
        return oldValue;
    }

    @Override
    public T get(int index) {
        return array.get(index);
    }

    @Override
    public int size() {
        return size.get();
    }

    void grow() {
        grow(this.length.get() + 1);
    }

    /**
     * lock free grow array capacity.
     * <p>
     * refer to {@link ArrayList} grow method.
     *
     * @param minCapacity the expand mini capacity
     */
    void grow(int minCapacity) {
        int oldCapacity = length.get();
        if (oldCapacity > 0) {
            int newCapacity = newLength(oldCapacity,
                    minCapacity - oldCapacity, /* minimum growth */
                    oldCapacity >> 1           /* preferred growth */);
            // spin lock
            casToSpin(length, oldCapacity, newCapacity);
            AtomicReferenceArray<T> newArray = new AtomicReferenceArray<>(newCapacity);
            // copy data
            for (int i = 0; i < oldCapacity; i++) {
                newArray.set(i, array.get(i));
            }
            this.array = newArray;
        }
    }


    /**
     * lock free eliminate element
     * <p>
     * refer to {@link ArrayList} fastRemove method
     *
     * @param index the index
     */
    void eliminate(int index) {
        int oldSize = size.get();
        final int newSize = size.get() - 1;
        if (newSize > index) {
            // corresponding System.arraycopy
            // 1. first copy index left of [0, index)
            for (int i = 0; i < index; i++) {
                array.set(i, array.get(i + 1));
            }
            // 2. then the remove index right elements toward left shift one position
            for (int i = 0; i < newSize - index; i++) {
                // index + i = shift position
                // index + i + 1 = the i-th element
                array.set(index + i, array.get(index + i + 1));
            }
        }

        casToSpin(size, oldSize, newSize);
        array.set(newSize, null);
    }


    // CAS (Compare And Swap) to Spin
    void casToSpin(AtomicInteger atomic, int expected, int value) {
        while (!atomic.compareAndSet(expected, value)) ;
    }

    // copy to ArraysSupport.newLength
    public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        // preconditions not checked because of inlining
        // assert oldLength >= 0
        // assert minGrowth > 0

        int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        } else {
            // put code cold in a separate method
            return hugeLength(oldLength, minGrowth);
        }
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError(
                    "Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
            return SOFT_MAX_ARRAY_LENGTH;
        } else {
            return minLength;
        }
    }
}
