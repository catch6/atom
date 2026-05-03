package cn.mindit.atom.jwt.service.impl;

import cn.mindit.atom.core.util.JsonUtils;
import cn.mindit.atom.core.util.ServiceException;
import cn.mindit.atom.jwt.service.JwtService;
import com.nimbusds.jose.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JWSSigner jwsSigner;

    private final JWSVerifier jwsVerifier;

    private static final JWSAlgorithm DEFAULT_ALGORITHM = JWSAlgorithm.HS256;

    @Override
    public <T> String sign(T payload) {
        JWSAlgorithm algorithm = jwsSigner.supportedJWSAlgorithms().contains(DEFAULT_ALGORITHM)
            ? DEFAULT_ALGORITHM
            : jwsSigner.supportedJWSAlgorithms().stream().findFirst().orElseThrow(() -> new ServiceException("未知签名算法"));
        JWSHeader jwsHeader = new JWSHeader(algorithm);
        JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(JsonUtils.toJson(payload)));
        try {
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parse(String signed, Class<T> clazz) {
        try {
            JWSObject jwsObject = JWSObject.parse(signed);
            if (!jwsObject.verify(jwsVerifier)) {
                log.debug("JWT verify failed");
                return null;
            }
            if (clazz == String.class) {
                return (T) jwsObject.getPayload().toString();
            }
            return JsonUtils.toObject(jwsObject.getPayload().toString(), clazz);
        } catch (ParseException | JOSEException e) {
            log.debug("JWT parse failed: {}", e.getMessage());
            return null;
        }
    }

}
