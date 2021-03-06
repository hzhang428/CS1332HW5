/**
 * A class representing a MapEntry for a HashMap.
 *
 * DO NOT ALTER THIS FILE!!
 *
 * @version 1.0
 * @author CS 1332 TAs
 */
public class MapEntry<K, V> {
    private boolean removed;
    private K key;
    private V value;

    /**
     * Creates a MapEntry object with the given key and value.
     * 
     * @param k key for this entry
     * @param v value for this entry
     */
    public MapEntry(K k, V v) {
        key = k;
        value = v;
    }

    /**
     * Gets the removed status of this entry.
     *
     * @return true if the entry is marked as removed, false otherwise
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Sets the removed status of this entry.
     *
     * @param removed true if the entry should be marked as removed, false
     * otherwise
     */
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    /**
     * Gets the key held by this entry.
     *
     * @return key in this entry.
     */
    public K getKey() {
        return key;
    }

    /**
     * Sets the key held by this entry.
     *
     * @param key key to store in this entry.
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Gets the value held by this entry.
     *
     * @return value in this entry
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets the value held by this entry.
     *
     * @param value value to store in this entry
     */
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s%s: %s", removed ? "(Removed) " : "",
                key.toString(), value.toString());
    }

    // DO NOT USE METHODS BEYOND THIS POINT. This is for testing ONLY!

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (!(o instanceof MapEntry)) {
            return false;
        }
        MapEntry<K, V> that = (MapEntry<K, V>) o;
        return that.getKey().equals(key) && that.getValue().equals(value)
                && (that.isRemoved() == removed);
    }
}
