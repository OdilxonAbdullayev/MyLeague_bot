package bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineButtonBuilder {
    public static InlineKeyboardMarkup build(List<List<String>> names, List<List<String>> callback) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < names.get(i).size(); j++) {
                InlineKeyboardButton button = new InlineKeyboardButton(names.get(i).get(j));
                button.setCallbackData(callback.get(i).get(j));
                row.add(button);
            }
            buttons.add(row);
        }
        return new InlineKeyboardMarkup(buttons);

    }
}
