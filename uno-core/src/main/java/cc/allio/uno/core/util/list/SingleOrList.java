package cc.allio.uno.core.util.list;

import java.util.*;

/**
 * support quick get single.
 *
 * @author j.x
 * @since 0.2.0
 */
public class SingleOrList<T> implements List<T> {

    private final List<T> actual;

    public SingleOrList() {
        this(new ArrayList<>());
    }

    public SingleOrList(List<T> actual) {
        this.actual = actual;
    }

    @Override
    public int size() {
        return actual.size();
    }

    @Override
    public boolean isEmpty() {
        return actual.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return actual.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return actual.iterator();
    }

    @Override
    public Object[] toArray() {
        return actual.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return actual.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return actual.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return actual.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return actual.contains(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return actual.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return actual.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return actual.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return actual.retainAll(c);
    }

    @Override
    public void clear() {
        actual.clear();
    }

    @Override
    public T get(int index) {
        return actual.get(index);
    }

    @Override
    public T set(int index, T element) {
        return actual.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        actual.add(index, element);
    }

    @Override
    public T remove(int index) {
        return actual.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return actual.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return actual.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return actual.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return actual.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return actual.subList(fromIndex, toIndex);
    }

    /**
     * return single value
     *
     * @throws NoSuchElementException
     */
    public T single() {
        return getFirst();
    }
}
