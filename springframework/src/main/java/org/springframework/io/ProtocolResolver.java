package org.springframework.io;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;

@FunctionalInterface
public interface ProtocolResolver {

    @Nullable
    Resource resolve(String location, ResourceLoader resourceLoader);

}
