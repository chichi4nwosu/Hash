public class QuadraticProbingHashSet<E> implements Hash<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    private Object[] hashTable;
    private int size;

    public QuadraticProbingHashSet() {
        hashTable = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public int length() {
        return hashTable.length;
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
        if (loadFactor() >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
        
        int index = quadraticProbe(value.hashCode());
        int count = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].equals(value)) {
                return false; // Element already exists
            }
            index = (index + count * count) % hashTable.length;
            count++;
        }

        hashTable[index] = value;
        size++;
        return true;
    }

    @Override
    public boolean contains(E value) {
        int index = quadraticProbe(value.hashCode());
        int count = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].equals(value)) {
                return true;
            }
            index = (index + count * count) % hashTable.length;
            count++;
        }

        return false;
    }

    @Override
    public boolean remove(E value) {
        int index = quadraticProbe(value.hashCode());
        int count = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].equals(value)) {
                hashTable[index] = null;
                size--;
                return true;
            }
            index = (index + count * count) % hashTable.length;
            count++;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object item : hashTable) {
            if (item != null) {
                sb.append(item).append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove the trailing comma and space
        }
        sb.append("]");
        return sb.toString();
    }

    private void rehash() {
        Object[] oldTable = hashTable;
        hashTable = new Object[hashTable.length * 2];
        size = 0;

        for (Object item : oldTable) {
            if (item != null) {
                add((E) item);
            }
        }
    }

    private double loadFactor() {
        return (double) size / hashTable.length;
    }

    private int quadraticProbe(int hashCode) {
        return Math.abs(hashCode) % hashTable.length;
    }
}
