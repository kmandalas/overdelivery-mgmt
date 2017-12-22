package com.github.kmandalas.aodm.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry) {
		registry.addEndpoint("/monitor").withSockJS();
	}

	@Override public void configureWebSocketTransport(final WebSocketTransportRegistration webSocketTransportRegistration) {

	}

	@Override public void configureClientInboundChannel(final ChannelRegistration channelRegistration) {

	}

	@Override public void configureClientOutboundChannel(final ChannelRegistration channelRegistration) {

	}

	@Override public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> list) {

	}

	@Override public void addReturnValueHandlers(final List<HandlerMethodReturnValueHandler> list) {

	}

	@Override public boolean configureMessageConverters(final List<MessageConverter> list) {
		return false;
	}

	@Override public void configureMessageBroker(final MessageBrokerRegistry messageBrokerRegistry) {

	}

}
