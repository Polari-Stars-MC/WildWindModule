package org.polaris2023.wildwind.wilderness.mixin;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPool.class)
public interface LootPoolEntriesAccessor {
    @Accessor("entries") List<LootPoolEntryContainer> wildwind$entries();
    @Mutable @Accessor("entries") void wildwind$setEntries(List<LootPoolEntryContainer> entries);
}
