package ir.asta.samplebot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AvanCoBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (text.equals("/start")) {
                SendMessage sendMessage = new SendMessage()
                        .setChatId(chatId)
                        .setText(text + "Hi");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "avanco_bot";
    }

    @Override
    public String getBotToken() {
        return "712948461:AAF6ZmQfy8s-Ek44g2HrUbEWrvDXFa0ZxjQ";
    }
}
