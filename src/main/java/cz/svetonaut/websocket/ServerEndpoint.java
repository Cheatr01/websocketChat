/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.svetonaut.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 *
 * @author sn
 */
@javax.websocket.server.ServerEndpoint(value = "/chat")
public class ServerEndpoint {

    private static Set<Session> sessions = new HashSet<>();

    public static void broadcastMessage(String message) throws IOException {
        for (var s : sessions) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Websocket connection open: " + session.getId());
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Websocket message: " + message);
        try {
//            session.getBasicRemote().sendText(message);
            broadcastMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Websocket error - sessionId: " + session.getId() + ", throwable: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(session.getId() + " session was closed");
        sessions.remove(session);
    }



}
