package cs553.team13.ExternalSort;

import cs553.team13.CustomBuffer.CustomBuffer;
import cs553.team13.CustomBuffer.CustomBuffer_I;
import cs553.team13.InternalSort.MergeSort;

import java.io.*;
import java.util.*;

public class ExternalSort implements ExternalSort_I{
    private long filesize;
    public List<File> chunkFiles(File file) throws IOException {
        this.filesize = file.length();
        return wrappedChunkFiles(file);
    }
        private List<File> wrappedChunkFiles(File file) throws IOException{
            List<File> chunks = new ArrayList<>();
            FileInputStream fs = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            long chunk = this.getChunkSize(file.length());
            List<String> records_in_chunk = new ArrayList<>();

            String line = "";
            long current_chunk_size = 0;
            while(line != null) {
                while ((current_chunk_size < chunk) && line != null /* How to get rid of this */) {
                    if(!line.isEmpty()){
                        records_in_chunk.add(line);
                        current_chunk_size = current_chunk_size + line.length();
                    }
                    try {
                        line = br.readLine();
                    } catch (OutOfMemoryError e){
                        break;
                    }
                }
                current_chunk_size = 0;
                MergeSort<String> mg = new MergeSort<>();
                mg.sort(records_in_chunk);
                File temp_file = File.createTempFile("tmp", ".temp", new File("./"));
                BufferedWriter bw = new BufferedWriter(new FileWriter(temp_file));

                for(String record : records_in_chunk){
                    bw.write(record+"\r"); // \\r causes valsort to throw unsorted
                    bw.newLine();
                }
                bw.close();
                temp_file.deleteOnExit();
                chunks.add(temp_file);
                records_in_chunk = new ArrayList<>();
            }
            br.close();

            return chunks;
        }

        private long getChunkSize(long file_size){

            long run_size = Math.max(Runtime.getRuntime().maxMemory()/8, Runtime.getRuntime().freeMemory())/2;
            int k_way = (int)Math.ceil((file_size / run_size))+5;
            long single_chunk_size = file_size / k_way;
            return single_chunk_size;
        }

    public void mergeLocalChunks(List<File> chunks, File final_output) throws IOException {
        wrappedMergeLocalChunks(chunks, final_output);
    }
        private void wrappedMergeLocalChunks(List<File> chunks, File final_output) throws IOException{
            /*
                PriorityQueue idea : https://en.wikipedia.org/wiki/K-way_merge_algorithm
                                    https://stackoverflow.com/questions/5055909/algorithm-for-n-way-merge
             */

            Comparator<String> comp = String::compareTo;

            PriorityQueue<CustomBuffer_I> priority_queue = new PriorityQueue<>
                    (11, (lhs, rhs) -> comp.compare(lhs.getRecord(), rhs.getRecord()));

            BufferedWriter bw = new BufferedWriter(new FileWriter(final_output));
            int counter = 0;
            for(File chunk : chunks){
//                System.out.println(this.getBufferSize(chunks.size()-counter));
                priority_queue.add(new CustomBuffer(chunk, this.getBufferSize(chunks.size())));
                counter++;
            }

            while(!priority_queue.isEmpty()) {
                CustomBuffer_I buffer = priority_queue.poll();
                String r = buffer.pollRecord();
                bw.write(r+"\r");
                bw.newLine();
                if(buffer.isEmpty()){
                    buffer.getBufferedRead().close();
                    boolean isDeleted = buffer.getSourceFile().delete();
                    if(!isDeleted){
                        throw new RuntimeException();
                    }
                } else {
                    priority_queue.add(buffer);
                }
            }
            bw.close();
        }

        private int getBufferSize(int number_of_chunks) {
            return (int) (Runtime.getRuntime().freeMemory() / number_of_chunks);
        }

    private static class ChunkExceedOverFreeMemoryException extends RuntimeException {
    }
}
