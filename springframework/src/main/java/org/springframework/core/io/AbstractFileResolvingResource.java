package org.springframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/04/05 下午4:15
 */
public abstract class AbstractFileResolvingResource extends AbstractResource {


    @Override
    public Resource createRelative(String relativePath) throws IOException {
        throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
    }

}
