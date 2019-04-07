package utils.cache;

import java.util.LinkedHashMap;

public class LRU<K, V> {
int capacity;

LRU(int capacity) {
    this.capacity = capacity;
}

LinkedHashMap<K, V> ma = new LinkedHashMap<>();

V get(K k) {
    V v = ma.get(k);
    update(k, v);
    return v;
}

void put(K k, V v) {
    if (ma.size() >= this.capacity) {
        K first = ma.keySet().iterator().next();
        ma.remove(first);
    }
    if (ma.containsKey(k)) ma.remove(k);
    ma.put(k, v);
}

void update(K k, V v) {
    ma.remove(k);
    ma.put(k, v);
}

int getCapacity() {
    return capacity;
}

int size() {
    return this.ma.size();
}

}
