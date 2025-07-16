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
        int len = characters.length();
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.insert(0, characters.charAt((int) (id % len)));
            id = id / characters.length();
        }
        return sb.toString();
    }

    public static long baseToId(String characters, String base) {
        int len = characters.length();
        long id = 0;
        for (int i = 0; i < base.length(); i++) {
            id = id * len + characters.indexOf(base.charAt(i));
        }
        return id;
    }

}
