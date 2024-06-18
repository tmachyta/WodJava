package app.training;

import app.training.service.telegram.WodWarriorSupportBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TestTrainingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTrainingAppApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new WodWarriorSupportBot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
