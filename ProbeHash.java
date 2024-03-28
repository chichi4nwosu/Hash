import java.util.Collection;
import java.util.Vector;

public class ProbeHash<E> implements Hash<E> {
    private Vector<E> hashTable;
    private Vector<Integer> statusTable; // Status table indicating whether each slot is removed, empty, or has an element
    private int capacity;
    private int size;
    private final double LOAD_FACTOR_THRESHOLD = 0.75;

    // Constructors
    public ProbeHash() {
        this.capacity = 10;
        this.size = 0;
        this.hashTable = new Vector<>(capacity);
        this.statusTable = new Vector<>(capacity);
        for (int i = 0; i < capacity; i++) {
            hashTable.add(null);
            statusTable.add(0); // Initialize status table with 0 (empty)
        }
    }

    public ProbeHash(Collection<E> list) {
        this();
        for (E element : list) {
            add(element);
        }
    }

    public ProbeHash(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        this.hashTable = new Vector<>(capacity);
        this.statusTable = new Vector<>(capacity);
        for (int i = 0; i < capacity; i++) {
            hashTable.add(null);
            statusTable.add(0); // Initialize status table with 0 (empty)
        }
    }

    // Helper method to calculate hash index
    private int hashFunction(E value) {
        return Math.abs(value.hashCode() % capacity);
    }

    // Method to double the size of the hash table
    private void rehash() {
        int newCapacity = capacity * 2;
        Vector<E> newHashTable = new Vector<>(newCapacity);
        Vector<Integer> newStatusTable = new Vector<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newHashTable.add(null);
            newStatusTable.add(0); // Initialize status table with 0 (empty)
        }
        for (int i = 0; i < capacity; i++) {
            if (hashTable.get(i) != null) {
                int newIndex = Math.abs(hashTable.get(i).hashCode() % newCapacity);
                while (newHashTable.get(newIndex) != null) {
                    newIndex = (newIndex + 1) % newCapacity; // Linear probing
                }
                newHashTable.set(newIndex, hashTable.get(i));
                newStatusTable.set(newIndex, 1); // Set status to 1 (has an element)
            }
        }
        hashTable = newHashTable;
        statusTable = newStatusTable;
        capacity = newCapacity;
    }

    // Implemented methods from Hash interface
    @Override
    public int length() {
        return capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E value) {
        if (contains(value)) {
            return false;
        }
        if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
        int index = hashFunction(value);
        while (hashTable.get(index) != null) {
            index = (index + 1) % capacity; // Linear probing
        }
        hashTable.set(index, value);
        statusTable.set(index, 1); // Set status to 1 (has an element)
        size++;
        return true;
    }

    @Override
    public boolean contains(E value) {
        int index = hashFunction(value);
        int startIndex = index;
        do {
            if (hashTable.get(index) != null && hashTable.get(index).equals(value)) {
                return true;
            }
            index = (index + 1) % capacity; // Linear probing
        } while (index != startIndex);
        return false;
    }

    @Override
    public boolean remove(E value) {
        int index = hashFunction(value);
        int startIndex = index;
        do {
            if (hashTable.get(index) != null && hashTable.get(index).equals(value)) {
                hashTable.set(index, null);
                statusTable.set(index, -1); // Set status to -1 (removed)
                size--;
                return true;
            }
            index = (index + 1) % capacity; // Linear probing
        } while (index != startIndex);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < capacity; i++) {
            if (hashTable.get(i) != null) {
                sb.append(hashTable.get(i));
                sb.append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove trailing comma and space
        }
        sb.append("]");
        return sb.toString();
    }
}
