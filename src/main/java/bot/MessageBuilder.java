package bot;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import user.User;
import user.UserRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageBuilder {
    private static final String my_id = "7306026253";

    public static SendMessage getHome(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "Salom! Men<b> @MyLeague1_bot </b>Sizga yordam berish uchun shu yerdaman. Davom etish uchun kerakli buyruqni tanlang");
        sendMessage.setParseMode(ParseMode.HTML);

        InlineKeyboardMarkup homeInline = InlineButtonBuilder.build(
                List.of(List.of("Ustozga xabar yuborish✍️"), List.of("So'zlarni sanash\uD83D\uDD22"), List.of("Usernameni tahrirlash✏️", "Men haqimdaℹ️"), List.of("Feedback\uD83D\uDCAC")),
                List.of(List.of(CallBackDataValue.SEND_MESSAGE_TO_TEACHER), List.of(CallBackDataValue.COUNT_WORD), List.of(CallBackDataValue.EDIT_NAME, CallBackDataValue.ABOUT_ME), List.of(CallBackDataValue.FEEDBACK)));
        sendMessage.setReplyMarkup(homeInline);

        return sendMessage;
    }

    public static SendMessage getSendMessage(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "Yaxshi. Endi menga habarni yuboring(text, rasm, video, ovozli habar, audio, fayl).\nAgar habarni yubormoqchi bo'lmasangiz<b> /skip </b>ni bosing");
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

//    public static ForwardMessage getSendMessageToTeacher(User user, String teacherId, Message message) {
//        ForwardMessage forwardMessage = new ForwardMessage();
//        forwardMessage.setFromChatId(user.getId());
//        forwardMessage.setChatId(teacherId);
//        forwardMessage.setMessageId(message.getMessageId());
//        return forwardMessage;
//    }


    public static SendMessage completed(User user) {
        return new SendMessage(user.getId(), "Habaringiz ustozga yuborildi✅");
    }

    public static SendMessage getCountWord(User user) {
        return new SendMessage(user.getId(), "Textni yuboring\uD83D\uDCC3");
    }

    public static SendMessage getHomeForTeacher(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "<b> Salom! Men @MyLeague1_bot.</b>");
        sendMessage.setParseMode(ParseMode.HTML);

        InlineKeyboardMarkup homeInline = InlineButtonBuilder.build(
                List.of(List.of("O'quvchilarga habar yuborish✉️"), List.of("O'quvchilar haqida malumot olishℹ️"), List.of("Feedback\uD83D\uDCAC")),
                List.of(List.of(CallBackDataValue.SEND_MESSAGE_TO_STUDENT), List.of(CallBackDataValue.ABOUT_ALL_STUDENTS), List.of(CallBackDataValue.FEEDBACK)));
        sendMessage.setReplyMarkup(homeInline);

        return sendMessage;
    }


    public static SendMessage getSendMessageToStudent(User user, List<User> users) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow(Collections.singleton(new KeyboardButton("Barcha o'quvchilarga habar yuborish")));
        keyboardRows.add(row);
        KeyboardRow currentRow = new KeyboardRow();

        for (User value : users) {
            if (value.getRole() == UserRole.ADMIN) {
                continue;
            }

            KeyboardButton button = new KeyboardButton(value.getName());
            currentRow.add(button);

            if (currentRow.size() == 4) {
                keyboardRows.add(currentRow);
                currentRow = new KeyboardRow();
            }
        }

        if (!currentRow.isEmpty()) {
            keyboardRows.add(currentRow);
        }

        SendMessage sendMessage = new SendMessage(user.getId(), "Yaxshi, endi habar yubormoqchi bo'lgan o'quvchingizni tanlang\uD83D\uDC47");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public static SendMessage sendMessage(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "Habarni yuboring(<b>faqat text shaklida bo'lsin</b>). \nAgar habarni yubormoqchi bo'lmasangiz <b>/skip</b> ni bosing");
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        return sendMessage;
    }

    public static SendMessage endMethod(String userId, String text) {
        SendMessage sendMessage = new SendMessage(userId, "<b>Ustozdan habar: </b>\n" + text);
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }


    public static SendMessage sendMessageSuccessfully(User user, User student) {
        SendMessage sendMessage = new SendMessage(user.getId(), "Habaringiz:<b> %s </b>ga yuborildi✅".formatted(student.getName()));
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static EditMessageText getHomeForTeacherEditMessage(User user, Message sendMessage) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(user.getId());
        editMessageText.setMessageId(sendMessage.getMessageId());
        editMessageText.setText("<b>Salom! Men @MyLeague1_bot</b>");
        editMessageText.setParseMode(ParseMode.HTML);

        InlineKeyboardMarkup homeInline = InlineButtonBuilder.build(
                List.of(List.of("O'quvchilarga habar yuborish✉️"), List.of("O'quvchilar haqida malumot olishℹ️"), List.of("Feedback\uD83D\uDCAC")),
                List.of(List.of(CallBackDataValue.SEND_MESSAGE_TO_STUDENT), List.of(CallBackDataValue.ABOUT_ALL_STUDENTS), List.of(CallBackDataValue.FEEDBACK)));
        sendMessage.setReplyMarkup(homeInline);

        return editMessageText;
    }

    public static SendMessage getCurrentName(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "Sizning hozirgi usernameingiz:<b> %s </b>\nYangi username ni yuboring\uD83D\uDC47".formatted(user.getName()));
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static SendMessage editUsername(String id) {
        return new SendMessage(id, "Username muvaffaqiyatli o'zgartirildi✅");
    }

    public static SendMessage aboutMe(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "\uD83C\uDD94User id:<b> %s </b> \n\uD83C\uDFF7Username:<b> %s </b> \n\uD83C\uDFADUser role:<b> %s </b>".formatted(user.getId(), user.getName(), user.getRole()));
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public static SendMessage errorSendMessageToUser(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "<b>Habar faqat text shaklida bo'lsin❗️</b>");
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static EditMessageText sendMessageEditMessage(User user, Message execute) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(user.getId());
        editMessageText.setMessageId(execute.getMessageId());
        editMessageText.setText("Habarni yuboring(<b>faqat text shaklida bo'lsin</b>). \nAgar habarni yubormoqchi bo'lmasangiz <b>/skip</b> ni bosing");
        editMessageText.setParseMode(ParseMode.HTML);

        return editMessageText;
    }

    public static SendMessage aboutAllStudents(User user, List<User> allUser) {
        List<User> userRoleList = allUser.stream()
                .filter(u -> u.getRole() == UserRole.USER)
                .toList();

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < userRoleList.size(); i++) {
            User currentUser = userRoleList.get(i);
            text.append(String.format("<b>USER %d \n\uD83C\uDD94User id: %s \n\uD83C\uDFF7Username: %s \n\uD83C\uDFADUser role: %s </b>\n\n",
                    i + 1, currentUser.getId(), currentUser.getName(), currentUser.getRole()));
        }

        SendMessage sendMessage = new SendMessage(user.getId(), text.toString());
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static SendMessage sendMessageToTeacher(User user, String teacherId, String text) {
        SendMessage sendMessage = new SendMessage(teacherId, "Bu habarni<b> %s </b>yubordi:\n".formatted(user.getName()) + text);
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static SendPhoto sendPhotoToTeacher(User user, String teacherId, List<PhotoSize> photoSize) {
        PhotoSize largestPhoto = photoSize.stream()
                .max(Comparator.comparingInt(PhotoSize::getFileSize))
                .orElseThrow(() -> new IllegalArgumentException("Hech qanday foto topilmadi!"));

        String fileId = largestPhoto.getFileId();
        InputFile inputFile = new InputFile(fileId);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(teacherId);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setCaption("Bu rasmni<b> %s </b>yubordi:\n".formatted(user.getName()));
        sendPhoto.setParseMode("HTML");

        return sendPhoto;
    }

    public static SendAudio sendAudioToTeacher(User user, String teacherId, Audio audio) {
        String fileId = audio.getFileId();

        InputFile inputAudio = new InputFile(fileId);

        SendAudio sendAudio = new SendAudio();
        sendAudio.setChatId(teacherId);
        sendAudio.setAudio(inputAudio);
        sendAudio.setCaption("Bu audioni<b> %s </b>yubordi:\n".formatted(user.getName()));
        sendAudio.setParseMode("HTML");

        return sendAudio;
    }

    public static SendVideo sendVideoToTeacher(User user, String teacherId, Video video) {
        String fileId = video.getFileId();

        InputFile inputVideo = new InputFile(fileId);

        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(teacherId);
        sendVideo.setVideo(inputVideo);
        sendVideo.setCaption("Bu videoni<b> %s </b>yubordi:\n".formatted(user.getName()));
        sendVideo.setParseMode("HTML");

        return sendVideo;
    }

    public static SendVideoNote sendVideoNoteToTeacher(String teacherId, VideoNote videoNote) {
        String fileId = videoNote.getFileId();

        InputFile inputVideoNote = new InputFile(fileId);

        SendVideoNote sendVideoNote = new SendVideoNote();
        sendVideoNote.setChatId(teacherId);
        sendVideoNote.setVideoNote(inputVideoNote);

        return sendVideoNote;
    }

    public static SendMessage sendVideoNoteToReplyToTeacher(User user, String teacherId, Message executeVideoNote) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(teacherId);
        replyMessage.setText("Bu record videoni<b> %s </b>yubordi:\n".formatted(user.getName()));
        replyMessage.setParseMode("HTML");
        replyMessage.setReplyToMessageId(executeVideoNote.getMessageId());
        return replyMessage;
    }

    public static SendDocument sendDocumentToTeacher(User user, String teacherId, Document document) {
        String fileId = document.getFileId();
        InputFile inputDocument = new InputFile(fileId);

        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(teacherId);
        sendDocument.setDocument(inputDocument);
        sendDocument.setCaption("Bu faylni<b> %s </b>yubordi:\n".formatted(user.getName()));
        sendDocument.setParseMode("HTML");

        return sendDocument;
    }

    public static SendVoice sendVoiceTeacher(User user, String teacherId, Voice voice) {
        String fileId = voice.getFileId();

        InputFile inputVoice = new InputFile(fileId);

        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(teacherId);
        sendVoice.setVoice(inputVoice);
        sendVoice.setCaption("Bu ovozli habarni<b> %s </b>yubordi:\n".formatted(user.getName()));
        sendVoice.setParseMode("HTML");

        return sendVoice;
    }

    public static SendMessage sendWrongMessageToTeacher(String userId) {
        SendMessage sendMessage = new SendMessage(userId, "<b>Faqat shu turdagi habarni yubora olasiz(text, rasm, video, ovozli habar, audio, fayl)</b>");
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static SendMessage sendPhoneNumberSuccess(User user) {
        return new SendMessage(user.getId(), "Telefon raqamingiz qabul qilindi✔️");
    }

    public static DeleteMessage deleteMessage(User user, Message execute) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(user.getId());
        deleteMessage.setMessageId(execute.getMessageId());
        return deleteMessage;
    }

    public static SendMessage errorUsernameType(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "<b>Username faqat text shaklida bo'lsin. Qaytadan urinib ko'ring!</b>");
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static EditMessageText errorUsernameTypeEditMessage(User user, Message execute) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(user.getId());
        editMessageText.setMessageId(execute.getMessageId());
        editMessageText.setText("Sizning hozirgi usernameingiz:<b> %s </b>\nYangi username ni yuboring\uD83D\uDC47".formatted(user.getName()));
        editMessageText.setParseMode(ParseMode.HTML);
        return editMessageText;
    }

    public static SendMessage sendMessageToAllStudentSuccessfully(String id) {
        SendMessage sendMessage = new SendMessage(id, "<b>Habaringiz barcha o'quvchilarga yuborildi✔️</b>");
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static SendMessage sendPhoneNumber(User user) {
        KeyboardButton button = new KeyboardButton("☎️Telefon raqamingizni yuboring");
        button.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(button);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(Collections.singletonList(row));
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage(user.getId(), "\uD83D\uDCDEIltimos, telefon raqamingizni yuboring");
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public static SendMessage writeFeedback(User user) {
        return new SendMessage(user.getId(), "Bot haqida fikr-mulohaza yozib qoldiring✍️");
    }

    public static SendMessage getFeedback(User user) {
        return new SendMessage(user.getId(), "Yaxshi fikrlaringiz uchun rahmat! Bizning xizmatlarimizni yaxshilashga harakat qilamiz\uD83E\uDEE1");
    }

    public static SendMessage sendFeedbackToMe(User user, String feedbacktext) {
        SendMessage sendMessage = new SendMessage(my_id, "<b>Feedback from: %s \nFeedback text: %s </b>".formatted(user.getName(), feedbacktext));
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.disableNotification();
        return sendMessage;
    }

    public static SendMessage errorSendFeedback(User user) {
        SendMessage sendMessage = new SendMessage(user.getId(), "<b>Feedback faqat text shaklida bo'lsin❗️</b>");
        sendMessage.setParseMode(ParseMode.HTML);
        return sendMessage;
    }

    public static SendMessage sendMeAllMessage(Long id, Message message) {
        return new SendMessage(id.toString(), message.getText());
    }

}