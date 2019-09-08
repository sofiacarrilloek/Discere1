package com.jhpat.discere;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Abhi on 11 Feb 2018 011.
 */

public class MyHttpEntity extends HttpEntityWrapper {

    private ProgressListener progressListener;

    public MyHttpEntity(final HttpEntity entity, final ProgressListener progressListener) {
        super(entity);
        this.progressListener = progressListener;
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        this.wrappedEntity.writeTo(outStream instanceof ProgressOutputStream ? outStream :
                new ProgressOutputStream(outStream, this.progressListener,
                        this.getContentLength()));
    }

    public interface ProgressListener {
        void transferred(float progress);
    }

    public static class ProgressOutputStream extends FilterOutputStream {

        private final ProgressListener progressListener;

        private long transferred;

        private long total;

        public ProgressOutputStream(final OutputStream outputStream,
                                    final ProgressListener progressListener,
                                    long total) {

            super(outputStream);
            this.progressListener = progressListener;
            this.transferred = 0;
            this.total = total;
        }

        @Override
        public void write(byte[] buffer, int offset, int length) throws IOException {

            out.write(buffer, offset, length);
            this.transferred += length;
            this.progressListener.transferred(this.getCurrentProgress());
        }

        @Override
        public void write(byte[] buffer) throws IOException {

            out.write(buffer);
            this.transferred++;
            this.progressListener.transferred(this.getCurrentProgress());
        }

        private float getCurrentProgress() {
            return ((float) this.transferred / this.total) * 100;
        }
    }
}
