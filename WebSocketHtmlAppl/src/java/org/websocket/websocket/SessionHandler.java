/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.websocket.websocket;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import javax.websocket.Session;
import javax.json.JsonObject;
import org.websocket.model.Item;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.spi.JsonProvider;

/**
 *
 * @author girinathr
 */
@ApplicationScoped
public class SessionHandler {
    
    private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Item> devices = new HashSet<>();
    
    public void addSession(Session session) {
        sessions.add(session);
        for (Item device : devices) {
            JsonObject addMessage = createAddMessage(device);
            sendToSession(session, addMessage);
        }

    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
     public List<Item> getDevices() {
        return new ArrayList<>(devices);
    }

    public void addDevice(Item device) {
        device.setId(deviceId);
        devices.add(device);
        deviceId++;
        JsonObject addMessage = createAddMessage(device);
        sendToAllConnectedSessions(addMessage);
    }

    public void removeDevice(int id) {
        Item device = getDeviceById(id);
        if (device != null) {
            devices.remove(device);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }

    public void toggleDevice(int id) {
        JsonProvider provider = JsonProvider.provider();
        Item device = getDeviceById(id);
        if (device != null) {
            if ("On".equals(device.getStatus())) {
                device.setStatus("Off");
            } else {
                device.setStatus("On");
            }
            JsonObject updateDevMessage = provider.createObjectBuilder()
                    .add("action", "toggle")
                    .add("id", device.getId())
                    .add("status", device.getStatus())
                    .build();
            sendToAllConnectedSessions(updateDevMessage);
        }
    }

    private Item getDeviceById(int id) {
        for (Item device : devices) {
            if (device.getId() == id) {
                return device;
            }
        }
        return null;
    }

    private JsonObject createAddMessage(Item device) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", device.getId())
                .add("name", device.getName())
                .add("type", device.getType())
                .add("status", device.getStatus())
                .add("description", device.getDescription())
                .build();
        return addMessage;
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (Exception ex) {
            sessions.remove(session);
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
