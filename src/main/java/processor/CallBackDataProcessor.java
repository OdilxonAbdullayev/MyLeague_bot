package processor;

import bot.MessageBuilder;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import user.User;
import user.UserState;

import java.util.List;

import static bot.CallBackDataValue.*;

public class CallBackDataProcessor extends AbstractProcessor {

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery();
    }

    @SneakyThrows
    @Override
    public void process(Update update, DefaultAbsSender sender) {
        User user = userService.getOrElseCreate(update.getCallbackQuery().getFrom().getId().toString(), update.getCallbackQuery().getFrom().getFirstName());
        String callBackData = update.getCallbackQuery().getData();

        switch (callBackData) {
            case SEND_MESSAGE_TO_TEACHER -> {
                SendMessage sendMessage = MessageBuilder.getSendMessage(user);
                sender.execute(sendMessage);
                user.setState(UserState.SEND_MESSAGE);
            }
            case COUNT_WORD -> {
                SendMessage sendMessage = MessageBuilder.getCountWord(user);
                sender.execute(sendMessage);
                user.setState(UserState.COUNT_WORD);
            }
            case SEND_MESSAGE_TO_STUDENT -> {
                List<User> allUser = userService.getAllUser();
                if (allUser.size() == 1) {
                    SendMessage sendMessage = new SendMessage(user.getId(), "<b> Afsuski, hozir birorta ham o'quvchi mavjud emas❗️ </b>");
                    sendMessage.setParseMode(ParseMode.HTML);
                    Message execute = sender.execute(sendMessage);
                    Thread.sleep(2000);
                    DeleteMessage deleteMessage = MessageBuilder.deleteMessage(user, execute);
                    sender.execute(deleteMessage);

                    user.setState(UserState.HOME);
                    break;
                }
                SendMessage sendMessage = MessageBuilder.getSendMessageToStudent(user, allUser);
                sender.execute(sendMessage);

                user.setState(UserState.SEND_MESSAGE);
            }
            case EDIT_NAME -> {
                SendMessage sendMessage = MessageBuilder.getCurrentName(user);
                sender.execute(sendMessage);
                user.setState(UserState.EDIT_USERNAME);
            }
            case ABOUT_ME -> {
                SendMessage sendMessage = MessageBuilder.aboutMe(user);
                sender.execute(sendMessage);
            }
            case ABOUT_ALL_STUDENTS -> {
                if (userService.getAllUser().size() == 1) {
                    SendMessage sendMessage = new SendMessage(user.getId(), "<b> Afsuski, hozir birorta ham o'quvchi mavjud emas❗️ </b>");
                    sendMessage.setParseMode(ParseMode.HTML);
                    Message execute = sender.execute(sendMessage);
                    Thread.sleep(2000);
                    DeleteMessage deleteMessage = MessageBuilder.deleteMessage(user, execute);
                    sender.execute(deleteMessage);
                    user.setState(UserState.HOME);
                    break;
                }
                SendMessage sendMessage = MessageBuilder.aboutAllStudents(user, userService.getAllUser());
                sender.execute(sendMessage);

                user.setState(UserState.HOME);
            }
            case FEEDBACK -> {
                sender.execute(MessageBuilder.writeFeedback(user));
                user.setState(UserState.FEEDBACK);
            }

        }
    }


}
