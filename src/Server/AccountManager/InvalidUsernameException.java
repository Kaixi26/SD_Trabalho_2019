package Server.AccountManager;

public class InvalidUsernameException extends Exception {

    InvalidUsernameException(){
        super();
    }

    InvalidUsernameException(String msg){
        super(msg);
    }
}
