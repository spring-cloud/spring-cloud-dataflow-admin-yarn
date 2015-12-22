/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.yarn.streamappmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.yarn.YarnSystemConstants;
import org.springframework.yarn.am.AppmasterTrackService;

/**
 * Yarn application bootstrapping appmaster.
 *
 * @author Janne Valkealahti
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({ DataflowHostInfoDiscoveryProperties.class })
public class StreamAppmasterApplication {

	@Configuration
	public static class Config {

		@Autowired
		private DataflowHostInfoDiscoveryProperties discoveryProperties;

		@Bean(name=YarnSystemConstants.DEFAULT_ID_AMTRACKSERVICE)
		public AppmasterTrackService appmasterTrackService() {
			return new EmbeddedAppmasterTrackService(hostInfoDiscovery());
		}

		@Bean
		public HostInfoDiscovery hostInfoDiscovery() {
			DefaultHostInfoDiscovery discovery = new DefaultHostInfoDiscovery();
			if (StringUtils.hasText(discoveryProperties.getMatchIpv4())) {
				discovery.setMatchIpv4(discoveryProperties.getMatchIpv4());
			}
			if (StringUtils.hasText(discoveryProperties.getMatchInterface())) {
				discovery.setMatchInterface(discoveryProperties.getMatchInterface());
			}
			if (discoveryProperties.getPreferInterface() != null) {
				discovery.setPreferInterface(discoveryProperties.getPreferInterface());
			}
			discovery.setLoopback(discoveryProperties.isLoopback());
			discovery.setPointToPoint(discoveryProperties.isPointToPoint());
			return discovery;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(StreamAppmasterApplication.class, args);
	}

}
