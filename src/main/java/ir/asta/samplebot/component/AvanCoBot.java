package ir.asta.samplebot.component;

import ir.asta.samplebot.entities.user.UserEntity;
import ir.asta.samplebot.service.userservice.UserService;
import ir.asta.samplebot.util.date.JalaliCalendar;
import ir.asta.samplebot.util.locale.LocaleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.toIntExact;

@Component
@Slf4j
public class AvanCoBot extends TelegramLongPollingBot {
    private static final String UNKNOWN_COMMAND = "Unknown command";
    private static final String SENT_MESSAGE_TO = "Sent message \"{}\" to {}";
    private static final String FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR = "Failed to send message \"{}\" to {} due to error: {}";
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
            switch(text)
            {
                case "/daysofyear" :
                    UserEntity user = userService.findByTelegramId(update.getMessage().getFrom().getId());
                    if (user.getCurrentDays() > calculateDaysOfYear()) {
                        continuePage(update, chatId);
                    } else {
                        createMessageDyasOfYaer(chatId);
                    }
                    break;
                case "/start" :
                    addUser(update, chatId);
                    break;
                case "/stop" :
                    findUser(update, chatId);
                    break;
                case "/continue" :
                    continuePage(update, chatId);
                    break;
                default :
                    unkownCommand(chatId, UNKNOWN_COMMAND);
            }
        } else if (update.hasCallbackQuery()) {
            callBack(update);
        }
    }

    private void callBack(Update update) {
        String callData = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (callData.equals("update_read_text")) {
            int nextPage = 2 * calculateDaysOfYear();
            int currentPag = nextPage - 1;
            UserEntity user = userService.findByTelegramId(toIntExact(update.getCallbackQuery().getFrom().getId()));
            if (user.getCurrentDays() != null && user.getCurrentDays() == calculateDaysOfYear()) {
                EditMessageText newMessage = new EditMessageText()
                        .setChatId(chatId)
                        .setMessageId(toIntExact(messageId))
                        .setText(LocaleUtil.getText("common_old_read_text", currentPag, nextPage));
                try {
                    execute(newMessage);
                    log.info(SENT_MESSAGE_TO, newMessage, chatId);
                } catch (TelegramApiException e) {
                    log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, newMessage, chatId, e.getMessage());
                }
            } else {
                user.setCurrentDays(calculateDaysOfYear());
                userService.saveOrUpdate(user);
                String answer = LocaleUtil.getText("common_read_text", currentPag, nextPage);
                EditMessageText newMessage = new EditMessageText()
                        .setChatId(chatId)
                        .setMessageId(toIntExact(messageId))
                        .setText(answer);
                try {
                    execute(newMessage);
                    log.info(SENT_MESSAGE_TO, newMessage, chatId);
                } catch (TelegramApiException e) {
                    log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, newMessage, chatId, e.getMessage());
                }
            }

        } else if (callData.equals("update_read_continue_text")) {
            UserEntity user = userService.findByTelegramId(toIntExact(update.getCallbackQuery().getFrom().getId()));
            int nextPage = 2 * user.getCurrentDays() + 1;
            int currentPag = nextPage - 1;
            user.setCurrentDays(user.getCurrentDays() + 1);
            userService.saveOrUpdate(user);
            String answer = LocaleUtil.getText("common_read_text", currentPag, nextPage);
            EditMessageText newMessage = new EditMessageText()
                    .setChatId(chatId)
                    .setMessageId(toIntExact(messageId))
                    .setText(answer);
            try {
                execute(newMessage);
                log.info(SENT_MESSAGE_TO, newMessage, chatId);
            } catch (TelegramApiException e) {
                log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, newMessage, chatId, e.getMessage());
            }
        }
    }

    private void createMessageDyasOfYaer(Long chatId) {
        StringBuilder buffer = new StringBuilder();
        int nextPage = 2 * calculateDaysOfYear();
        int currentPag = nextPage - 1;
        buffer.append(LocaleUtil.getText("common_a_few_days_of_the_year", calculateDaysOfYear(), currentPag, nextPage));
        buffer.append("\n\n");
        buffer.append(LocaleUtil.getText("common_link_tanzil", "http://tanzil.net/?locale=fa_IR#"+ currentPag +":1" , "http://tanzil.net/?locale=fa_IR#"+ nextPage +":1"));
        daysOfYaer(chatId, LocaleUtil.getText(buffer.toString()));
    }

    private void continuePage(Update update, Long chatId) {
        UserEntity user = userService.findByTelegramId(update.getMessage().getFrom().getId());
        if (user != null && user.getCurrentDays() != null) {
            StringBuilder buffer = new StringBuilder();
            int nextPage = 2 * user.getCurrentDays() + 1;
            int currentPag = nextPage - 1;
            buffer.append(LocaleUtil.getText("common_read_continue_end_quarn", user.getCurrentDays(), currentPag, nextPage));
            buffer.append("\n\n");
            buffer.append(LocaleUtil.getText("common_link_tanzil", "http://tanzil.net/?locale=fa_IR#"+ currentPag +":1" , "http://tanzil.net/?locale=fa_IR#"+ nextPage +":1"));
            if (user.getCurrentDays() > calculateDaysOfYear()) {
                buffer.append("\n\n");
                int days = user.getCurrentDays() - calculateDaysOfYear();
                buffer.append(LocaleUtil.getText("common_read_additional", days));
            } else {
                buffer.append("\n\n");
                int days = calculateDaysOfYear() - user.getCurrentDays();
                buffer.append(LocaleUtil.getText("common_read_beforable", days));
            }
            SendMessage sendMessage = new SendMessage()
                    .setChatId(chatId)
                    .setText(buffer.toString());
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText(LocaleUtil.getText("common_read")).setCallbackData("update_read_continue_text"));
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
            // Add it to the message
            markupInline.setKeyboard(rowsInline);
            sendMessage.setReplyMarkup(markupInline);
            try {
                execute(sendMessage);
                log.info(SENT_MESSAGE_TO, buffer.toString(), chatId);
            } catch (TelegramApiException e) {
                log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, buffer.toString(), chatId, e.getMessage());
            }
        } else {
            unkownCommand(chatId, LocaleUtil.getText("common_no_read"));
        }
    }

    private void findUser(Update update, Long chatId) {
        UserEntity user = userService.findByTelegramId(update.getMessage().getFrom().getId());
        if (user != null) {
            String username = user.getUsername() != null ? user.getUsername() : LocaleUtil.getText("common_no_username");
            String textMessageFindUser = "find user by id: " + user.getId()
                    + ", telegram id  is: " + user.getTelegramId()
                    + ", firstName is: " + user.getFirstName()
                    + " and username is: " + username;
            SendMessage sendMessage = new SendMessage()
                    .setChatId(chatId)
                    .setText(textMessageFindUser);
            try {
                execute(sendMessage);
                log.info(SENT_MESSAGE_TO, textMessageFindUser, chatId);
            } catch (TelegramApiException e) {
                log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, textMessageFindUser, chatId, e.getMessage());
            }
        } else {
            String textMessageFindUser = "User not found!";
            SendMessage sendMessage = new SendMessage()
                    .setChatId(chatId)
                    .setText(textMessageFindUser);
            try {
                execute(sendMessage);
                log.info(SENT_MESSAGE_TO, textMessageFindUser, chatId);
            } catch (TelegramApiException e) {
                log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, textMessageFindUser, chatId, e.getMessage());
            }
        }
    }

    private void addUser(Update update, Long chatId) {
        UserEntity user = new  UserEntity();
        user.setFirstName(update.getMessage().getFrom().getFirstName());
        user.setLastName(update.getMessage().getFrom().getLastName());
        user.setUsername(update.getMessage().getFrom().getUserName());
        user.setIsBot(update.getMessage().getFrom().getBot());
        user.setLanguageCode(update.getMessage().getFrom().getLanguageCode());
        user.setTelegramId(update.getMessage().getFrom().getId());
        userService.saveOrUpdate(user);
        String textMessageUserSave = LocaleUtil.getText("common_save_user", update.getMessage().getFrom().getFirstName(), "/daysofyear", "/continue");
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(textMessageUserSave);
        try {
            execute(sendMessage);
            log.info(SENT_MESSAGE_TO, textMessageUserSave, chatId);
        } catch (TelegramApiException e) {
            log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, textMessageUserSave, chatId, e.getMessage());
        }
    }

    private void unkownCommand(Long chatId, String textMessage) {
        // Unknown command
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText(textMessage);
        try {
            execute(message); // Sending our message object to user
            log.info(SENT_MESSAGE_TO, textMessage, chatId);
        } catch (TelegramApiException e) {
            log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, textMessage, chatId, e.getMessage());
        }
    }

    private void daysOfYaer(Long chatId, String textMessageDaysOfYear) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(textMessageDaysOfYear);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(LocaleUtil.getText("common_read")).setCallbackData("update_read_text"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);
        try {
            execute(sendMessage);
            log.info(SENT_MESSAGE_TO, textMessageDaysOfYear, chatId);
        } catch (TelegramApiException e) {
            log.error(FAILED_TO_SEND_MESSAGE_TO_DUE_TO_ERROR, textMessageDaysOfYear, chatId, e.getMessage());
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
        JalaliCalendar calendar = new JalaliCalendar();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
}
