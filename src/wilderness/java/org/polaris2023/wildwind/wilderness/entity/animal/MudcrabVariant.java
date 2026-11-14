package org.polaris2023.wildwind.wilderness.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.entity.variant.*;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.wilderness.WildWindWilderness;
import org.polaris2023.wildwind.wilderness.registry.DatadrivenRegistryKey;
import org.polaris2023.wildwind.wilderness.tag.ModBiomeTags;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 泥沼蟹变种数据喵~
 *
 * @author Mon-Landis
 * @author safe_saffawa
 */
public record MudcrabVariant(ClientAsset.ResourceTexture assetInfo,
                             SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<MudcrabVariant> DIRECT_CODEC = RecordCodecBuilder.create(
            i -> i.group(
                            ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(MudcrabVariant::assetInfo),
                            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(MudcrabVariant::spawnConditions)
                    )
                    .apply(i, MudcrabVariant::new)
    );
    public static final Codec<MudcrabVariant> NETWORK_CODEC = RecordCodecBuilder.create(
            i -> i.group(ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(MudcrabVariant::assetInfo)).apply(i, info -> new MudcrabVariant(info, SpawnPrioritySelectors.EMPTY))
    );
    public static final Codec<Holder<MudcrabVariant>> CODEC = RegistryFixedCodec.create(DatadrivenRegistryKey.MUDCRAB_VARIANT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MudcrabVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DatadrivenRegistryKey.MUDCRAB_VARIANT);

    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return spawnConditions.selectors();
    }


    public static final ResourceKey<MudcrabVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE.toString());
    public static final ResourceKey<MudcrabVariant> WARM = createKey(TemperatureVariants.WARM.toString());
    public static final ResourceKey<MudcrabVariant> COLD = createKey(TemperatureVariants.COLD.toString());

    /**
     * 用字符串接收 ID，再经 {@link org.polaris2023.wildwind.imp.ImpIds} 取回本族的原生类型。
     *
     * <p>为什么形参是 {@code String} 而不是资源 ID 类型：{@code TemperatureVariants} 的三个常量
     * 在 26.x 是 {@code Identifier}、在旧族是 {@code ResourceLocation}（1.20.6 甚至整个类都不存在）。
     * 若把形参写成其中任一个，源码就锁死在单一族上（{@code checkImpBoundary} 会拦）。
     * 走 {@code toString()} → {@code ImpIds} 这条路，三个族都能编译，版本分叉留在 imp 内。</p>
     */
    private static ResourceKey<MudcrabVariant> createKey(String id) {
        return ResourceKey.create(DatadrivenRegistryKey.MUDCRAB_VARIANT,
                org.polaris2023.wildwind.imp.ImpIds.fromString(id).unwrap());
    }

    public static void bootstrapDatagen(BootstrapContext<MudcrabVariant> registry) {
        registry.register(TEMPERATE, new MudcrabVariant(new ClientAsset.ResourceTexture(WildWindWilderness.id("entity/mudcrab/temperate").unwrap()), SpawnPrioritySelectors.fallback(0)));
        registry.register(WARM, new MudcrabVariant(new ClientAsset.ResourceTexture(WildWindWilderness.id("entity/mudcrab/warm").unwrap()), SpawnPrioritySelectors.single(new BiomeCheck(registry.lookup(Registries.BIOME).getOrThrow(ModBiomeTags.EntityGen.MUDCRABS_WARM)), 1)));
        registry.register(COLD, new MudcrabVariant(new ClientAsset.ResourceTexture(WildWindWilderness.id("entity/mudcrab/cold").unwrap()), SpawnPrioritySelectors.single(new BiomeCheck(registry.lookup(Registries.BIOME).getOrThrow(ModBiomeTags.EntityGen.MUDCRABS_COLD)), 1)));
    }

    public static Holder<MudcrabVariant> lookup(RegistryAccess access, ResourceKey<MudcrabVariant> key){
        return access.getOrThrow(key);
    }

}
