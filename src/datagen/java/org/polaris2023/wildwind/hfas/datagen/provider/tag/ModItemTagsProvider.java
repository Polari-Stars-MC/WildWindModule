package org.polaris2023.wildwind.hfas.datagen.provider.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import org.polaris2023.wildwind.hfas.datagen.ReleaseModule;
import net.minecraft.resources.Identifier;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.wilderness.tag.ModItemTags;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * 物品标签生成器
 * 为木质物品添加原版和模组标签
 * <p>
 * 迁移至26.2时旧有的IntrinsicHolderTagsProvider已被移除。改为父类TagsProvider并由addToTag方法桥接。
 * TODO_FINISHED: 部分item tag key存在缺失问题，已模仿原版行为进行处理，但无法确定这种缺失是否系系统性删除，无法确保对应功能一定可以正常工作。请求测试阶段重点检查。
 *  见[Need Check]注释部分。
 *  ——landis, 2026/9/1
 *
 * @author baka4n
 * @since 2026/04/15
 */
public class ModItemTagsProvider extends TagsProvider<Item> {
    private final ReleaseModule module;


    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ReleaseModule module) {
//        super(output, Registries.ITEM, lookupProvider, item -> item.builtInRegistryHolder().key(), HFASMod.MOD_ID);
        super(output, Registries.ITEM, lookupProvider, module.id());
        this.module = module;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        if (module == ReleaseModule.CORE) {
            addToTag(TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("signs")), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SIGN.get().asItem());
            addToTag(TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("hanging_signs")), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_HANGING_SIGN.get().asItem());
            addToTag(TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("wooden_shelves")), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SHELF.get().asItem());
        }
        registerWoodLogItemTag("blaze",
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG.get(), org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG.get(), org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD.get());
        registerWoodLogItemTag("soul",
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG.get(), org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG.get(), org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD.get());
        registerWoodLogItemTag("azalea",
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_LOG.get(), org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WOOD.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG.get(), org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_WOOD.get());

        // ==================== 焚烬木物品标签 ====================
        registerWoodItemTags(
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_BLAZE_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PLANKS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_STAIRS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SLAB.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_FENCE_GATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_DOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_TRAPDOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_PRESSURE_PLATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_BUTTON.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_LEAVES.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.BLAZE_SAPLING.get()
        );

        // ==================== 灵焰木物品标签 ====================
        registerWoodItemTags(
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_LOG.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.STRIPPED_SOUL_WOOD.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PLANKS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_STAIRS.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SLAB.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_FENCE_GATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_DOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_TRAPDOOR.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_PRESSURE_PLATE.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_BUTTON.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_LEAVES.get(),
                org.polaris2023.wildwind.wilderness.block.ModBlocks.SOUL_SAPLING.get()
        );

        // ==================== 杜鹃木物品标签 ====================
        registerWoodItemTags(
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_LOG.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_WOOD.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_LOG.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.STRIPPED_AZALEA_WOOD.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PLANKS.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_STAIRS.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_SLAB.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_FENCE_GATE.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_DOOR.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_TRAPDOOR.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_PRESSURE_PLATE.get(),
                org.polaris2023.wildwind.core.block.ModBlocks.AZALEA_BUTTON.get(),
                null, // 杜鹃木使用原版树叶
                null  // 杜鹃木使用原版树苗
        );

        if (module == ReleaseModule.WILDERNESS) {
            addToTag(ItemTags.ARROWS, org.polaris2023.wildwind.wilderness.registry.ModItems.TIPPED_ARROW.get());
            tag(Tags.Items.ANIMAL_FOODS)
                    .addTag(ModItemTags.MUDCRAB_FOOD);
            addToTag(ModItemTags.MUDCRAB_FOOD, Items.SPIDER_EYE);

            // ==================== 食人鱼相关物品标签 ====================
            // 猫、豹猫以生食人鱼为食
            addToTag(ItemTags.CAT_FOOD, org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.get());
            addToTag(ItemTags.OCELOT_FOOD, org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.get());
            // 狼以生/熟食人鱼为食
            addToTag(ItemTags.WOLF_FOOD, org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.get(), org.polaris2023.wildwind.wilderness.registry.ModItems.COOKED_PIRANHA.get());
            // 原版鱼类标签
            addToTag(ItemTags.FISHES, org.polaris2023.wildwind.wilderness.registry.ModItems.PIRANHA.get(), org.polaris2023.wildwind.wilderness.registry.ModItems.COOKED_PIRANHA.get());
            // 熟鱼食物标签
            addToTag(Tags.Items.FOODS_COOKED_FISH, org.polaris2023.wildwind.wilderness.registry.ModItems.COOKED_PIRANHA.get());
        }
    }

    /**
     * 注册木质物品标签
     */
    private void registerWoodItemTags(
            Block log, Block wood,
            Block strippedLog, Block strippedWood,
            Block planks, Block stairs, Block slab,
            Block fence, Block fenceGate,
            Block door, Block trapdoor,
            Block pressurePlate, Block button,
            Block leaves, Block sapling) {
        if (!module.owns(log)) return;

        // 原木物品标签
        addToTag(ItemTags.LOGS, log, wood, strippedLog, strippedWood);
        addToTag(ItemTags.LOGS_THAT_BURN, log, wood, strippedLog, strippedWood);
        addToTag(Tags.Items.STRIPPED_LOGS, strippedLog);
        addToTag(Tags.Items.STRIPPED_WOODS, strippedWood);
//        tag(ItemTags.LOGS)
//                .add(log.asItem())
//                .add(wood.asItem())
//                .add(strippedLog.asItem())
//                .add(strippedWood.asItem());

        // 木板物品标签
        addToTag(ItemTags.PLANKS, planks);

        // 木质楼梯物品标签
        addToTag(ItemTags.WOODEN_STAIRS,stairs);

        // 木质台阶物品标签
        addToTag(ItemTags.WOODEN_SLABS, slab);

        // 木质栅栏物品标签
        addToTag(ItemTags.WOODEN_FENCES, fence);
        addToTag(Tags.Items.FENCES_WOODEN, fence);

        // 栅栏物品标签
        // 栅栏tag默认包含"#c:fences/wooden"，弃用
//        tag(Tags.Items.FENCES)
//                .add(fence.asItem());

        // 木质栅栏门物品标签
        addToTag(Tags.Items.FENCE_GATES_WOODEN, fenceGate);

        // 栅栏门物品标签
        addToTag(ItemTags.FENCE_GATES, fenceGate);

        // 木质门物品标签
        addToTag(ItemTags.WOODEN_DOORS,door);

        // 门物品标签
        // bugjump不知道给这玩意更新到哪去了 item没key 难绷
        // [NeedCheck]
        addToTag(BlockItemTags.DOORS.item(), door);

        // 活板门物品标签
        // 续上：我要点名某些明明有包含关系但是就是不引用子tag还要把key删掉的tag
        // [NeedCheck]
        addToTag(BlockItemTags.TRAPDOORS.item(), trapdoor);

        // 木质活板门物品标签
        addToTag(ItemTags.WOODEN_TRAPDOORS, trapdoor);

        // 木质压力板物品标签
        addToTag(ItemTags.WOODEN_PRESSURE_PLATES, pressurePlate);

        // 木质按钮物品标签
        addToTag(ItemTags.WOODEN_BUTTONS, button);

        // 按钮物品标签
        // 不是哥们 你怎么也没了我朝为
        // [NeedCheck]
        addToTag(BlockItemTags.BUTTONS.item(), button);

        // 树叶物品标签
        if (leaves != null) {
            addToTag(ItemTags.LEAVES, leaves);
        }

        // 树苗物品标签
        if (sapling != null) {
            addToTag(ItemTags.SAPLINGS, sapling);
        }
    }

    private void registerWoodLogItemTag(String name, Block log, Block wood, Block strippedLog, Block strippedWood) {
        if (!module.owns(log)) return;
        TagKey<Item> logsTag = TagKey.create(
                Registries.ITEM,
                HFASMod.id(name + "_logs")
        );
        addToTag(logsTag, log, wood, strippedLog, strippedWood);
    }

    @SuppressWarnings("all")
    public void addToTag(TagKey<Item> tag, Block... items){
        if(items.length == 0) return;
        tag(tag).addAll(Arrays.stream(items).map(Block::asItem).map(Item::builtInRegistryHolder).map(Holder.Reference::getKey));
    }

    @SuppressWarnings("all")
    public void addToTag(TagKey<Item> tag, Item... items){
        if(items.length == 0) return;
        tag(tag).addAll(Arrays.stream(items).map(Item::builtInRegistryHolder).map(Holder.Reference::getKey));
    }
}
