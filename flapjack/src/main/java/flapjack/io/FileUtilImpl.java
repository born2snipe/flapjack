package flapjack.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ScatteringByteChannel;


public class FileUtilImpl implements FileUtil {
    public ScatteringByteChannel channel(File file) {
        try {
            return new FileInputStream(file).getChannel();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(ReadableByteChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ByteBuffer map(ScatteringByteChannel channel, long offset, long length) {
        if (channel instanceof FileChannel) {
            FileChannel fileChannel = (FileChannel) channel;
            try {
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException("Channel is NOT a FileChannel it is a " + channel.getClass().getName());
    }

    public long length(File file) {
        return file.length();
    }
}
