package org.polaris2023.ww_ag.utils;

/**
 * @author baka4n
 * @code @Date 2025/5/28 01:05:28
 */
public interface ILanguage<E,
        P extends E,
        G,
        T extends com.tterrag.registrate.builders.AbstractBuilder<E, P, G, T>> extends ISelf<T> {
    @SuppressWarnings("unchecked")
    static <E, P extends E, G, T extends com.tterrag.registrate.builders.AbstractBuilder<E, P, G, T>> ILanguage<E, P, G, T> convert1(T t) {
        return (ILanguage<E, P, G, T>) t;
    }
    ILanguage<E, P, G, T> ww_ag$zh_cn(String name);
    ILanguage<E, P, G, T> ww_ag$zh_tw(String name);
    ILanguage<E, P, G, T> ww_ag$zh_hk(String name);
}
