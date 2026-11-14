package org.polaris2023.wildwind.imp;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 带固定格数物品容器的方块实体基类（ADR-0003）。
 *
 * <p>为什么需要它：容器方块实体的存读档钩子在两族里签名不同——</p>
 * <ul>
 *   <li>26.x：{@code saveAdditional(ValueOutput)} / {@code loadAdditional(ValueInput)}，
 *       物品读写走 {@code ContainerHelper.saveAllItems(ValueOutput, NonNullList)}；</li>
 *   <li>1.20 / 1.21：{@code saveAdditional(CompoundTag, HolderLookup.Provider)} /
 *       {@code loadAdditional(CompoundTag, HolderLookup.Provider)}，
 *       物品读写要额外传 {@code HolderLookup.Provider}（数据组件解析用）。</li>
 * </ul>
 *
 * <p>这两个方法是 {@code protected} 覆写，签名里带版本专属类型，
 * 所以覆写必须留在 imp；内容模块只声明格数并自行实现 {@code Container} 的存取逻辑
 * （那些方法在三族里签名完全一致），通过 {@link #items()} 拿到物品列表。</p>
 */
public abstract class ImpItemContainerBlockEntity extends net.minecraft.world.level.block.entity.BlockEntity {

    private final NonNullList<ItemStack> items;

    protected ImpItemContainerBlockEntity(net.minecraft.world.level.block.entity.BlockEntityType<?> type,
                                          net.minecraft.core.BlockPos pos,
                                          net.minecraft.world.level.block.state.BlockState state,
                                          int size) {
        super(type, pos, state);
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    /** 固定的物品列表；容量由构造时的 {@code size} 决定。 */
    protected final NonNullList<ItemStack> items() {
        return this.items;
    }

    /** 26.x：{@code ValueOutput} 版存档钩子。 */
    @Since("26.1")
    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        net.minecraft.world.ContainerHelper.saveAllItems(output, this.items);
    }

    /** 26.x：{@code ValueInput} 版读档钩子。 */
    @Since("26.1")
    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        net.minecraft.world.ContainerHelper.loadAllItems(input, this.items);
    }

    /** 1.20 / 1.21：{@code CompoundTag + HolderLookup.Provider} 版存档钩子。 */
    @Until("26.1")
    @Override
    protected void saveAdditional(net.minecraft.nbt.CompoundTag tag,
                                  net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        net.minecraft.world.ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    /** 1.20 / 1.21：{@code CompoundTag + HolderLookup.Provider} 版读档钩子。 */
    @Until("26.1")
    @Override
    protected void loadAdditional(net.minecraft.nbt.CompoundTag tag,
                                  net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        net.minecraft.world.ContainerHelper.loadAllItems(tag, this.items, registries);
    }
}
