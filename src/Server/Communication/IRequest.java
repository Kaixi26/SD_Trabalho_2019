package Server.Communication;

import java.net.Socket;

public interface IRequest {
    String stringSerialize();
    String getUsername();
    String getPassword();
}
