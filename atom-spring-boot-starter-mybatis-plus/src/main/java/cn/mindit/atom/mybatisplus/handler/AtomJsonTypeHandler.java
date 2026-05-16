package cn.mindit.atom.mybatisplus.handler;

import cn.mindit.atom.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * JSON/JSONB ↔ Java 对象 TypeHandler，基于 {@link JsonUtils}。
 * <p>用法：在字段上声明子类作为 typeHandler，或继承本类指定目标类型。</p>
 * <pre>
 * &#64;TableField(typeHandler = AtomJsonTypeHandler.class)
 * private MyDto info;
 * </pre>
 * <p>PostgreSQL JSONB 列使用 {@code setObject(i, json, Types.OTHER)} 写入。</p>
 *
 * @param <T> 目标 Java 类型
 * @author Catch
 * @since 2026-05-16
 */
@Slf4j
public class AtomJsonTypeHandler<T> extends BaseTypeHandler<T> {

    private final Class<T> type;

    /**
     * 无参构造仅为兼容 MyBatis package 扫描注册流程，反序列化将无法定位目标类型。
     */
    public AtomJsonTypeHandler() {
        this.type = null;
    }

    public AtomJsonTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, JsonUtils.toJson(parameter), Types.OTHER);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private T parse(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        if (type == null) {
            throw new IllegalStateException("AtomJsonTypeHandler 未指定目标类型，请通过子类或带 Class 构造器使用");
        }
        return JsonUtils.toObject(json, type);
    }

}
