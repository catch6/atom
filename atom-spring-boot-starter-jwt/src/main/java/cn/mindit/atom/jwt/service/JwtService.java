package cn.mindit.atom.jwt.service;

/**
 * @author Catch
 * @since 2023-06-06
 */
public interface JwtService {

    <T> String sign(T payload);

    <T> T parse(String signed, Class<T> clazz);

}
