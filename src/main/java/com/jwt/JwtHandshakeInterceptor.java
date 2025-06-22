//package com.jwt;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@Component
//public class JwtHandshakeInterceptor implements HandshakeInterceptor {
//
//	@Autowired
//	private JwtUtil jwtUtil;
//
//	@Override
//	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//			Map<String, Object> attributes) throws Exception {
//
//		if (request instanceof ServletServerHttpRequest servletRequest) {
//			HttpServletRequest httpRequest = servletRequest.getServletRequest();
//			String authHeader = httpRequest.getHeader("Authorization");
//
//			if (authHeader != null && authHeader.startsWith("Bearer ")) {
//				String token = authHeader.substring(7);
//				if (jwtUtil.validateToken(token)) {
//					String username = jwtUtil.extractUsername(token);
//					attributes.put("username", username);
//					return true;
//				}
//			}
//		}
//
//		return false; // Reject the connection if invalid
//	}
//
//	@Override
//	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//			Exception exception) {
//		// No-op
//	}
//
//}
