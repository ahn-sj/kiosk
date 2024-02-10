package sample.test.kiosk.spring.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final HttpStatus status;
    private final int code;
    private final String message;
    private final T data;

    public ApiResponse(final HttpStatus status, final String message, final T data) {
        this.status = status;
        this.code = status.value();
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, final T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, final T data) {
        return of(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok(final T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }

}
