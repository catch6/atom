package cn.mindit.atom.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Catch
 * @since 2024-10-28
 */
@Slf4j
@MappedJdbcTypes({JdbcType.TIMESTAMP, JdbcType.TIMESTAMP_WITH_TIMEZONE})
@MappedTypes({LocalDateTime.class, Timestamp.class})
public class AtomLocalDateTimeHandler extends LocalDateTimeTypeHandler {

}
