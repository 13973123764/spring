package org.springframework.core.io;

import java.io.IOException;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:16
 */
public interface Resource extends InputStreamSource {

    String getDescription();

    Resource createRelative(String relativePath) throws IOException;
}
