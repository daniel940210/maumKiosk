package com.mindslab.toronto.maumKiosk.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Slack_Service {
    public void messageBuilder(String userName, String text) {
        SlackMessage slackMessage = SlackMessage.builder()
                                        .username(userName)
                                        .text("Notification: A new visitor arrived." + "Name: " + userName + ", Message: " + text)
                                        .icon_emoji(":twice:")
                                        .build();
        SlackUtils.sendMessage(slackMessage);
    }
    public void knownUser(String userName) {
        SlackMessage slackMessage = SlackMessage.builder()
                                        .username(userName)
                                        .text("Notification: " + userName + " has arrived.")
                                        .icon_emoji(":twice:")
                                        .build();
        SlackUtils.sendMessage(slackMessage);
    }
}
