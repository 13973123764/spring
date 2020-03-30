package org.springframework.io;

import org.springframework.lang.Nullable;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class DefaultResourceLoader implements ResourceLoader{

    @Nullable
    private ClassLoader classLoader;

    private final Set<ProtocolResolver> protocolResolvers = new LinkedHashSet<>(4);

    public Resource getResource(String location) {
        return null;
    }


}
