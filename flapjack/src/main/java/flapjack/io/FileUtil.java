package flapjack.io;

import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.io.File;


public interface FileUtil {
    ScatteringByteChannel channel(File file);
    void close(ReadableByteChannel channel);
}
