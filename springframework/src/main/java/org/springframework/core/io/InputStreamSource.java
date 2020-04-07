package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:16
 */
public interface InputStreamSource {

    InputStream getInputStream() throws IOException;
}
