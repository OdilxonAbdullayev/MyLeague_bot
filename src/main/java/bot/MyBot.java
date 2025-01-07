package bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import processor.AbstractProcessor;
import processor.ProcessorFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MyBot extends TelegramLongPollingBot {
    private final String botUserName;
    private final String botToken;

    {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/resources/application.properties"));
            botUserName = properties.getProperty("quiz-bot.telegram.bot.username");
            botToken = properties.getProperty("quiz-bot.telegram.bot.token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
