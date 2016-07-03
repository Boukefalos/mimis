package jna;
import com.sun.jna.Library;
import com.sun.jna.Native;


public class HelloTest {
    public interface HelloLibrary extends Library {
        public void helloFromC();
    }

    public static void main(String[] args) {
        HelloLibrary ctest = (HelloLibrary) Native.loadLibrary("ctest", HelloLibrary.class);
        ctest.helloFromC();
    }
}
