package com.lebron.springmvc.web.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.web.socket.server.standard.SpringConfigurator;

public class HttpSessionConfigurator extends SpringConfigurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {

        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        sec.getUserProperties().put(HandshakeRequest.class.getName(), request); // for every request, add
                                                                                // HandshakeRequest to get request
                                                                                // parameters
    }

}
