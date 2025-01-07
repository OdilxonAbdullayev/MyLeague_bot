package processor;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

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
        throw new RuntimeException("Processor not supported for update %s ".formatted(update.toString()));
    }

}
