package cn.mindit.atom.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PostgreSQL {@code int8[]} ↔ {@code List<Long>} TypeHandler。
 * <p>用法：</p>
 * <pre>
 * &#64;TableField(typeHandler = AtomLongListArrayHandler.class)
 * private List&lt;Long&gt; ids;
 * </pre>
 *
 * @author Catch
 * @since 2026-05-16
 */
@Slf4j
public class AtomLongListArrayHandler extends BaseTypeHandler<List<Long>> {

    private static final String PG_TYPE = "int8";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        Array array = ps.getConnection().createArrayOf(PG_TYPE, parameter.toArray(new Long[0]));
        ps.setArray(i, array);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toList(rs.getArray(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toList(rs.getArray(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toList(cs.getArray(columnIndex));
    }

    private List<Long> toList(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        Object raw = array.getArray();
        if (!(raw instanceof Object[] arr)) {
            return null;
        }
        List<Long> list = new ArrayList<>(arr.length);
        for (Object o : arr) {
            if (o == null) {
                list.add(null);
            } else if (o instanceof Number n) {
                list.add(n.longValue());
            } else {
                list.add(Long.valueOf(o.toString()));
            }
        }
        return list;
    }

}
