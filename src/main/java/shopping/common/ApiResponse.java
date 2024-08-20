package shopping.common;

public class ApiResponse<T> {
    private Boolean result;
    private T data;
    private String message;

    public ApiResponse(){}

    public ApiResponse(Boolean result, T data) {
        this.result = result;
        this.data = data;
    }

    public ApiResponse(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public static <T> ApiResponse<T> ofSuccess(T data) {
        return new ApiResponse<T>(true, data);
    }

    public static ApiResponse ofError(String message) {
        return new ApiResponse(false, message);
    }

    public Boolean getResult() {
        return result;

    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
