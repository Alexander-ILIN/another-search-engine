package main;

import main.service.Init;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * главный класс приложения
 */
@SpringBootApplication
public class AnotherSearchEngine
{
    private static ApplicationContext context;

    public static void main(String[] args)
    {
        context = SpringApplication.run(AnotherSearchEngine.class, args);
        Init init = context.getBean(Init.class);
        init.run();

    }

    /**
     * получение контекста приложения
     * @return контекст приложения
     */
    public static ApplicationContext getContext()
    {
        return context;
    }
}
