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
 * PostgreSQL {@code text[]} ↔ {@code List<String>} TypeHandler。
 * <p>用法：</p>
 * <pre>
 * &#64;TableField(typeHandler = AtomStringListArrayHandler.class)
 * private List&lt;String&gt; tags;
 * </pre>
 *
 * @author Catch
 * @since 2026-05-16
 */
@Slf4j
public class AtomStringListArrayHandler extends BaseTypeHandler<List<String>> {

    private static final String PG_TYPE = "text";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        Array array = ps.getConnection().createArrayOf(PG_TYPE, parameter.toArray(new String[0]));
        ps.setArray(i, array);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toList(rs.getArray(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toList(rs.getArray(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toList(cs.getArray(columnIndex));
    }

    private List<String> toList(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        Object raw = array.getArray();
        if (!(raw instanceof Object[] arr)) {
            return null;
        }
        List<String> list = new ArrayList<>(arr.length);
        for (Object o : arr) {
            list.add(o == null ? null : o.toString());
        }
        return list;
    }

}
