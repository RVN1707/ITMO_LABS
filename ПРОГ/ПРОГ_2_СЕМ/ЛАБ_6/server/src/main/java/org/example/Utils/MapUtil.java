package org.example.Utils;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class MapUtil {
    public Map<String, String> getSubMapByPrefix(Map<String, String> originalMap, String prefix) {
        return originalMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().substring(prefix.length()), // Удаляем префикс из ключа
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        HashMap::new

                ));
    }
}