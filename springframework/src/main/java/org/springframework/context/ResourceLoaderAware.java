package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.io.ResourceLoader;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/28 下午6:44
 */
public interface ResourceLoaderAware extends Aware {


    void setResourceLoader(ResourceLoader resourceLoader);

}
