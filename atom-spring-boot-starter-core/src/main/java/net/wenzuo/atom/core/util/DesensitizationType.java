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

package net.wenzuo.atom.core.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Catch
 * @since 2023-08-25
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum DesensitizationType {

    /**
     * 自定义, 自定义 [start, end) 字符为 *
     */
    CUSTOM,
    /**
     * 只显示第1位,如 李*, 王**
     */
    CHINESE_NAME,
    /**
     * 只显示身份证号前3位和后4位,如 410***********1234
     */
    ID_CARD,
    /**
     * 只显示手机号前3位和后4位,如 138****1234
     */
    MOBILE_PHONE,
    /**
     * 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
     */
    EMAIL,
    /**
     * 密码的全部字符都用*代替，比如：******
     */
    PASSWORD,
    /**
     * 由于银行卡号长度不定，所以只展示前4位，后面的位数根据卡号决定展示1-4位 例如：
     */
    BANK_CARD,
    /**
     * 只显示座机号前4位和后2位,如 0379*****12
     */
    FIXED_PHONE,
    /**
     * 只显示到地区，不显示详细地址，比如：北京市海淀区****
     */
    ADDRESS,
    /**
     * 不对外提供 userId, 统一都为 0L
     */
    USER_ID,
    /**
     * 车牌中间用*代替
     * <p>eg1：null -》 ""
     * <p>eg2："" -》 ""
     * <p>eg3：苏D40000 -》 苏D4***0
     * <p>eg4：陕A12345D -》 陕A1****D
     * <p>eg5：京A123 -》 京A123 如果是错误的车牌，不处理
     */
    CAR_LICENSE,
    /**
     * IPv4脱敏，如：脱敏前：192.168.1.1；脱敏后：192.*.*.*
     */
    IPV4,
    /**
     * IPv6脱敏，如：脱敏前：2001:0db8:86a3:08d3:1319:8a2e:0370:7344；脱敏后：2001:*:*:*:*:*:*:*
     */
    IPV6

}
