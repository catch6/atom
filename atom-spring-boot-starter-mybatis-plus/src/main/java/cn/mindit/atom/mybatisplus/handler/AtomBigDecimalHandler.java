package cn.mindit.atom.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BigDecimalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;

/**
 * @author Catch
 * @since 2024-10-28
 */
@Slf4j
@MappedJdbcTypes({JdbcType.NUMERIC, JdbcType.DECIMAL})
@MappedTypes({BigDecimal.class})
public class AtomBigDecimalHandler extends BigDecimalTypeHandler {

}
