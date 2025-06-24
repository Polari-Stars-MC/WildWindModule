package org.polaris2023.ww_ag.common.registrate.entry;

import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.utils.ISelf;

import java.util.Locale;

/**
 * @author baka4n
 * @code @Date 2025/6/24 22:06:59
 */
@SuppressWarnings({"unchecked", "ClassCanBeRecord"})
public class StoneEntry<T extends WWRegistrate, E extends StoneEntry<T, E>> implements
        ISelf<E> {
    public final String name;
    public final T registrate;


    public String firstUpName() {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public StoneEntry(T registrate, String name) {
        this.name = name;
        this.registrate = registrate;
    }

    @Override
    public E ww_ag$self() {
        return (E) this;
    }
}
