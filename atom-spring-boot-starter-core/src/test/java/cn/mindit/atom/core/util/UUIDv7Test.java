package cn.mindit.atom.core.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UUIDv7Test {

    @Test
    void uuid_shouldHaveVersion7_andVariant2_andBeParsable() {
        UUID uuid = UUIDv7.uuid();
        assertNotNull(uuid);

        log.info("uuid: {}", uuid);

        assertEquals(7, uuid.version());
        assertEquals(2, uuid.variant());

        assertDoesNotThrow(() -> UUID.fromString(uuid.toString()));
    }

    @Test
    void uuidStr_shouldBeValidUuidString_andTimestampExtractionShouldWork() {
        long before = System.currentTimeMillis();
        String uuidStr = UUIDv7.uuidStr();
        long after = System.currentTimeMillis();

        assertNotNull(uuidStr);

        UUID uuid = assertDoesNotThrow(() -> UUID.fromString(uuidStr));
        assertEquals(7, uuid.version());
        assertEquals(2, uuid.variant());

        long tsFromUuid = UUIDv7.timestamp(uuid);
        long tsFromStr = UUIDv7.timestamp(uuidStr);
        log.info("tsFromUuid: {}, tsFromStr: {}", tsFromUuid, tsFromStr);
        assertEquals(tsFromUuid, tsFromStr);

        // timestamp 来自 System.currentTimeMillis() 的 48 位截断值。
        // 这里做一个宽松区间断言，避免 CI/调度导致的 flaky。
        assertTrue(tsFromUuid >= before && tsFromUuid <= after,
            "timestamp should be within [before, after] but was " + tsFromUuid + ", before=" + before + ", after=" + after);
    }

    @Test
    void timestamp_shouldEqualMostSignificantBitsShift16() {
        UUID uuid = UUIDv7.uuid();
        long expected = uuid.getMostSignificantBits() >>> 16;
        assertEquals(expected, UUIDv7.timestamp(uuid));
    }

    @Test
    void uuid_shouldBeUnique_underConcurrency() throws Exception {
        int threads = Math.max(4, Runtime.getRuntime().availableProcessors());
        int perThread = 5_000;
        int total = threads * perThread;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        Set<UUID> set = ConcurrentHashMap.newKeySet(total);

        for (int t = 0; t < threads; t++) {
            pool.execute(() -> {
                try {
                    start.await();
                    for (int i = 0; i < perThread; i++) {
                        set.add(UUIDv7.uuid());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS), "generation did not finish in time");

        pool.shutdown();
        assertTrue(pool.awaitTermination(30, TimeUnit.SECONDS), "thread pool did not terminate in time");

        assertEquals(total, set.size(), "UUIDv7 should be unique across concurrent generations");
    }

    @Test
    void uuid_shouldBeFastEnough_onAverage() {
        int iterations = 10000;
        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            UUIDv7.uuid();
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double averageTime = (double) duration / iterations;

        // 验证平均生成时间在合理范围内（应该小于1毫秒）
        assertTrue(averageTime < 1_000_000, "平均生成时间应该小于1毫秒: " + averageTime + " 纳秒");

        System.out.println("平均生成时间: " + averageTime + " 纳秒");
    }

    @Test
    void uuid_shouldGenerateSomeAmountWithinOneSecond() {
        long durationNanos = TimeUnit.SECONDS.toNanos(1);
        long start = System.nanoTime();
        long deadline = start + durationNanos;

        long count = 0;
        while (System.nanoTime() < deadline) {
            UUIDv7.uuid();
            count++;
        }

        System.out.println("1秒生成个数: " + count);

        // 非常宽松的下限，主要用于防止测试被意外写成极慢/阻塞逻辑；避免对不同机器性能造成 flaky。
        assertTrue(count > 10_000, "1秒生成个数应该大于10000，但实际为: " + count);
    }

}
