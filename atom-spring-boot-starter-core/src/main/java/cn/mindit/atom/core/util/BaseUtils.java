/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.core.util;

/**
 * @author Catch
 * @since 2024-02-02
 */
public abstract class BaseUtils {

    private static final String BASE32 = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String BASE36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String idToBase32(long id) {
        return idToBase(BASE32, id);
    }

    public static long base32ToId(String base32) {
        return baseToId(BASE32, base32);
    }

    public static String idToBase(String characters, long id) {
        if (characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException("Characters cannot be null or empty");
        }

        if (id <= 0) {
            return "";
        }

        int len = characters.length();
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.insert(0, characters.charAt((int) (id % len)));
            id = id / len;
        }
        return sb.toString();
    }

    public static long baseToId(String characters, String base) {
        if (characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException("Characters cannot be null or empty");
        }

        if (base == null || base.isEmpty()) {
            return 0L;
        }

        int len = characters.length();
        long id = 0;
        for (int i = 0; i < base.length(); i++) {
            int index = characters.indexOf(base.charAt(i));
            if (index == -1) {
                return -1L; // 字符不在字符集中，返回 -1
            }
            id = id * len + index;
        }
        return id;
    }

}
