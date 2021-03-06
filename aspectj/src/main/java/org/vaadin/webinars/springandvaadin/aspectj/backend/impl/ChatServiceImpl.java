/*
 * Copyright 2014 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.webinars.springandvaadin.aspectj.backend.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.vaadin.webinars.springandvaadin.aspectj.backend.ChatMessage;
import org.vaadin.webinars.springandvaadin.aspectj.backend.ChatService;
import org.vaadin.webinars.springandvaadin.aspectj.backend.MessagePostedEvent;
import org.vaadin.webinars.springandvaadin.aspectj.backend.RoomCreatedEvent;

import java.util.*;

/**
 * @author petter@vaadin.com
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    private final Map<String, List<ChatMessage>> messages = new HashMap<>();

    @Override
    public List<String> getRooms() {
        synchronized (messages) {
            return new ArrayList<>(messages.keySet());
        }
    }

    @Override
    public void createRoom(String room) {
        if (room == null || room.isEmpty()) {
            return;
        }
        synchronized (messages) {
            if (messages.containsKey(room)) {
                return;
            }
            messages.put(room, new LinkedList<ChatMessage>());
        }
        eventPublisher.publishEvent(new RoomCreatedEvent(this, room));
    }

    @Override
    public List<ChatMessage> getMessagesInRoom(String room) {
        List<ChatMessage> messageList;
        synchronized (messages) {
            messageList = messages.get(room);
        }
        if (messageList != null) {
            synchronized (messageList) {
                return new ArrayList<>(messageList);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void post(ChatMessage message) {
        List<ChatMessage> messagesList;
        synchronized (messages) {
            messagesList = messages.get(message.getRoom());
        }
        if (messagesList != null) {
            synchronized (messagesList) {
                messagesList.add(message);
            }
            eventPublisher.publishEvent(new MessagePostedEvent(this, message));
        } else {
            throw new IllegalArgumentException("Unknown room: " + message.getRoom());
        }
    }
}
