import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Your implementation of HashMap.
 * 
 * @author Hao Zhang
 * @version 1.0
 */
public class HashMap<K, V> implements HashMapInterface<K, V> {

    // Do not make any new instance variables.
    private MapEntry<K, V>[] backingArray;
    private int size;

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code INITIAL_CAPACITY}.
     *
     * Use constructor chaining.
     */
    @SuppressWarnings("unchecked")
    public HashMap() {
        this(INITIAL_CAPACITY);
    }


    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code initialCapacity}.
     *
     * You may assume {@code initialCapacity} will always be positive.
     *
     * @param initialCapacity initial capacity of the backing array
     */
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity) {
        backingArray = new MapEntry[initialCapacity];
    }


    /**
     * Adds the given key-value pair to the HashMap.
     * If the key is already in the HashMap, then replace the value with the new
     * value.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * After putting the new element into your hash map check if the current
     * load factor is strictly greater than the {@code MAX_LOAD_FACTOR}.
     * If it is, resize your backing array.
     *
     * @param key key to add into the HashMap
     * @param value value to add into the HashMap
     * @throws IllegalArgumentException if key or value is null
     * @return null if the key was not already in the map or was deleted.
     * Otherwise, return the old value associated with it
     */
    @Override
    public V put(K key, V value) {
        if ((double) (size + 1) / backingArray.length > MAX_LOAD_FACTOR) {
            resizeBackingArray(backingArray.length * 2 + 3);
        }
        return Put(key, value);
    }

    private V Put(K key, V value) {
        V old_value = null;
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        int p = linearProb1(key);
        if (backingArray[p] == null) {
            backingArray[p] = new MapEntry<>(key, value);
            size += 1;
        } else if (backingArray[p].isRemoved()) {
            backingArray[p].setRemoved(false);
            backingArray[p] = new MapEntry<>(key, value);
            size += 1;
        } else {
            old_value = backingArray[p].getValue();
            backingArray[p].setValue(value);
        }
        return old_value;
    }

    private int linearProb1(K key) {
        int position = Math.abs(key.hashCode() % backingArray.length);
        while (backingArray[position] != null && !(backingArray[position].isRemoved()) &&
                !(backingArray[position].getKey().equals(key))) {
            position = (position + 1) % backingArray.length;
        }
        return position;
    }

    /**
     * Removes the entry associated with the given key.
     *
     * When removing, do not set the index in the array to {@code null}, nor set
     * the key and value to {@code null}; mark the entry as removed.
     *
     * @param key the key to remove
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key does not exist
     * @return the value previously associated with the key
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int p = linearProb2(key);
        if (backingArray[p] == null) {
            throw new java.util.NoSuchElementException();
        } else {
            backingArray[p].setRemoved(true);
            size -= 1;
            return backingArray[p].getValue();
        }
    }

    private int linearProb2(K key) {
        int position = Math.abs(key.hashCode() % backingArray.length);
        while (backingArray[position] != null && !(backingArray[position].getKey().equals(key))) {
            position = (position + 1) % backingArray.length;
        }
        return position;
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key is not in the map
     * @return the value associated with the given key
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int p = linearProb2(key);
        if (backingArray[p] == null || backingArray[p].isRemoved()) {
            throw new java.util.NoSuchElementException();
        } else {
            return backingArray[p].getValue();
        }
    }


    /**
     * Counts the number of entries containing the specified value.
     *
     * @param value the value to count
     * @throws IllegalArgumentException if the value is null
     * @return the number of entries with the given value
     */
    @Override
    public int count(V value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        int count = 0;
        for (MapEntry<K, V> entry : backingArray) {
            if (entry !=null && !entry.isRemoved()) {
                if (entry.getValue().equals(value)) {
                    count += 1;
                }
            }
        }
        return count;
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for
     * @throws IllegalArgumentException if key is null
     * @return whether or not the key is in the map
     */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int p = linearProb2(key);
        if (backingArray[p] == null) {
            return false;
        } else {
            return !backingArray[p].isRemoved();
        }
    }

    /**
     * Clears the array and resets it to the default size.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        backingArray = new MapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the number of elements in the map.
     *
     * @return number of elements in the HashMap
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     * Use {@code java.util.HashSet}.
     *
     * @return set of keys in this map
     */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<K>();
        for (MapEntry<K, V> entry: backingArray) {
            if (entry != null && !entry.isRemoved()) {
                set.add(entry.getKey());
            }
        }
        return set;
    }

    /**
     * Returns a List view of the values contained in this map.
     * Use any class that implements the List interface.
     *
     * The order of values in your list should be the same as they appear in
     * your backing array.
     *
     * @return list of values in this map
     */
    @Override
    public List<V> values() {
        List<V> list = new ArrayList<V>();
        for (MapEntry<K, V> entry : backingArray) {
            if (entry != null && !entry.isRemoved()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    /**
     * Resize the backing array to {@code length}. This may mean that after
     * resizing, the load factor may exceed {@code MAX_LOAD_FACTOR}. In this
     * case, do NOT regrow the table; keep it at {@code length}.
     *
     * Remember that you cannot just simply copy the entries over to the new
     * array.
     *
     * @param length new length of the backing table
     * @throws IllegalArgumentException if length is less than the number of
     * entries in the hash map.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void resizeBackingArray(int length) {
        if (length < size) {
            throw new IllegalArgumentException("resized array length cannot " +
                    "be less than the current length");
        }
        MapEntry<K, V>[] backingArray2 = backingArray;
        backingArray = new MapEntry[length];
        size = 0;
        for (MapEntry<K, V> entry : backingArray2) {
            if (entry != null && !entry.isRemoved()) {
                Put(entry.getKey(), entry.getValue());
            }
        }
    }

    // DO NOT MODIFY OR USE CODE BEYOND THIS POINT.

    @Override
    public MapEntry<K, V>[] getArray() {
        return backingArray;
    }

}
