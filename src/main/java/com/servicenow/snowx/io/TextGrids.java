package com.servicenow.snowx.io;

import com.servicenow.snowx.util.Bounds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TextGrids {

    public static Scanner scan(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            int rowLength = line.length(), rowCount;
            for (rowCount = 0; line != null; rowCount++, line = reader.readLine()) {
                if (line.length() != rowLength)
                    throw new TextGridReadException(
                            "Lines are inconsistent length; expected " + rowLength +
                                    " characters on line " + rowCount);
            }
            return new Scanner(path, rowLength, rowCount);
        } catch (IOException e) {
            throw new TextGridReadException(e);
        }
    }

    public static class Scanner {

        private final SeekableByteChannel file;
        private final int gridWidth;
        private final int gridHeight;
        private boolean parallel;

        private Scanner(Path path, int gridWidth, int gridHeight) {
            try {
                this.file = new RandomAccessFile(path.toFile(), "r").getChannel();
                this.gridWidth = gridWidth;
                this.gridHeight = gridHeight;
            } catch (IOException e) {
                throw new TextGridReadException(e);
            }
        }

        public Scanner parallel(boolean parallel) {
            this.parallel = parallel;
            return this;
        }

        public Stream<TextBlock> blocks(int blockWidth, int blockHeight) {
            return StreamSupport.stream(blockSpliterator(blockWidth, blockHeight), parallel);
        }

        private Spliterator<TextBlock> blockSpliterator(int blockWidth, int blockHeight) {
            return new TextFileGridSpliterator(
                    new TextBlockReader(blockWidth, blockHeight),
                    new Bounds(0, 0, gridWidth, gridHeight)
            );
        }

        /**
         * Given a random access file, get the set of all (overlapping) blocks of given dimensions as CharSequence arrays.
         */
        class TextFileGridSpliterator implements Spliterator<TextBlock> {

            private final TextBlockReader block;
            private final Bounds bounds;
            private int offsetX, offsetY;

            private TextFileGridSpliterator(TextBlockReader block, Bounds bounds) {
                this.block = block;
                this.bounds = bounds;
                this.offsetX = bounds.getStartX();
                this.offsetY = bounds.getStartY();
            }

            @Override
            public boolean tryAdvance(Consumer<? super TextBlock> consumer) {
                if (getRoomX() >= block.getWidth()) {
                    consumer.accept(readBlock(offsetX++, offsetY));
                    return true;
                }
                else if (getRoomY() > block.getHeight()) {
                    consumer.accept(readBlock(offsetX = bounds.getStartX(), ++offsetY));
                    offsetX++;
                    return true;
                }
                return false;
            }

            private TextBlock readBlock(int x, int y) {
                return block.read(file, x, y, gridWidth);
            }

            @Override
            public Spliterator<TextBlock> trySplit() {
                if (getRoomX() >= 2*block.getWidth())
                    return splitX();
                else if (getRoomY() >= 2*block.getHeight())
                    return splitY();
                return null;
            }

            private int getRoomX() {
                return bounds.getEndX() - offsetX;
            }

            private int getRoomY() {
                return bounds.getEndY() - offsetY;
            }

            private Spliterator<TextBlock> splitX() {
                final int originalEndX = bounds.getEndX(),
                          splitX = offsetX + (originalEndX - offsetX) / 2;
                this.bounds.setEndX(splitX);
                return new TextFileGridSpliterator(block.clone(),
                        new Bounds(splitX, offsetY, originalEndX - splitX, bounds.getHeight()));
            }

            private Spliterator<TextBlock> splitY() {
                final int originalEndY = bounds.getEndY(),
                          splitY = offsetY + (originalEndY - offsetY) / 2;
                this.bounds.setEndY(splitY);
                return new TextFileGridSpliterator(block.clone(),
                        new Bounds(offsetX, splitY, bounds.getWidth(), originalEndY - splitY));
            }

            @Override
            public long estimateSize() {
                return bounds.getArea();
            }

            @Override
            public int characteristics() {
                return Spliterator.CONCURRENT & Spliterator.SIZED;
            }

        }

    }

}
