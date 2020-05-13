package cs553.team13.CustomBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public interface CustomBuffer_I {
    boolean isEmpty();
    BufferedReader getBufferedRead();
    String getRecord();
    String pollRecord() throws IOException;
    File getSourceFile();
}
