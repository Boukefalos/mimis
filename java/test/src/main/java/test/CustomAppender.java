package test;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class CustomAppender extends AppenderSkeleton {
    protected int bla = 9999;
    
    public void setBla(int bla) {
        this.bla = bla;
    }

    public int getBla() {
        return bla;
    }

    public boolean requiresLayout() {
        return true;
    }

    public void close() {}

    protected void append(LoggingEvent loggingEvent) {
        System.out.print(layout.format(loggingEvent));
    }

}
