package org.polaris2023.ww_ag.common.block.entity;

import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * @author baka4n
 * @code @Date 2025/7/14 22:46:12
 */
@SuppressWarnings({"unused", "LombokGetterMayBeUsed"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ComponentBlockEntity<T extends ComponentBlockEntity<T>> extends BlockEntity {

    private final PatchedDataComponentMap components;

    public ComponentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.components = new PatchedDataComponentMap(createDefaultComponents());
    }

    private DataComponentMap createDefaultComponents() {
        return DataComponentMap.builder().build();
    }

    public DataComponentMap getComponents() {
        return components;
    }

    public T updateComponents(NonNullConsumer<PatchedDataComponentMap> map) {
        map.accept(components);
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
        return self();
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        PatchedDataComponentMap.CODEC.encodeStart(NbtOps.INSTANCE, components)
                .result()
                .ifPresent(componentTag -> tag.put("components", componentTag));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("components")) {
            DataComponentMap.CODEC.parse(NbtOps.INSTANCE, tag.get("components"))
                    .result()
                    .ifPresent(loadedComponents -> {
                        // 创建新的构建器并合并现有组件
                        DataComponentMap.Builder builder = DataComponentMap.builder();
                        // 保留未改变的组件
                        components.stream().forEach(entry -> {
                            if (!loadedComponents.has(entry.type())) {
                                builder.addAll(components);
                            }
                        });
                        // 添加新加载的组件
                        builder.addAll(loadedComponents);
                        
                        this.components.setAll(builder.build());
                    });
        }
    }


    @SuppressWarnings("unchecked")
    public T self() {
        return (T) this;
    }
}
