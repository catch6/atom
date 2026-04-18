/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.UUIDv7;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UUIDv7Test {

    @Test
    void uuidVersionIs7() {
        UUID uuid = UUIDv7.uuid();
        assertThat(uuid.version()).isEqualTo(7);
    }

    @Test
    void uuidVariantIs2ForRfc4122() {
        UUID uuid = UUIDv7.uuid();
        assertThat(uuid.variant()).isEqualTo(2);
    }

    @Test
    void uuidStrMatchesStandardUuidFormat() {
        String str = UUIDv7.uuidStr();
        assertThat(str).matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-7[0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
    }

    @Test
    void timestampIsCloseToSystemTime() {
        long before = System.currentTimeMillis();
        UUID uuid = UUIDv7.uuid();
        long after = System.currentTimeMillis();
        long ts = UUIDv7.timestamp(uuid);
        assertThat(ts).isBetween(before, after);
    }

    @Test
    void timestampFromStringMatchesTimestampFromUuid() {
        UUID uuid = UUIDv7.uuid();
        assertThat(UUIDv7.timestamp(uuid.toString())).isEqualTo(UUIDv7.timestamp(uuid));
    }

    @Test
    void multipleUuidsAreUnique() {
        Set<UUID> set = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            set.add(UUIDv7.uuid());
        }
        assertThat(set).hasSize(1000);
    }

}
