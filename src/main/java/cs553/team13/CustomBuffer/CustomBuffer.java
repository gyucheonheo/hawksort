package cs553.team13.CustomBuffer;

import java.io.*;

public class CustomBuffer implements CustomBuffer_I {
    private BufferedReader br;
    private File source;
    private int size;
    private String record;

    public CustomBuffer(File source, int size) throws IOException {
        this.size = size;
        this.source = source;
        this.br = new BufferedReader(new FileReader(this.source), this.size);
        this.record = this.readRecordFromReader(this.br);
    }
        private String readRecordFromReader(BufferedReader br) throws IOException {
            return br.readLine();
        }
    @Override
    public boolean isEmpty() {
        return this.record== null;
    }

    @Override
    public BufferedReader getBufferedRead() {
        return this.br;
    }

    @Override
    public String getRecord() {
        return this.record;
    }

    @Override
    public String pollRecord() throws IOException {
        String record = this.getRecord();
        this.record = this.readRecordFromReader(br);
        return record;
    }

    @Override
    public File getSourceFile() {
        return this.source;
    }
}

