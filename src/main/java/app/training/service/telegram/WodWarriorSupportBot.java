package app.training.service.telegram;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class WodWarriorSupportBot extends TelegramLongPollingBot {
    private static final String BOT_USERNAME = "WODWarriorSupportService_bot";
    private static final String BOT_TOKEN = "6423698407:AAGJgzLgam7ikM_J5orJy8fw4XD58YHYyB0";
    private long adminChatId = 420725930;
    private boolean isBotRunning;
    private final Map<Long, Long> userToAdminMap = new HashMap<>();

    public WodWarriorSupportBot() {
        isBotRunning = false;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long userChatId = update.getMessage().getChatId();
            String userMessage = update.getMessage().getText();
            String userName = update.getMessage().getFrom().getUserName();

            if (userChatId.equals(adminChatId) && userMessage.startsWith("/reply")) {
                handleAdminResponse(userMessage);
            } else if (userMessage.equals("/stop")) {
                stopBot(userChatId);
                sendResponse(userChatId, "Bot has been stopped.");
            } else if (!isBotRunning && userMessage.equals("/start")) {
                startBot(userChatId);
                sendResponse(userChatId, "Welcome to the WODWarrior Support Bot!"
                        + "\nHow can we help you?\n"
                        + "Send your message and our customer service will answer on it!");

            } else {
                forwardMessageToAdmin(userChatId, userMessage, userName);
            }
        }
    }

    private void startBot(Long userChatId) {
        isBotRunning = true;
        sendResponse(userChatId, "Bot started.");
    }

    private void stopBot(Long userChatId) {
        isBotRunning = false;
        sendResponse(userChatId, "Bot stopped. Use /start to restart.");
    }

    private void forwardMessageToAdmin(Long userChatId, String userMessage, String userName) {
        String messageToAdmin = userName + " userId " + userChatId + " says: " + userMessage;
        sendResponse(adminChatId, messageToAdmin);
        userToAdminMap.put(userChatId, adminChatId);
    }

    private void handleAdminResponse(String adminMessage) {
        String[] parts = adminMessage.split(" ", 3);
        if (parts.length < 3) {
            sendResponse(adminChatId, "Invalid format. Use: /reply <userChatId> <message>");
            return;
        }

        Long userChatId;
        try {
            userChatId = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            sendResponse(adminChatId, "Invalid user chat ID.");
            return;
        }

        String responseMessage = parts[2];
        sendResponse(userChatId, "Customer service: " + responseMessage);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private void sendResponse(Long chatId, String message) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(message);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            throw new RuntimeException("Telegram notification failed: " + ex.getMessage());
        }
    }
}
