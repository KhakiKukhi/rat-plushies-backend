package lol.khakikukhi.ratplushies.domain.constants;

public class Messages {

    public enum WebError {
        NO_SESSION("No session found."),
        NOT_AUTHENTICATED("User is not authenticated."),
        NOT_AUTHORISED("User is not authorised.");

        private final String message;

        WebError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }
}
