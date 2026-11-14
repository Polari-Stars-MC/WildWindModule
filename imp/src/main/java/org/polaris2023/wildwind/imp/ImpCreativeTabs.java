package org.polaris2023.wildwind.imp;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

/**
 * 创造模式物品栏「按锚点插入」的版本无关口（ADR-0003）。
 *
 * <h2>问题本质</h2>
 * <p>{@code BuildCreativeModeTabContentsEvent} 的<b>插入 API 是 1.21 才有的</b>（javap 核对
 * 20.6.144 / 21.1.209 / 26.2.0.75）：</p>
 * <ul>
 *   <li><b>1.20.6</b>：只有 {@code getEntries()}（返回
 *       {@code MutableHashedLinkedMap<ItemStack, TabVisibility>}）与
 *       {@code accept(stack, visibility)}——<b>没有</b> {@code insertAfter/Before/First/remove}。
 *       定位插入只能自己走 {@code getEntries().putAfter(anchor, stack, visibility)}。</li>
 *   <li><b>1.21.1 / 26.2</b>：事件类上有 {@code insertAfter(ItemStack, ItemStack, TabVisibility)}，
 *       语义是「锚点必须已在条目里，否则抛 {@code IllegalArgumentException}」。</li>
 * </ul>
 *
 * <p>内容模块不该为此分叉，所以把「插到锚点之后」收敛成本方法。</p>
 *
 * <h2>语义对齐（关键）</h2>
 * <p>1.20.6 的 {@code MutableHashedLinkedMap.putAfter} 在锚点<b>不存在</b>时会退化成
 * 「追加到末尾」（源码：{@code if (target == null) return put(key, value);}），而新族会抛
 * {@code IllegalArgumentException}。若照抄 putAfter，同一个数据包在旧族会<b>静默乱序</b>、
 * 在新族会崩——那是两种都不可接受的语义。</p>
 *
 * <p>因此旧族分支显式补上同样的存在性校验，让两族都表现为「锚点不在 → 抛异常」，
 * 与 1.21+ 的原生行为逐字一致，不引入任何静默降级。</p>
 *
 * @author baka4n
 * @author safe_saffawa
 */
public final class ImpCreativeTabs {

    private ImpCreativeTabs() {}

    /** 26.x / 1.21：直接用事件自带的 {@code insertAfter}（原生语义）。 */
    @Since("1.21")
    @Name("insertAfter")
    public static void insertAfterModern(net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent event,
                                         ItemStack anchor, ItemStack stack,
                                         CreativeModeTab.TabVisibility visibility) {
        event.insertAfter(anchor, stack, visibility);
    }

    /**
     * 1.20.6：没有 {@code insertAfter}，走 {@code getEntries().putAfter(...)}，
     * 并补上 1.21+ 才有的「锚点必须存在」校验（见类注释的语义对齐说明）。
     */
    @Until("1.21")
    @Name("insertAfter")
    public static void insertAfterLegacy(net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent event,
                                         ItemStack anchor, ItemStack stack,
                                         CreativeModeTab.TabVisibility visibility) {
        if (!event.getEntries().contains(anchor)) {
            // 与 1.21+ 的 assertTargetExists 保持同一语义：锚点缺失是数据错误，不静默吞掉
            throw new IllegalArgumentException(
                    "锚点物品不在该物品栏中，无法定位插入：" + anchor);
        }
        event.getEntries().putAfter(anchor, stack, visibility);
    }
}
