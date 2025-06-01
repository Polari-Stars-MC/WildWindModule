package org.polaris2023.ww_ag.utils;

import com.ibm.icu.impl.duration.impl.DataRecord;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import org.polaris2023.ww_ag.datagen.WWDataRepeater;

/**
 * @author baka4n
 * @code @Date 2025/5/28 11:44:33
 */
public interface IDatagen<
        E,
        P extends E,
        G,
        T extends AbstractBuilder<E, P, G, T>> {
    T ww_ag$datagen(NonNullBiConsumer<DataGenContext<E, P>, WWDataRepeater> consumer);
}
