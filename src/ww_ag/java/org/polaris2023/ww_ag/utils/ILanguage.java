package org.polaris2023.ww_ag.utils;

/**
 * @author baka4n
 * @code @Date 2025/5/28 01:05:28
 */
public interface ILanguage<E,
        P extends E,
        G,
        T extends com.tterrag.registrate.builders.AbstractBuilder<E, P, G, T>> {
    T ww_ag$zh_cn(String name);
    T ww_ag$zh_tw(String name);
    T ww_ag$zh_hk(String name);
}
