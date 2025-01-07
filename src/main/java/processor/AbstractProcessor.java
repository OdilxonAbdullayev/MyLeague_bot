package processor;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import user.UserService;

public abstract class AbstractProcessor {
    protected final UserService userService = UserService.getInstance();
    public abstract boolean supports(Update update);

    public abstract void process(Update update, DefaultAbsSender sender);

}
