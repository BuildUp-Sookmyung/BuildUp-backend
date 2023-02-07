package buildup.server.common.response;

public class ResponseUtil {

    public static <T> BasicResponse<T> success (T response) {
        return new BasicResponse<>(true, response, null);
    }

    public static <T> BasicResponse<T> error (ErrorEntity e) {
        return new BasicResponse<>(false, null, e);
    }

}
