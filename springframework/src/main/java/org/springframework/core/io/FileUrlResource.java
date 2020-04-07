package org.springframework.core.io;

import org.springframework.lang.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:40
 */
public class FileUrlResource extends UrlResource implements WritableResource {

    @Nullable
    private volatile File file;


    public FileUrlResource(URL url) {
        super(url);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }
}
