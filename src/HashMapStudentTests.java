import org.junit.Test;
import org.omg.CORBA.TIMEOUT;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for your implementation of HashMap.
 *
 * @author gbianco6
 * @author CS 1332 TAs
 * @version 2.4
 */

public class HashMapStudentTests {
    private static final int TIMEOUT = 500;
    private HashMapInterface<Integer, String> map;
    private MapEntry<Integer, String>[] expected;
    private HashMapInterface<String, String> hashCheckMap;

    /**
     * Tests for correct backingArray size in constructors()
     */
    @Test(timeout = TIMEOUT)
    public void testConstructors() {
        map = new HashMap<>();
        assertEquals(HashMapInterface.INITIAL_CAPACITY, map.getArray().length);
        assertEquals(0, map.size());

        map = new HashMap<>(101);
        assertEquals(101, map.getArray().length);
        assertEquals(0, map.size());
    }


    /**
     * Tests basic size, values, backingArray length and positioning, and
     * value overwrite
     */
    @Test(timeout = TIMEOUT)
    public void testPut1() {
        map = new HashMap<>(10);
        assertEquals(0, map.size());
        assertEquals(10, map.getArray().length);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(12, "twelve");
        map.put(11, "eleven");
        map.put(3, "new three");

        assertEquals(5, map.size());
        assertEquals(10, map.getArray().length);

        expected = (MapEntry<Integer, String>[]) new MapEntry[10];
        expected[1] = new MapEntry<>(1, "one");
        expected[2] = new MapEntry<>(2, "two");
        expected[3] = new MapEntry<>(3, "new three");
        expected[4] = new MapEntry<>(12, "twelve");
        expected[5] = new MapEntry<>(11, "eleven");
        assertArrayEquals(expected, map.getArray());
    }

    /**
     * Tests for resizing of the backingArray and that the correct hash
     * compression and resize amount in put() is being used
     */
    @Test(timeout = TIMEOUT)
    public void testPut2() {
        map = new HashMap<>(10);
        assertEquals(0, map.size());
        assertEquals(10, map.getArray().length);
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        map.put(5, "5");
        map.put(6, "6");
        map.put(7, "7");
        map.put(8, "8");
        map.put(15, "15");
        map.put(40, "40");

        assertEquals(10, map.size());
        assertEquals(23, map.getArray().length);

        expected = (MapEntry<Integer, String>[]) new MapEntry[23];
        for (int i = 1; i <= 8; i++) {
            expected[i] = new MapEntry<>(i, String.valueOf(i));
        }
        expected[15] = new MapEntry<>(15, "15");
        expected[17] = new MapEntry<>(40, "40");
        assertArrayEquals(expected, map.getArray());
    }

    /**
     * Tests for resizing of the backingArray and that the correct hash
     * compression and resize amount in put() is being used
     */
    @Test(timeout = TIMEOUT)
    public void testPut3() {
        map = new HashMap<>(10);
        assertEquals(0, map.size());
        assertEquals(10, map.getArray().length);
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        map.remove(3);
        map.put(13, "13");
        map.put(23, "23");

        assertEquals(5, map.size());
        assertEquals(10, map.getArray().length);

        expected = (MapEntry<Integer, String>[]) new MapEntry[10];
        expected[1] = new MapEntry<>(1, "1");
        expected[2] = new MapEntry<>(2, "2");
        expected[3] = new MapEntry<>(13, "13");
        expected[4] = new MapEntry<>(4, "4");
        expected[5] = new MapEntry<>(23, "23");
        assertArrayEquals(expected, map.getArray());
    }

    /**
     * Tests for correct size, removed boolean value, and existance of
     * removed data in the unmodified backingArray
     */
    @Test(timeout = TIMEOUT)
    public void testRemove1() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        assertEquals("two", map.remove(2));

        expected = (MapEntry<Integer, String>[]) new MapEntry[6];
        expected[1] = new MapEntry<>(1, "one");
        MapEntry<Integer, String> removedEntry = new MapEntry<>(2, "two");
        removedEntry.setRemoved(true);
        expected[2] = removedEntry;
        expected[3] = new MapEntry<>(3, "three");
        assertArrayEquals(expected, map.getArray());
        assertEquals(2, map.size());
    }

    /**
     * Tests to make sure that clear() is not used when remove is called on
     * {@code size} amount of times
     */
    @Test(timeout = TIMEOUT)
    public void testRemove2() {
        map = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            map.put(i, String.valueOf(i));
        }
        assertEquals(20, map.size());
        assertEquals(49, map.getArray().length);

        for (int i = 0; i < 20; i++) {
            map.remove(i);
        }
        assertEquals(0, map.size());
        assertEquals(49, map.getArray().length);
    }

    /**
     * Tests to make sure that remove continues through the list if not at the
     * expected location in the array
     */
    @Test(timeout = TIMEOUT)
    public void testRemove3() {
        map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(12, "12");
        String holder = map.remove(12);
        assertEquals("12", holder);
    }

    /**
     * Tests basics of get()
     */
    @Test(timeout = TIMEOUT)
    public void testGet1() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        assertEquals("three", map.get(3));
    }

    /**
     * Tests to make sure that get doesn't get weirdly affected by the remove
     * method and that it doesn't return removed items
     */
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testGet2() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(-4, "four");
        map.remove(3);
        assertEquals("four", map.get(-4));
        String three = map.get(3);
    }

    /**
     * Tests for basic count of values
     */
    @Test(timeout = TIMEOUT)
    public void testCount1() {
        map = new HashMap<>(10);
        map.put(1, "yes");
        map.put(2, "no");
        map.put(3, "yes");
        map.put(9, "yes");
        assertEquals(3, map.count("yes"));
    }

    /**
     * Tests to make sure that count doesn't include removed values
     */
    @Test(timeout = TIMEOUT)
    public void testCount2() {
        map = new HashMap<>(10);
        map.put(1, "yes");
        map.put(2, "no");
        map.put(3, "yes");
        map.put(9, "yes");
        map.remove(3);
        assertEquals(2, map.count("yes"));
    }

    /**
     * Tests for basic contains()
     */
    @Test(timeout = TIMEOUT)
    public void testContains1() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        assertTrue(map.containsKey(2));
        assertFalse(map.containsKey(8));
    }

    /**
     * Tests to makes sure that contains is not affected by removed values
     */
    @Test(timeout = TIMEOUT)
    public void testContains2() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(8, "eight");
        map.remove(2);
        assertFalse(map.containsKey(2));
        assertTrue(map.containsKey(8));
    }

    /**
     * Tests to make sure that contains continues searching if the key is not
     * at the expected index
     */
    @Test(timeout = TIMEOUT)
    public void testContains3() {
        map = new HashMap<>(8);
        map.put(1, "one");
        map.put(2, "two");
        map.put(-3, "three");
        map.put(11, "eleven");
        map.put(19, "nineteen");
        assertEquals("eleven", map.getArray()[4].getValue());
        assertEquals("nineteen", map.getArray()[5].getValue());
        map.remove(-3);
        assertTrue(map.containsKey(11));
        assertTrue(map.containsKey(19));
        assertFalse(map.containsKey(-3));
    }

    /**
     * Tests to make sure that clear resets size and that backingArray has
     * been reset by creating a new blank array
     */
    @Test(timeout = TIMEOUT)
    public void testClear() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        String originalId = map.getArray().toString();
        map.clear();

        String newId = map.getArray().toString();
        expected = (MapEntry<Integer, String>[]) new MapEntry[10];
        assertEquals(HashMapInterface.INITIAL_CAPACITY, map.getArray().length);
        assertEquals(0, map.size());
        assertArrayEquals(map.getArray(), expected);
        assertNotEquals(originalId, newId);
    }

    /**
     * Tests basic size
     */
    @Test(timeout = TIMEOUT)
    public void testSize() {
        map = new HashMap<>(6);
        map.put(1, "one");
        assertEquals(1, map.size());
        map.put(2, "two");
        assertEquals(2, map.size());
        map.put(3, "three");
        assertEquals(3, map.size());
        map.remove(2);
        assertEquals(2, map.size());
    }

    /**
     * Tests basic keyset retrieval
     */
    @Test(timeout = TIMEOUT)
    public void testKeySet() {
        map = new HashMap<>(10);
        map.put(1, "one");
        map.put(5, "five");
        map.put(13, "thirteen");
        map.put(-2, "-2");

        Set<Integer> expectedKeys = new HashSet<>();
        expectedKeys.add(13);
        expectedKeys.add(5);
        expectedKeys.add(1);
        expectedKeys.add(-2);
        assertEquals(expectedKeys, map.keySet());
    }

    /**
     * Tests basic values list retrieval
     */
    @Test(timeout = TIMEOUT)
    public void testValues() {
        map = new HashMap<>(10);
        map.put(1, "one");
        map.put(5, "five");
        map.put(13, "thirteen");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("one");
        expectedValues.add("thirteen");
        expectedValues.add("five");
        assertEquals(expectedValues, map.values());
    }

    /**
     * Tests for basic manual resizing with no loadfactor issues
     */
    @Test(timeout = TIMEOUT)
    public void testResize1() {
        map = new HashMap<>(10);
        map.put(5, "five");
        map.put(13, "thirteen");
        map.resizeBackingArray(20);

        expected = (MapEntry<Integer, String>[]) new MapEntry[20];
        expected[5] = new MapEntry<>(5, "five");
        expected[13] = new MapEntry<>(13, "thirteen");
        assertArrayEquals(expected, map.getArray());
        assertEquals(2, map.size());
    }

    /**
     * Tests that resizing happens at critical loadfactor
     */
    @Test(timeout = TIMEOUT)
    public void testResize2() {
        map = new HashMap<>();
        assertEquals(0, map.size());
        assertEquals(10, map.getArray().length);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        map.put(5, "five");
        map.put(6, "six");
        map.put(7, "seven");
        assertEquals(7, map.size());
        assertEquals(10, map.getArray().length);
        map.put(8, "eight");
        assertEquals(8, map.size());
        assertEquals(23, map.getArray().length);
    }

    /**
     * Tests to make sure that a legal manual resize can happen if all values
     * can fit, then makes sure that sequential resizing uses the current
     * length of the backingArray and not {@constant INTIAL_CAPACITY}
     */
    @Test(timeout = TIMEOUT)
    public void testResize3() {
        map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        map.put(5, "5");
        map.put(6, "6");
        map.put(7, "7");
        map.put(8, "8");
        assertEquals(8, map.size());
        map.resizeBackingArray(9);

        expected = (MapEntry<Integer, String>[]) new MapEntry[9];
        for (int i = 1; i <= 8; i++) {
            expected[i] = new MapEntry<>(i, String.valueOf(i));
        }
        assertArrayEquals(expected, map.getArray());

        map.put(9, "10");
        assertEquals(9, map.size());
        assertEquals(21, map.getArray().length);
    }

    /**
     * Tests for an thrown exception when you attempt to resize using too low
     * a length parameter
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void exceptionResize() {
        map = new HashMap<>();
        assertEquals(0, map.size());
        assertEquals(10, map.getArray().length);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        map.put(5, "five");
        map.put(6, "six");
        map.resizeBackingArray(4);
    }

    /**
     * Tests for a thrown exception when you try to pass in a null key for
     * contains
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void exceptionContains() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.containsKey(null);
    }

    /**
     * Tests for a thrown exception when you try to pass in a null key for
     * count
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void exceptionCount() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.count(null);
    }

    /**
     * Tests for a thrown exception when you try to pass in a null key for
     * get
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void exceptionGetNull() {
        map = new HashMap<>(6);
        map.get(null);
    }

    /**
     * Tests for a thrown exception when you try to pass in a key for get
     * that is not in the map
     */
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void exceptionGetNotInMap() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.get(4);
    }

    /**
     * Tests for a thrown exception when you try to pass in a null key for
     * remove
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void exceptionRemoveNull() {
        map = new HashMap<>(6);
        map.remove(null);
    }

    /**
     * Tests for a thrown exception when you try to pass in a key for remove
     * that is not in the map
     */
    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void exceptionRemoveNotInMap() {
        map = new HashMap<>(6);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.remove(4);
    }

    /**
     * Tests for a thrown exception when you try to pass in a null key for put
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void exceptionPutKey() {
        map = new HashMap<>(6);
        map.put(null, "string");
    }

    /**
     * Tests for a thrown exception when you try to pass in a null value for put
     */
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void exceptionPutValue() {
        map = new HashMap<>(6);
        map.put(1, null);
    }

    /**
     * Tests to make sure that you are resizing the array BEFORE attempting
     * in insert (as is the contract in HW05 Version 1.1)
     */
    @Test(timeout = TIMEOUT)
    public void testResizeBeforePut() {
        map = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            map.put(i, String.valueOf(i));
        }
        assertEquals(10, map.getArray().length);
        assertEquals(7, map.size());
        map.put(7, "new seven");
        assertEquals(7, map.size());
        assertEquals(23, map.getArray().length);
    }

    /**
     * Tests to make sure hashcode() is called in the compression function
     */
    @Test(timeout = TIMEOUT)
    public void testProperHashCode() {
        hashCheckMap = new HashMap<>();
        hashCheckMap.put("This", "shouldn't fail");
    }

    /**
     * Tests to make sure that put wraps around if necessary
     */
    @Test(timeout = TIMEOUT)
    public void testPut4() {
        map = new HashMap<>(10);
        assertEquals(0, map.size());
        assertEquals(10, map.getArray().length);
        map.put(5, "5");
        map.put(6, "6");
        map.put(-7, "-7");
        map.put(8, "8");
        map.put(9, "9");
        map.put(17, "17");
        assertEquals(6, map.size());
        assertEquals(10, map.getArray().length);

        expected = (MapEntry<Integer, String>[]) new MapEntry[10];
        expected[5] = new MapEntry<>(5, "5");
        expected[6] = new MapEntry<>(6, "6");
        expected[7] = new MapEntry<>(-7, "-7");
        expected[8] = new MapEntry<>(8, "8");
        expected[9] = new MapEntry<>(9, "9");
        expected[0] = new MapEntry<>(17, "17");

        assertArrayEquals(expected, map.getArray());
    }
}
