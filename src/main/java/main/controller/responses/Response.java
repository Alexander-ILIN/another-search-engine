package main.controller.responses;

/**
 * Абстрактный класс ответов на запросы
 */
public abstract class Response
{
    // Результат
    private final boolean result;

    public Response(boolean result)
    {
        this.result = result;
    }

    public boolean getResult()
    {
        return result;
    }

}
