package flapjack.io;

import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.io.File;


public interface FileUtil {
    ScatteringByteChannel channel(File file);
    void close(ReadableByteChannel channel);
    ByteBuffer map(ScatteringByteChannel channel, long offset, long length);
    long length(File file);
}
