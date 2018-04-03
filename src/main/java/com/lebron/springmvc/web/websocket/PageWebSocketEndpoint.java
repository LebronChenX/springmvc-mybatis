package com.lebron.springmvc.web.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;


@ServerEndpoint(value = "/pagewebsocket", configurator = SpringConfigurator.class)
@Component
public class PageWebSocketEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(PageWebSocketEndpoint.class);

    private Session session; // this websocket session for send and receive message
    private String linkname; // page request timestamp
    private String pid; // the connection's rid

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        pid = ((PrincipalWithParam) session.getUserPrincipal()).getParam().get("pid");
        linkname = ((PrincipalWithParam) session.getUserPrincipal()).getParam().get("identifyId");
        if (StringUtils.isEmpty(pid) || StringUtils.isEmpty(linkname)) {
            try {
                this.session.close(new CloseReason(CloseCodes.CLOSED_ABNORMALLY, "Unvalid parameters"));
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("[Receiver message][" + linkname + "]------------------------------  {}: ", message);
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        logger.info("[Disconnect][" + linkname + "]------------------------------  status: " + closeReason.getCloseCode().getCode() + " closeReason:" + closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("Close connection", error);
    }

    private void sendMessage(String msg) {
        try {
            if (session != null && this.session.isOpen()) {
                this.session.getAsyncRemote().sendText(msg);
            } else {
                logger.error("websocket session is closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), e);
        }
    }

}
