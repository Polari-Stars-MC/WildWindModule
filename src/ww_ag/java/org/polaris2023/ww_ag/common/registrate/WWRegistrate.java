package org.polaris2023.ww_ag.common.registrate;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import org.polaris2023.ww_ag.common.registrate.build.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

/**
* @author baka4n
* {@code @Date 2025/05/19 22:44:01}
*/
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class WWRegistrate extends L2Registrate {
    public WWRegistrate(String modid) {
        super(modid);
    }

    public SoundBuilder<SoundEvent, L2Registrate> fixRangeSound(String name, float range) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> SoundEvent.createFixedRangeEvent(loc(name), range)));
    }
    public SoundBuilder<SoundEvent, L2Registrate> variableRangeSound(String name) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> SoundEvent.createVariableRangeEvent(loc(name))));
    }

    public <T extends SoundEvent> SoundBuilder<T, L2Registrate> customSound(String name, Function<ResourceLocation, T> function) {
        return entry(name, callback -> SoundBuilder.create(this, self(), name, callback, () -> function.apply(loc(name))));
    }

    public <T extends IGlobalLootModifier> LootModifierBuilder<T, L2Registrate> glm(String name, MapCodec<T> codec) {
        return entry(name, callback -> LootModifierBuilder.create(this, self(), name, callback, codec));
    }

}
