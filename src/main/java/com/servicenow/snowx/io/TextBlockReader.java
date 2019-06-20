package com.servicenow.snowx.io;

import com.servicenow.snowx.util.Bounds;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;

public class TextBlockReader {

    private final ByteBuffer[] buffers;

    TextBlockReader(int width, int height) {
        this.buffers = new ByteBuffer[height];
        for (int i=0; i < height; i++)
            this.buffers[i] = ByteBuffer.allocate(width);
    }

    /**
     * Get view of buffers after reading block of text file.
     */
    TextBlock read(SeekableByteChannel file, int x, int y, int rowWidth) {
        try {
            final CharSequence[] textBlock = new CharSequence[buffers.length];
            for (int i = 0; i < textBlock.length; i++) {
                ByteBuffer buffer = buffers[i];
                buffer.clear();
                synchronized (file) {
                    int position = (rowWidth + 1) * (y + i) + x;
                    file.position(position);
                    int bytesRead = file.read(buffer);
                    if (bytesRead != buffer.capacity()) {
                        throw new IllegalStateException("Row could not be read in full! "
                                + bytesRead + " bytes read at position " + position + " " + file.position());
                    }
                }
                textBlock[i] = StandardCharsets.ISO_8859_1.decode(buffer.flip());
            }
            return new TextBlock(new Bounds(x, y, getWidth(), getHeight()), textBlock);
        } catch (IOException e) {
            throw new TextGridReadException(e);
        }
    }

    public int getWidth() {
        return buffers[0].capacity();
    }

    public int getHeight() {
        return buffers.length;
    }

    @Override
    public TextBlockReader clone() {
        return new TextBlockReader(getWidth(), getHeight());
    }

}
