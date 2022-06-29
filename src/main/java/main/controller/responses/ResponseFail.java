package main.controller.responses;

/**
 * Класс ответа в случае неудачи
 */
public class ResponseFail extends Response
{
    // Описание ошибки
    private final String error;

    public ResponseFail(boolean result, String error)
    {
        super(result);
        this.error = error;
    }

    public String getError()
    {
        return error;
    }
}
