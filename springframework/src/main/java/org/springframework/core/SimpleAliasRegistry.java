package org.springframework.core;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 别名注册器
 * @author ZhouFan
 * @Date 2020/03/29 上午9:17
 */
public class SimpleAliasRegistry implements AliasRegistry{

    /** 从别名映射到真实名称 */
    private final Map<String, String> aliasMap = new ConcurrentHashMap<>(16);


    /**
     * 注册别名
     * @param name      the canonical name          真实的名称
     * @param alias     the alias to be registered  注册后的别名
     */
    @Override
    public void registerAlias(String name, String alias) {
        Assert.hasText(name, "bean真实名称不能为空");
        Assert.hasText(name, "bean别名不能为空");
        // 同步锁
        synchronized (this.aliasMap) {
            // 真实名称等于别名，则从map中删除这个别名
            if (alias.equals(name)) {
                this.aliasMap.remove(alias);
                System.out.println("真实名称等于别名，则从map中删除这个别名");
            }
            else {
                String registeredName = this.aliasMap.get(alias);
                // 不为空
                if (registeredName != null) {
                    if (registeredName.equals(name)) {
                        // 相等表示已经存在 无需重新注册
                        return;
                    }
                }
                // 是否允许重写别名
                if (!allowAliasOverriding()) {
                    throw new IllegalStateException("Cannot define alias '" + alias + "' for name '" +
                            name + "': It is already registered for name '" + registeredName + "'.");
                }
                // 循环检查别名
                checkForAliasCircle(name, alias);
                // 存入map中
                this.aliasMap.put(alias, name);
            }
        }

    }

    protected boolean allowAliasOverriding() {
        return true;
    }

    protected void checkForAliasCircle(String name, String alias){
        if(hashAlias(alias, name)){
            throw new IllegalStateException("Cannot register alias '" + alias +
                    "' for name '" + name + "': Circular reference - '" +
                    name + "' is a direct or indirect alias for '" + alias + "' already");
        }
    }

    public boolean hashAlias(String name, String alias){
        for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
            String registeredName = entry.getValue();
            // 已注册的名称等于当前要注册的真实名称
            if (registeredName.equals(name)) {
                String registeredAlias = entry.getKey();
                if (registeredAlias.equals(alias) || hashAlias(registeredAlias, alias)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void removeAlias(String alias) {
    }

    @Override
    public boolean isAlias(String name) {
        return false;
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    /**
     * 确定原始名称，将别名解析为规范名称。
     * @param name
     * @return
     */
    public String canonicalName(String name) {
        String canonicalName = name;
        String resolvedName;
        do {
            resolvedName = this.aliasMap.get(canonicalName);
            if (resolvedName != null) {
                canonicalName = resolvedName;
            }
        }
        while (resolvedName != null);
        return canonicalName;
    }



}
