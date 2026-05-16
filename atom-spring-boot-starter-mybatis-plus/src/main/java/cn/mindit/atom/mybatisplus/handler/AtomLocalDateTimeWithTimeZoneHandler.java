package cn.mindit.atom.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * TIMESTAMPTZ ↔ LocalDateTime TypeHandler，统一使用 Asia/Shanghai 时区。
 *
 * @author Catch
 * @since 2026-05-16
 */
@Slf4j
@MappedJdbcTypes(JdbcType.TIMESTAMP_WITH_TIMEZONE)
@MappedTypes(LocalDateTime.class)
public class AtomLocalDateTimeWithTimeZoneHandler extends BaseTypeHandler<LocalDateTime> {

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
