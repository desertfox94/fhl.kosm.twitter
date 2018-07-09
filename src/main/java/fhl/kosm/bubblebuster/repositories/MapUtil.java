package fhl.kosm.bubblebuster.repositories;

import java.util.*;
import java.util.function.Consumer;

public class MapUtil {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        return sortByValue(map, l -> {
            l.sort(Map.Entry.comparingByValue());
            Collections.reverse(l);
        });
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return sortByValue(map, l -> l.sort(Map.Entry.comparingByValue()));
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Consumer<List> sorting) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        sorting.accept(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
