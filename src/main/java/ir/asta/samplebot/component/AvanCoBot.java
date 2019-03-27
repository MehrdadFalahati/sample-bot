package ir.asta.samplebot.component;

import ir.asta.samplebot.entities.user.UserEntity;
import ir.asta.samplebot.service.userservice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Calendar;

@Component
@Slf4j
public class AvanCoBot extends TelegramLongPollingBot {
    private static final String UNKNOWN_COMMAND = "Unknown command";
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (text.equals("/daysofyear")) {
                String textMessageDaysOfYear = "A few days of the year: " + calculateDaysOfYear();
                SendMessage sendMessage = new SendMessage()
                        .setChatId(chatId)
                        .setText(textMessageDaysOfYear);
                try {
                    execute(sendMessage);
                    log.info("Sent message \"{}\" to {}", textMessageDaysOfYear, chatId);
                } catch (TelegramApiException e) {
                    log.error("Failed to send message \"{}\" to {} due to error: {}", textMessageDaysOfYear, chatId, e.getMessage());
                }
            } else if (text.equals("/start")) {
                UserEntity user = UserEntity.builder()
                        .firstName(update.getMessage().getFrom().getFirstName())
                        .lastName(update.getMessage().getFrom().getLastName())
                        .username(update.getMessage().getFrom().getUserName())
                        .isBot(update.getMessage().getFrom().getBot())
                        .languageCode(update.getMessage().getFrom().getLanguageCode())
                        .build();
                user.setId(update.getMessage().getFrom().getId());
                userService.saveOrUpdate(user);
                String textMessageUserSave = "save user by id:" + update.getMessage().getFrom().getId()
                        + "and firstName is:" + update.getMessage().getFrom().getFirstName();
                SendMessage sendMessage = new SendMessage()
                        .setChatId(chatId)
                        .setText(textMessageUserSave);
                try {
                    execute(sendMessage);
                    log.info("Sent message \"{}\" to {}", textMessageUserSave, chatId);
                } catch (TelegramApiException e) {
                    log.error("Failed to send message \"{}\" to {} due to error: {}", textMessageUserSave, chatId, e.getMessage());
                }

            } else if (text.equals("/stop")) {

            } else {
                // Unknown command
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chatId)
                        .setText(UNKNOWN_COMMAND);
                try {
                    execute(message); // Sending our message object to user
                    log.info("Sent message \"{}\" to {}", UNKNOWN_COMMAND, chatId);
                } catch (TelegramApiException e) {
                    log.error("Failed to send message \"{}\" to {} due to error: {}", UNKNOWN_COMMAND, chatId, e.getMessage());
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

    private int calculateDaysOfYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
}
