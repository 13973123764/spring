package org.springframework.beans.factory.config;

import org.springframework.lang.Nullable;

public interface BeanDefinition {


    int ROLE_INFRASTRUCTURE = 2;

    int ROLE_APPLICATION = 0;

    @Nullable
    String getBeanClassName();

    void setRole(int role);

    int getRole();

}
