/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.jwt.service.impl;

import com.nimbusds.jose.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.utils.JsonUtils;
import net.wenzuo.atom.jwt.service.JwtService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final JWSSigner jwsSigner;

    private final JWSVerifier jwsVerifier;

    @Override
    public <T> String sign(T payload) {
        Optional<JWSAlgorithm> jwsAlgorithm = jwsSigner.supportedJWSAlgorithms()
                                                       .stream()
                                                       .findFirst();
        if (jwsAlgorithm.isEmpty()) {
            throw new RuntimeException("JWSAlgorithm is empty");
        }
        JWSHeader jwsHeader = new JWSHeader(jwsAlgorithm.get());
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
                return null;
            }
            if (clazz == String.class) {
                return (T) jwsObject.getPayload()
                                    .toString();
            }
            return JsonUtils.toObject(jwsObject.getPayload()
                                               .toString(), clazz);
        } catch (ParseException | JOSEException e) {
            return null;
        }
    }

}
