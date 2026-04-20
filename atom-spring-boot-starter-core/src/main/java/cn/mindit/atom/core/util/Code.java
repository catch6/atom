package cn.mindit.atom.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Catch
 * @since 2023-08-05
 */
public interface Code {

    Map<Class<? extends Code>, Map<Integer, ? extends Code>> CLASS_CODE_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    static <T extends Enum<T> & Code> T getInstance(Class<T> clazz, Integer code) {
        Map<Integer, T> codeMap = (Map<Integer, T>) CLASS_CODE_MAP.computeIfAbsent(clazz, k -> {
            Map<Integer, T> newMap = new ConcurrentHashMap<>();
            for (T value : clazz.getEnumConstants()) {
                newMap.put(value.getCode(), value);
            }
            return newMap;
        });
        return codeMap.get(code);
    }

    Integer getCode();

}
