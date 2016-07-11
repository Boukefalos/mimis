package base;

import java.io.IOException;

public interface Sender extends Control {
    public void send(byte[] buffer) throws IOException;
}
