package cn.mindit.atom.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * PostgreSQL TIMESTAMPTZ ↔ {@link LocalDateTime} TypeHandler，统一使用 {@code Asia/Shanghai} 时区。
 *
 * <p>pgjdbc 把 {@code timestamptz} 列元数据上报为 {@link java.sql.Types#TIMESTAMP}，
 * 而非 {@code TIMESTAMP_WITH_TIMEZONE}，故不限定 {@code @MappedJdbcTypes}，
 * 由本 handler 接管所有 {@link LocalDateTime} 列的读写以保证时区正确。
 *
 * @author Catch
 * @since 2026-05-16
 */
@Slf4j
@MappedTypes(LocalDateTime.class)
public class AtomLocalDateTimeHandler extends BaseTypeHandler<LocalDateTime> {

    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.atZone(ZONE).toOffsetDateTime());
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toLocalDateTime(rs.getObject(columnName, OffsetDateTime.class));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toLocalDateTime(rs.getObject(columnIndex, OffsetDateTime.class));
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toLocalDateTime(cs.getObject(columnIndex, OffsetDateTime.class));
    }

    private LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.atZoneSameInstant(ZONE).toLocalDateTime();
    }

}
