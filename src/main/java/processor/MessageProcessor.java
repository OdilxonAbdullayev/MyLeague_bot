package processor;

import bot.CallBackDataValue;
import bot.MessageBuilder;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import user.User;
import user.UserRole;
import user.UserState;

import java.util.Arrays;
import java.util.List;

public class MessageProcessor extends AbstractProcessor {
    private static String firstName;

    @Override
    public boolean supports(Update update) {
        return update.hasMessage();
    }

    @SneakyThrows
    @Override
    public void process(Update update, DefaultAbsSender sender) {
        SetMyCommands setCommands = new SetMyCommands();
        setCommands.setCommands(Arrays.asList(
                new BotCommand("/start", "Start the bot")
//                new BotCommand("/help", "Get help")
        ));

        sender.execute(setCommands);

        User user = userService.getOrElseCreate(update.getMessage().getChatId().toString(), update.getMessage().getFrom().getFirstName());

        // USER PANEL
        if (user.getRole() == UserRole.USER) {
            switch (user.getState()) {
                case START -> {
                    if (update.getMessage().hasContact()) {
                        userService.editPhoneNumber(user, update.getMessage().getContact().getPhoneNumber());
                        SendMessage sendMessage = MessageBuilder.sendPhoneNumberSuccess(user);
                        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                        sender.execute(sendMessage);
                        sender.execute(MessageBuilder.getHome(user));
                        user.setState(UserState.HOME);
                        return;
                    }
                    sender.execute(MessageBuilder.sendPhoneNumber(user));
                }

                case HOME -> {
                    sender.execute(MessageBuilder.getHome(user));
                    if (update.getMessage().hasText() && update.getMessage().getText().equals("key_get_all_user_info")) {
                        sender.execute(new SendMessage(user.getId(), userService.getAllUser().toString() + "\n"));
                    }
                }

                case SEND_MESSAGE -> {
                    if (update.getMessage().hasText() && update.getMessage().getText().equals("/skip")) {
                        user.setState(UserState.HOME);
                        sender.execute(MessageBuilder.getHome(user));
                        return;
                    }
//                    Message message = update.getMessage();
//                    ForwardMessage forwardMessage = MessageBuilder.getSendMessageToTeacher(user, teacherId, message);
//                    sender.execute(forwardMessage);
                    String teacherId = userService.getTeacherId();

                    if (update.getMessage().hasText()) {
                        sender.execute(MessageBuilder.sendMessageToTeacher(user, teacherId, update.getMessage().getText()));
                        sender.execute(MessageBuilder.completed(user));
                    } else if (update.getMessage().hasPhoto()) {
                        sender.execute(MessageBuilder.sendPhotoToTeacher(user, teacherId, update.getMessage().getPhoto()));
                        sender.execute(MessageBuilder.completed(user));
                    } else if (update.getMessage().hasAudio()) {
                        sender.execute(MessageBuilder.sendAudioToTeacher(user, teacherId, update.getMessage().getAudio()));
                        sender.execute(MessageBuilder.completed(user));
                    } else if (update.getMessage().hasVideo()) {
                        sender.execute(MessageBuilder.sendVideoToTeacher(user, teacherId, update.getMessage().getVideo()));
                        sender.execute(MessageBuilder.completed(user));
                    } else if (update.getMessage().hasVideoNote()) {
                        Message executeVideoNote = sender.execute(MessageBuilder.sendVideoNoteToTeacher(teacherId, update.getMessage().getVideoNote()));
                        sender.execute(MessageBuilder.sendVideoNoteToReplyToTeacher(user, teacherId, executeVideoNote));
                        sender.execute(MessageBuilder.completed(user));
                    } else if (update.getMessage().hasDocument()) {
                        sender.execute(MessageBuilder.sendDocumentToTeacher(user, teacherId, update.getMessage().getDocument()));
                        sender.execute(MessageBuilder.completed(user));
                    } else if (update.getMessage().hasVoice()) {
                        sender.execute(MessageBuilder.sendVoiceTeacher(user, teacherId, update.getMessage().getVoice()));
                        sender.execute(MessageBuilder.completed(user));
                    } else {
                        sender.execute(MessageBuilder.sendWrongMessageToTeacher(user.getId()));
                    }
                    sender.execute(MessageBuilder.getHome(user));
                    user.setState(UserState.HOME);
                }
                case COUNT_WORD -> {
                    if (update.getMessage().hasText()) {
                        String messageText = update.getMessage().getText();
                        int wordCount = countWords(messageText);

                        String response = "Siz yuborgan textda <b>" + wordCount + " ta </b>so‘z bor.";
                        SendMessage sendMessage = new SendMessage(user.getId(), response);
                        sendMessage.setParseMode(ParseMode.HTML);
                        sender.execute(sendMessage);
                        sender.execute(MessageBuilder.getHome(user));
                    } else {
                        sender.execute(new SendMessage(user.getId(), "Text formatda habar yuboring!"));
                        sender.execute(MessageBuilder.getHome(user));
                    }
                    user.setState(UserState.HOME);
                }
                case EDIT_USERNAME -> {
                    if (!update.getMessage().hasText()) {
                        Message execute = sender.execute(MessageBuilder.errorUsernameType(user));
                        Thread.sleep(2000);
                        sender.execute(MessageBuilder.errorUsernameTypeEditMessage(user, execute));
                        user.setState(UserState.EDIT_USERNAME);
                        break;
                    }
                    String text = update.getMessage().getText();
                    userService.editUsername(user, text);
                    sender.execute(MessageBuilder.editUsername(user.getId()));
                    sender.execute(MessageBuilder.getHome(user));
                    user.setState(UserState.HOME);
                }
                case FEEDBACK -> {
                    if (update.getMessage().hasText()) {
                        sender.execute(MessageBuilder.getFeedback(user));
                        sender.execute(MessageBuilder.sendFeedbackToMe(user, update.getMessage().getText()));
                        sender.execute(MessageBuilder.getHome(user));
                        user.setState(UserState.HOME);
                    } else {
                        sender.execute(MessageBuilder.errorSendFeedback(user));
                    }
                }
            }
        } else {
            // ADMIN PANEL
            switch (user.getState()) {
                case HOME -> {
                    sender.execute(MessageBuilder.getHomeForTeacher(user));
                }
                case SEND_MESSAGE -> {
                    if (update.getMessage().hasText()) {
                        if (update.getMessage().getText().equals(CallBackDataValue.SEND_MESSAGE_TO_ALL_STUDENTS)) {
                            sender.execute(MessageBuilder.sendMessage(user));
                            user.setState(UserState.SEND_MESSAGE_ALL_STUDENT);
                            break;
                        }
                    }

                    firstName = update.getMessage().getText();
                    User sendUser = userService.findByFirstName(firstName);
                    if (sendUser == null) {
                        Message executeMessage = sender.execute(new SendMessage(user.getId(), "Noto'gri o'quvchini tanladingiz! Iltimos qaytadan urinib ko'ring\uD83D\uDD04"));
                        Thread.sleep(1500);
                        sender.execute(MessageBuilder.getHomeForTeacherEditMessage(user, executeMessage));
                        user.setState(UserState.HOME);
                    } else {
                        sender.execute(MessageBuilder.sendMessage(user));
                        user.setState(UserState.COMPLETED);
                    }
                }
                case COMPLETED -> {
                    if (update.getMessage().hasText()) {
                        if (update.getMessage().getText().equals("/skip")) {
                            user.setState(UserState.HOME);
                            sender.execute(MessageBuilder.getHomeForTeacher(user));
                            return;
                        }
                        User user1 = userService.findByFirstName(firstName);

                        sender.execute(MessageBuilder.endMethod(user1.getId(), update.getMessage().getText()));
                        sender.execute(MessageBuilder.sendMessageSuccessfully(user, user1));
                        sender.execute(MessageBuilder.getHomeForTeacher(user));
                        user.setState(UserState.HOME);
                    } else {
                        Message execute = sender.execute(MessageBuilder.errorSendMessageToUser(user));
                        Thread.sleep(2000);
                        sender.execute(MessageBuilder.sendMessageEditMessage(user, execute));
                        user.setState(UserState.COMPLETED);
                    }
                }
                case SEND_MESSAGE_ALL_STUDENT -> {
                    if (update.getMessage().hasText()) {
                        String text = update.getMessage().getText();
                        if (text.equals("/skip")) {
                            user.setState(UserState.HOME);
                            sender.execute(MessageBuilder.getHomeForTeacher(user));
                            return;
                        }
                        sendMessageToAllStudents(sender, text, userService.getAllUser());
                        sender.execute(MessageBuilder.sendMessageToAllStudentSuccessfully(user.getId()));
                        sender.execute(MessageBuilder.getHomeForTeacher(user));
                        user.setState(UserState.HOME);
                    } else {
                        Message execute = sender.execute(MessageBuilder.errorSendMessageToUser(user));
                        Thread.sleep(2000);
                        sender.execute(MessageBuilder.sendMessageEditMessage(user, execute));
                        user.setState(UserState.SEND_MESSAGE_ALL_STUDENT);
                    }
                }
                case FEEDBACK -> {
                    if (update.getMessage().hasText()) {
                        sender.execute(MessageBuilder.getFeedback(user));
                        sender.execute(MessageBuilder.sendFeedbackToMe(user, update.getMessage().getText()));
                        sender.execute(MessageBuilder.getHomeForTeacher(user));
                        user.setState(UserState.HOME);
                    } else {
                        sender.execute(MessageBuilder.errorSendFeedback(user));
                    }
                }

            }
        }
    }

    private void sendMessageToAllStudents(DefaultAbsSender sender, String text, List<User> allUser) {
        for (int i = 0; i < allUser.size(); i++) {
            if (allUser.get(i).getRole() == UserRole.ADMIN) {
                continue;
            }
            try {
                sender.execute(MessageBuilder.endMethod(allUser.get(i).getId(), text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\s+");
        return words.length;
    }

}
