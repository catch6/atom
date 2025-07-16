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

package cn.mindit.atom.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author Catch
 * @since 2024-10-28
 */
@Slf4j
@MappedJdbcTypes({JdbcType.TINYINT, JdbcType.SMALLINT, JdbcType.INTEGER})
@MappedTypes({Integer.class, Byte.class, Short.class})
public class AtomIntegerHandler extends IntegerTypeHandler {

}
