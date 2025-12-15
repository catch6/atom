package cn.mindit.atom.core.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * UUID v7
 * <p>
 * MSB 高 64 位：48 位时间戳 + 4 位版本（7=0111）+ 12 位序列号
 * LSB 低 64 位：2 位变体（10）+ 62 位随机数
 *
 * @author Catch
 * @since 2025-12-14
 */
public class UUIDv7 {

    private UUIDv7() {
    }

    /**
     * 生成 UUID v7。
     *
     * <p>格式如下：
     * <ul>
     *   <li>MSB 高 64 位：48 位时间戳 + 4 位版本（7=0111）+ 12 位随机数（取自一个 64 位随机值）</li>
     *   <li>LSB 低 64 位：2 位变体（二进制10）+ 62 位随机数（其中 52 位来自 64 位随机值，另外 10 位来自 32 位随机值，总计提供 74 位熵）</li>
     * </ul>
     *
     * @return 表示 UUIDv7 的 {@link UUID} 实例。
     * @see UUID
     * @see ThreadLocalRandom
     */
    public static UUID uuid() {
        // 1) 获取当前毫秒时间戳，并截断为 48 位
        long currentMillis = System.currentTimeMillis();
        long ts48 = currentMillis & 0xFFFFFFFFFFFFL;  // 48 位掩码

        // 2) 从 ThreadLocalRandom 获取 74 位熵：64 位 + 32 位
        long random64 = ThreadLocalRandom.current().nextLong();
        int random32 = ThreadLocalRandom.current().nextInt();

        // 组装高 64 位（MSB）：
        //   [ 48 位时间戳 ] [ 4 位版本=7 ] [ 12 位高位随机数 ]
        long high = (ts48 << 16);                         // 将 48 位毫秒时间戳放入 high 的高 48 位
        long randHigh12 = (random64 >>> 52) & 0x0FFFL;    // random64 的最高 12 位
        high |= randHigh12;                              // 高 48位 + 低 12位
        high |= 0x0000000000007000L;                     // 高 48位 + 低 12位 + 中间的4位版本号

        // 组装低 64 位（LSB）：
        //   [ 2 位变体=10 ] [ random64 的低 52 位 ] [ random32 的高 10 位 ]
        long low = 0x8000000000000000L;                   // 在位 64–65 设置变体 0b10
        long randLow52 = random64 & 0x000FFFFFFFFFFFFFL;  // random64 的低 52 位
        int rand32High10 = (random32 >>> 22) & 0x3FF;     // random32 的高 10 位
        low |= (randLow52 << 10);                         // 将 52 位放到位 66–117
        low |= rand32High10;                              // 将 10 位放到位 118–127

        return new UUID(high, low);
    }

    /**
     * 生成 UUID v7。
     *
     * <p>格式如下：
     * <ul>
     *   <li>MSB 高 64 位：48 位时间戳 + 4 位版本（7=0111）+ 12 位随机数（取自一个 64 位随机值）</li>
     *   <li>LSB 低 64 位：2 位变体（二进制10）+ 62 位随机数（其中 52 位来自 64 位随机值，另外 10 位来自 32 位随机值，总计提供 74 位熵）</li>
     * </ul>
     *
     * @return 表示 UUIDv7 的 String。
     * @see UUID
     * @see ThreadLocalRandom
     */
    public static String uuidStr() {
        return uuid().toString();
    }

    /**
     * 从 UUIDv7 提取时间戳
     */
    public static long timestamp(UUID uuid) {
        return uuid.getMostSignificantBits() >>> 16;
    }

    /**
     * 从 UUIDv7 提取时间戳
     */
    public static long timestamp(String uuidStr) {
        return timestamp(UUID.fromString(uuidStr));
    }

}
