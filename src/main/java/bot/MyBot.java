package bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import processor.AbstractProcessor;
import processor.ProcessorFactory;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class MyBot extends TelegramLongPollingBot {
    private final String botUserName;
    private final String botToken;

    {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        if (inputStream == null) {
            log.error("application.properties fayli topilmadi!");
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        botUserName = properties.getProperty("quiz-bot.telegram.bot.username");
        botToken = properties.getProperty("quiz-bot.telegram.bot.token");
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        AbstractProcessor processor = ProcessorFactory.getProcessor(update);
        processor.process(update, this);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
