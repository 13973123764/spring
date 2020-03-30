package org.springframework.core.type.filter;

import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午8:01
 */
@FunctionalInterface
public interface TypeFilter {

    boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
            throws IOException;

}
