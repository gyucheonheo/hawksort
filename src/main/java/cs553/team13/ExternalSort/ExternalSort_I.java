package cs553.team13.ExternalSort;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ExternalSort_I {
    List<File> chunkFiles(File file) throws IOException;
    void mergeLocalChunks(List<File> files, File outputfile) throws IOException;
}
