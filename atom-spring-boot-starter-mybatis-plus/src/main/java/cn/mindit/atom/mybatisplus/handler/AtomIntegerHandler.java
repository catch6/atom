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
