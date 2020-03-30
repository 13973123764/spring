package org.springframework.core;

/**
 * Common interface for managing aliases.
 * 管理别名的通过接口
 */
public interface AliasRegistry {

    /**
     * Given a name, register an alias for it.
     * 给定一个名称 给它注册一个别名
     * @param name      the canonical name          真实的名称
     * @param alias     the alias to be registered  注册后的别名
     * @throws IllegalStateException if the alias is already in use  如果已经用户则抛出异常
     */
    void registerAlias(String name, String alias);

    /**
     * Remove the specified alias from this registry.
     * 从注册器中删除一个特定别名
     * @param alias
     */
    void removeAlias(String alias);

    /**
     * Determine whether this given name is defines as an alias
     * 确定此给定名称定义为别名
     * @param name
     * @return
     */
    boolean isAlias(String name);

    /**
     * Return the aliases for the given name, if defined
     * 如果定义了则从给定的名称 返回别名数组
     * @param name
     * @return
     */
    String[] getAliases(String name);







}
