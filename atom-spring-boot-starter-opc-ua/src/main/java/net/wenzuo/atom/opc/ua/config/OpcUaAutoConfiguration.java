/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.opc.ua.config;

import lombok.RequiredArgsConstructor;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Catch
 * @since 2024-06-23
 */
@RequiredArgsConstructor
@ComponentScan("net.wenzuo.atom.opc.ua")
@EnableConfigurationProperties(OpcUaProperties.class)
@ConditionalOnProperty(value = "atom.opc.ua.enabled", matchIfMissing = true)
public class OpcUaAutoConfiguration implements ApplicationListener<ApplicationStartedEvent> {

	private final OpcUaProperties opcUaProperties;

	@Override
	public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
		ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
		Path securityTempDir;
		if (opcUaProperties.getCertificatePath() == null) {
			securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
		} else {
			securityTempDir = Paths.get(opcUaProperties.getCertificatePath());
		}
		try {
			Files.createDirectories(securityTempDir);
		} catch (IOException e) {
			throw new RuntimeException("unable to create security dir: " + securityTempDir, e);
		}
		for (OpcUaProperties.OpcUaInstance instance : opcUaProperties.getInstances()) {
			if (!instance.getEnabled()) {
				continue;
			}
			IdentityProvider identityProvider;
			if (instance.getUsername() == null) {
				identityProvider = new AnonymousProvider();
			} else {
				identityProvider = new UsernameProvider(instance.getUsername(), instance.getPassword());
			}
			OpcUaClient opcUaClient;
			try {
				opcUaClient = OpcUaClient.create(instance.getUrl(),
					endpoints -> endpoints
						.stream()
						.findFirst(),
					configBuilder -> configBuilder
						.setApplicationName(LocalizedText.english("Atom Opc Ua Client"))
						.setApplicationUri("urn:atom:opc:ua:client")
						.setIdentityProvider(identityProvider)
						.setRequestTimeout(UInteger.valueOf(5000))
						.build()
				);
				opcUaClient.connect().get();
			} catch (Exception e) {
				throw new RuntimeException("connect opc ua server error: " + e.getMessage(), e);
			}
			beanFactory.registerSingleton("opcUaClient-" + instance.getId(), opcUaClient);
		}
	}

}
