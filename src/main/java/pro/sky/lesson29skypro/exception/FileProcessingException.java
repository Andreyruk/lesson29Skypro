package pro.sky.lesson29skypro.exception;

public class FileProcessingException extends RuntimeException {
    public FileProcessingException(){super(("Проблемы чтения файла."));}
    public FileProcessingException(String message){super(message);}
}
