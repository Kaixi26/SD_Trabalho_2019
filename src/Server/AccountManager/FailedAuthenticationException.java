package Server.AccountManager;

public class FailedAuthenticationException extends Exception {
    FailedAuthenticationException(){
        super();
    }

    FailedAuthenticationException(String msg){
        super(msg);
    }
}
