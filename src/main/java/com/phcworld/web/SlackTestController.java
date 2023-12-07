package com.phcworld.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phcworld.utils.SlackErrorMessage;
import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SlackTestController {
    @Value("${notification.slack.webhook.url}")
    private String slackAlertWebhookUrl;

    private final ObjectMapper objectMapper;

    private final Slack slack;

    @GetMapping("/hello-error-slack-client")
    public String testSlack() throws IOException {
//        Slack slack = Slack.getInstance();
        String errorMessage = "어딘가에서 에러가 발생했습니다.";
        SlackErrorMessage message = new SlackErrorMessage(errorMessage);
        WebhookResponse response = slack.send(slackAlertWebhookUrl, objectMapper.writeValueAsString(message));
        return "Success " + response.getCode();
    }
}
