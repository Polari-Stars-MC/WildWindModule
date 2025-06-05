package org.polaris2023.ww_ag.datagen;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.fml.LogicalSide;
import org.polaris2023.ww_ag.common.registrate.WWProviderType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

/**
 * @author baka4n
 * @code @Date 2025/6/4 22:33:39
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WWConfigGen extends ConfigDataProvider implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;
    public WWConfigGen(AbstractRegistrate<?> parent, DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd, String name) {
        super(generator, pvd, name);
        this.parent = parent;
    }
    private Collector collector;
    public void add(NonNullConsumer<Collector> consumer) {
        consumer.accept(collector);
    }

    @Override
    public void add(Collector collector) {
        this.collector = collector;
        parent.genData(WWProviderType.CFG, this);
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }
}
