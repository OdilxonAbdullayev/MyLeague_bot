package processor;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
public abstract class ProcessorFactory {
    private static final List<AbstractProcessor> processors = List.of(
            new CallBackDataProcessor(),
            new MessageProcessor()
    );

    public static AbstractProcessor getProcessor(Update update){
        for (AbstractProcessor processor: processors){
            boolean supports = processor.supports(update);
            if (supports){
                return processor;
            }
        }
        log.error("Processor not supported for update %s ".formatted(update.toString()));
        return null;
    }

}
