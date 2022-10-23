package gruppe_b.quizduell.lobbyserver.config;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final String X_AUTH_TOKEN = "X-Authorization";

    private static final Logger logger = LoggerFactory.getLogger(JwtHandshakeInterceptor.class);

    // @Override
    // public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse
    // response, WebSocketHandler wsHandler,
    // Map<String, Object> attributes) throws Exception {
    // if (request instanceof ServletServerHttpRequest) {
    // ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
    // HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
    // String token = httpServletRequest.getParameter(X_AUTH_TOKEN);
    // if (null == token) {
    // // httpServletRequest.set.setParameter(X_AUTH_TOKEN, "xxxxx");// todo token
    // // value
    // }
    // }
    // return true;
    // }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        String jwtToken = getAuthToken(request);
        logger.info("--> jwt: {}", jwtToken);

        if (true) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            @Nullable Exception exception) {
        // TODO Auto-generated method stub

    }

    String getAuthToken(ServerHttpRequest request) {
        try {
            List<String> authorization = request.getHeaders().get(X_AUTH_TOKEN);
            if (authorization != null && authorization.size() > 0) {
                return authorization.get(0);
            }
            return UriComponentsBuilder.fromHttpRequest(request).build()
                    .getQueryParams().get("token").get(0);
        } catch (NullPointerException e) {
            return null;
        }
    }
}
