package pipe;

import java.io.RandomAccessFile;

public class Client {
    public static void main(String[] args) {
        try {
            // Connect to the pipe
            RandomAccessFile pipe = new RandomAccessFile("\\\\.\\pipe\\detest", "rw");
            String echoText = "Hello word\n";

            // write to pipe
            pipe.write(echoText.getBytes());

            // read response
            String echoResponse = pipe.readLine();
            System.out.println(echoResponse);
            pipe.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
