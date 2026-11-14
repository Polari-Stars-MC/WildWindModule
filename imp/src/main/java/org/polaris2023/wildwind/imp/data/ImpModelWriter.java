package org.polaris2023.wildwind.imp.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.polaris2023.wildwind.imp.ImpId;
import org.polaris2023.wildwind.imp.ImpIds;
import org.polaris2023.wildwind.imp.annotations.Name;
import org.polaris2023.wildwind.imp.annotations.Since;
import org.polaris2023.wildwind.imp.annotations.Until;

import java.util.List;

/**
 * 版本无关的「模型发射器」（ADR-0005）。
 *
 * <p>模型是差异最大的一类：26.x 用原版新式 {@code BlockModelGenerators}（高层助手齐全），
 * 1.20/1.21 用 NeoForge 的 Forge 风格 {@code BlockStateProvider}（没有等价的木材助手）。
 * 两者由 {@code @Since} / {@code @Until} 分段承载，{@code @Name} 把留存段统一改名。</p>
 *
 * <p><b>现状</b>：26.x 分支经 datagen 产物 diff 验证；旧版分支已按 Forge 风格 API 逐条映射实现，
 * 但目前只能保证<b>编译通过</b>——内容模块尚未支持旧版编译，无法做运行时验证。</p>
 */
public final class ImpModelWriter {

    private ImpModelWriter() {}

    /** 内容模块提供的薄适配：交出该版本的生成器对象。 */
    public interface Generators {
        /** 26.x 为 {@code BlockModelGenerators}；1.20/1.21 为 {@code BlockStateProvider}。 */
        Object blockModels();

        /** 26.x 为 {@code ItemModelGenerators}；1.20/1.21 为 {@code ItemModelProvider}。 */
        Object itemModels();
    }

    /**
     * 按规格逐条发射模型。
     *
     * <p>这里用 {@code instanceof} 链而不是 switch 模式匹配：1.20 族按 Java 17 编译，
     * 而 switch 模式匹配要到 Java 21 才可用。</p>
     */
    public static void apply(List<ImpModelSpec> specs, Generators generators) {
        for (ImpModelSpec spec : specs) {
            if (spec instanceof ImpModelSpec.WoodSet woodSet) {
                emitWoodSet(woodSet, generators);
            } else if (spec instanceof ImpModelSpec.Cube cube) {
                emitCube(cube, generators);
            } else if (spec instanceof ImpModelSpec.StoneSet stoneSet) {
                emitStoneSet(stoneSet, generators);
            } else if (spec instanceof ImpModelSpec.Cross cross) {
                emitCross(cross, generators);
            } else if (spec instanceof ImpModelSpec.DoublePlant doublePlant) {
                emitDoublePlant(doublePlant, generators);
            } else if (spec instanceof ImpModelSpec.BuiltinDoublePlant builtinDoublePlant) {
                emitBuiltinDoublePlant(builtinDoublePlant, generators);
            } else if (spec instanceof ImpModelSpec.VanillaLike vanillaLike) {
                emitVanillaLike(vanillaLike, generators);
            } else if (spec instanceof ImpModelSpec.FlatItem flatItem) {
                emitFlatItem(flatItem, generators);
            } else {
                throw new IllegalArgumentException("未支持的模型规格：" + spec);
            }
        }
    }

    // ── 26.x：原版新式生成器 ─────────────────────────────────────────────

    @Since("26.1")
    @Name("emitWoodSet")
    @SuppressWarnings("unchecked")
    private static void emitWoodSetModern(ImpModelSpec.WoodSet w, Generators generators) {
        var blocks = (net.minecraft.client.data.models.BlockModelGenerators) generators.blockModels();

        blocks.woodProvider(block(w.log())).logWithHorizontal(block(w.log())).wood(block(w.wood()));
        blocks.woodProvider(block(w.strippedLog()))
            .logWithHorizontal(block(w.strippedLog()))
            .wood(block(w.strippedWood()));

        blocks.family(block(w.planks()))
            .stairs(block(w.stairs()))
            .slab(block(w.slab()))
            .fence(block(w.fence()))
            .fenceGate(block(w.fenceGate()))
            .pressurePlate(block(w.pressurePlate()))
            .button(block(w.button()));

        blocks.createDoor(block(w.door()));
        blocks.createTrapdoor(block(w.trapdoor()));

        if (w.hasLeaves()) {
            blocks.createTintedLeaves(block(w.leaves()),
                net.minecraft.client.data.models.model.TexturedModel.LEAVES, w.leavesTint());
        }
    }

    /**
     * 六面方块：方块状态 + 物品模型共用同一个 {@code cube_all} 模型。
     *
     * <p><b>与原 {@code MissingModels.cube} 逐句等价</b>：贴图缺失时也照样引用
     * {@code <namespace>:block/<path>}，客户端显示 missingno 而不是 datagen 报错。</p>
     */
    @Since("26.1")
    @Name("emitCube")
    @SuppressWarnings("unchecked")
    private static void emitCubeModern(ImpModelSpec.Cube spec, Generators generators) {
        var blocks = (net.minecraft.client.data.models.BlockModelGenerators) generators.blockModels();
        Block subject = block(spec.block());
        var model = net.minecraft.client.data.models.model.ModelTemplates.CUBE_ALL.create(
            subject,
            net.minecraft.client.data.models.model.TextureMapping.cube(subject),
            blocks.modelOutput);
        blocks.blockStateOutput.accept(net.minecraft.client.data.models.BlockModelGenerators
            .createSimpleBlock(subject, net.minecraft.client.data.models.BlockModelGenerators.plainVariant(model)));
        blocks.registerSimpleItemModel(subject.asItem(), model);
    }

    /**
     * 石料套件：基方块 + 可选楼梯 / 台阶 / 墙。
     *
     * <p><b>与原 {@code MissingModels.stoneSet} 逐句等价</b>：用
     * {@code new BlockFamilyProvider(TextureMapping.cube(base))} 再
     * {@code fullBlock(base, CUBE_ALL)}，family 成员会自行登记物品模型。</p>
     */
    @Since("26.1")
    @Name("emitStoneSet")
    @SuppressWarnings("unchecked")
    private static void emitStoneSetModern(ImpModelSpec.StoneSet spec, Generators generators) {
        var blocks = (net.minecraft.client.data.models.BlockModelGenerators) generators.blockModels();
        Block base = block(spec.base());
        net.minecraft.client.data.models.BlockModelGenerators.BlockFamilyProvider provider =
            blocks.new BlockFamilyProvider(net.minecraft.client.data.models.model.TextureMapping.cube(base))
                .fullBlock(base, net.minecraft.client.data.models.model.ModelTemplates.CUBE_ALL);
        if (spec.stairs() != null) {
            provider.stairs(block(spec.stairs()));
        }
        if (spec.slab() != null) {
            provider.slab(block(spec.slab()));
        }
        if (spec.wall() != null) {
            provider.wall(block(spec.wall()));
        }
    }

    @Since("26.1")
    @Name("emitCross")
    @SuppressWarnings("unchecked")
    private static void emitCrossModern(ImpModelSpec.Cross spec, Generators generators) {
        var blocks = (net.minecraft.client.data.models.BlockModelGenerators) generators.blockModels();
        // 与原 MissingModels.cross 一致：显式传 TextureMapping.cross(block)（三参重载）
        Block subject = block(spec.block());
        blocks.createCrossBlockWithDefaultItem(subject,
            net.minecraft.client.data.models.BlockModelGenerators.PlantType.NOT_TINTED,
            net.minecraft.client.data.models.model.TextureMapping.cross(subject));
    }

    @Since("26.1")
    @Name("emitDoublePlant")
    @SuppressWarnings("unchecked")
    private static void emitDoublePlantModern(ImpModelSpec.DoublePlant spec, Generators generators) {
        var blocks = (net.minecraft.client.data.models.BlockModelGenerators) generators.blockModels();
        Block subject = block(spec.block());
        var model = net.minecraft.client.data.models.model.ModelTemplates.CROSS.create(
            subject,
            net.minecraft.client.data.models.model.TextureMapping.cross(subject),
            blocks.modelOutput);
        var variant = net.minecraft.client.data.models.BlockModelGenerators.plainVariant(model);
        blocks.createDoubleBlock(subject, variant, variant);
        blocks.registerSimpleItemModel(subject.asItem(), model);
    }

    @Since("26.1")
    @Name("emitBuiltinDoublePlant")
    @SuppressWarnings("unchecked")
    private static void emitBuiltinDoublePlantModern(ImpModelSpec.BuiltinDoublePlant spec, Generators generators) {
        var blocks = (net.minecraft.client.data.models.BlockModelGenerators) generators.blockModels();
        blocks.createDoublePlantWithDefaultItem(block(spec.block()),
            net.minecraft.client.data.models.BlockModelGenerators.PlantType.NOT_TINTED);
    }

    @Since("26.1")
    @Name("emitVanillaLike")
    @SuppressWarnings("unchecked")
    private static void emitVanillaLikeModern(ImpModelSpec.VanillaLike spec, Generators generators) {
        var blocks = (net.minecraft.client.data.models.BlockModelGenerators) generators.blockModels();
        Block subject = block(spec.block());
        var vanillaModel = net.minecraft.client.data.models.model.ModelLocationUtils
            .getModelLocation(block(spec.vanillaBlock()));
        blocks.blockStateOutput.accept(net.minecraft.client.data.models.BlockModelGenerators
            .createSimpleBlock(subject, net.minecraft.client.data.models.BlockModelGenerators.plainVariant(vanillaModel)));
        blocks.registerSimpleItemModel(subject.asItem(), vanillaModel);
    }

    @Since("26.1")
    @Name("emitFlatItem")
    private static void emitFlatItemModern(ImpModelSpec.FlatItem spec, Generators generators) {
        var items = (net.minecraft.client.data.models.ItemModelGenerators) generators.itemModels();
        items.generateFlatItem(item(spec.item()),
            net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM);
    }

    // ── 1.20 / 1.21：NeoForge Forge 风格生成器 ────────────────────────────
    //
    // 旧版没有等价的木材助手（woodProvider / family / createTintedLeaves），这里逐条映射到
    // BlockStateProvider 的 logBlock / axisBlock / stairsBlock / slabBlock / fenceBlock /
    // fenceGateBlock / pressurePlateBlock / buttonBlock / doorBlock / trapdoorBlock，
    // 贴图路径按约定从方块 ID 推导（<namespace>:block/<path>）。
    //
    // 另外旧版的这些助手**都不生成物品模型**（只有 simpleBlockWithItem / simpleBlockItem 例外），
    // 所以每条分支都要自己补 itemModels()。
    //
    // 注意：内容模块目前还不能按旧版编译，因此这段代码只有"编译通过"这一层保证，
    // 没有运行时 / 产物验证。等旧版内容迁移时再补 diff。

    @Until("26.1")
    @Name("emitWoodSet")
    private static void emitWoodSetLegacy(ImpModelSpec.WoodSet w, Generators generators) {
        var blocks = (net.neoforged.neoforge.client.model.generators.BlockStateProvider) generators.blockModels();
        var items = (net.neoforged.neoforge.client.model.generators.ItemModelProvider) generators.itemModels();
        var models = blocks.models();

        // 原木 / 去皮原木：柱状模型，侧面用自身贴图、端面用 _top
        blocks.logBlock((net.minecraft.world.level.block.RotatedPillarBlock) block(w.log()));
        blocks.logBlock((net.minecraft.world.level.block.RotatedPillarBlock) block(w.strippedLog()));

        // 木头 / 去皮木头：三个轴向都用同一个模型，且侧面与端面都用原木贴图
        // （26.x 的 WoodProvider#wood 就是这样产出的：end 与 side 同为 <log> 贴图）
        var woodModel = models.cubeColumn(w.wood().path(), blockLoc(w.log()), blockLoc(w.log()));
        blocks.axisBlock((net.minecraft.world.level.block.RotatedPillarBlock) block(w.wood()), woodModel, woodModel);
        var strippedWoodModel = models.cubeColumn(w.strippedWood().path(),
            blockLoc(w.strippedLog()), blockLoc(w.strippedLog()));
        blocks.axisBlock((net.minecraft.world.level.block.RotatedPillarBlock) block(w.strippedWood()),
            strippedWoodModel, strippedWoodModel);

        // 木板及其构件：统一使用木板贴图
        var planksTexture = blockLoc(w.planks());
        blocks.simpleBlockWithItem(block(w.planks()), models.cubeAll(w.planks().path(), planksTexture));
        blocks.stairsBlock((net.minecraft.world.level.block.StairBlock) block(w.stairs()), planksTexture);
        blocks.slabBlock((net.minecraft.world.level.block.SlabBlock) block(w.slab()),
            blockLoc(w.planks()), planksTexture);
        blocks.fenceBlock((net.minecraft.world.level.block.FenceBlock) block(w.fence()), planksTexture);
        blocks.fenceGateBlock((net.minecraft.world.level.block.FenceGateBlock) block(w.fenceGate()), planksTexture);
        blocks.pressurePlateBlock((net.minecraft.world.level.block.PressurePlateBlock) block(w.pressurePlate()),
            planksTexture);
        blocks.buttonBlock((net.minecraft.world.level.block.ButtonBlock) block(w.button()), planksTexture);
        blocks.doorBlock((net.minecraft.world.level.block.DoorBlock) block(w.door()),
            blockLoc(w.door(), "_bottom"), blockLoc(w.door(), "_top"));
        // orientable=false：与 26.x createTrapdoor 产出的 template_trapdoor_* 一致
        blocks.trapdoorBlock((net.minecraft.world.level.block.TrapDoorBlock) block(w.trapdoor()),
            blockLoc(w.trapdoor()), false);

        if (w.hasLeaves()) {
            blocks.simpleBlock(block(w.leaves()), models.leaves(w.leaves().path(), blockLoc(w.leaves())));
        }

        // 物品模型：以上助手都不生成，逐条补
        itemFromBlockModel(items, w.log());
        itemFromBlockModel(items, w.wood());
        itemFromBlockModel(items, w.strippedLog());
        itemFromBlockModel(items, w.strippedWood());
        itemFromBlockModel(items, w.stairs());
        itemFromBlockModel(items, w.slab());
        itemFromBlockModel(items, w.fenceGate());
        itemFromBlockModel(items, w.pressurePlate());
        // 活板门物品图标用下半模型（与 26.x 一致）
        itemFromBlockModel(items, w.trapdoor(), "_bottom");

        // 栅栏 / 按钮的物品模型指向专门的 inventory 方块模型，得先把它造出来
        models.fenceInventory(w.fence().path() + "_inventory", planksTexture);
        itemFromBlockModel(items, w.fence(), "_inventory");
        models.buttonInventory(w.button().path() + "_inventory", planksTexture);
        itemFromBlockModel(items, w.button(), "_inventory");

        // 门沿用物品栏贴图（与原版门一致）
        items.basicItem(block(w.door()).asItem());

        if (w.hasLeaves()) {
            itemFromBlockModel(items, w.leaves());
        }
    }

    @Until("26.1")
    @Name("emitCube")
    private static void emitCubeLegacy(ImpModelSpec.Cube spec, Generators generators) {
        var blocks = (net.neoforged.neoforge.client.model.generators.BlockStateProvider) generators.blockModels();
        ImpId id = spec.block();
        // cubeAll 的贴图就是 blockTexture(block) = <namespace>:block/<path>，与 26.x 分支一致
        blocks.simpleBlockWithItem(block(id), blocks.models().cubeAll(id.path(), blockLoc(id)));
    }

    @Until("26.1")
    @Name("emitStoneSet")
    private static void emitStoneSetLegacy(ImpModelSpec.StoneSet spec, Generators generators) {
        var blocks = (net.neoforged.neoforge.client.model.generators.BlockStateProvider) generators.blockModels();
        var items = (net.neoforged.neoforge.client.model.generators.ItemModelProvider) generators.itemModels();
        ImpId base = spec.base();
        var baseTexture = blockLoc(base);

        blocks.simpleBlockWithItem(block(base), blocks.models().cubeAll(base.path(), baseTexture));

        if (spec.stairs() != null) {
            blocks.stairsBlock((net.minecraft.world.level.block.StairBlock) block(spec.stairs()), baseTexture);
            itemFromBlockModel(items, spec.stairs());
        }
        if (spec.slab() != null) {
            // 双台阶沿用基方块模型
            blocks.slabBlock((net.minecraft.world.level.block.SlabBlock) block(spec.slab()),
                blockLoc(base), baseTexture);
            itemFromBlockModel(items, spec.slab());
        }
        if (spec.wall() != null) {
            blocks.wallBlock((net.minecraft.world.level.block.WallBlock) block(spec.wall()), baseTexture);
            itemFromBlockModel(items, spec.wall());
        }
    }

    @Until("26.1")
    @Name("emitCross")
    private static void emitCrossLegacy(ImpModelSpec.Cross spec, Generators generators) {
        var blocks = (net.neoforged.neoforge.client.model.generators.BlockStateProvider) generators.blockModels();
        var items = (net.neoforged.neoforge.client.model.generators.ItemModelProvider) generators.itemModels();
        ImpId id = spec.block();

        blocks.simpleBlock(block(id), blocks.models().cross(id.path(), blockLoc(id)));
        // 26.x 的默认物品模型是"用方块贴图生成的平面物品"，layer0 取 block/<path>
        items.withExistingParent(id.path(), "item/generated").texture("layer0", blockLoc(id));
    }

    @Until("26.1")
    @Name("emitDoublePlant")
    private static void emitDoublePlantLegacy(ImpModelSpec.DoublePlant spec, Generators generators) {
        var blocks = (net.neoforged.neoforge.client.model.generators.BlockStateProvider) generators.blockModels();
        var items = (net.neoforged.neoforge.client.model.generators.ItemModelProvider) generators.itemModels();
        ImpId id = spec.block();

        // 上下两半共用同一个自定义模型（与 26.x createDoubleBlock 的两份同模型一致）
        var model = blocks.models().cross(id.path(), blockLoc(id));
        blocks.getVariantBuilder(block(id))
            .forAllStates(state -> new net.neoforged.neoforge.client.model.generators.ConfiguredModel[] {
                new net.neoforged.neoforge.client.model.generators.ConfiguredModel(model)
            });

        itemFromBlockModel(items, id);
    }

    @Until("26.1")
    @Name("emitBuiltinDoublePlant")
    private static void emitBuiltinDoublePlantLegacy(ImpModelSpec.BuiltinDoublePlant spec, Generators generators) {
        var blocks = (net.neoforged.neoforge.client.model.generators.BlockStateProvider) generators.blockModels();
        var items = (net.neoforged.neoforge.client.model.generators.ItemModelProvider) generators.itemModels();
        ImpId id = spec.block();

        // 内置助手会生成 _bottom / _top 两个模型，这里照做
        var bottom = blocks.models().cross(id.path() + "_bottom", blockLoc(id, "_bottom"));
        var top = blocks.models().cross(id.path() + "_top", blockLoc(id, "_top"));
        blocks.getVariantBuilder(block(id)).forAllStates(state -> {
            boolean upper = state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties
                .DOUBLE_BLOCK_HALF) == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;
            return new net.neoforged.neoforge.client.model.generators.ConfiguredModel[] {
                new net.neoforged.neoforge.client.model.generators.ConfiguredModel(upper ? top : bottom)
            };
        });

        // 物品模型用上半部分贴图（与 26.x registerSimpleFlatItemModel(block, "_top") 一致）
        items.withExistingParent(id.path(), "item/generated").texture("layer0", blockLoc(id, "_top"));
    }

    @Until("26.1")
    @Name("emitVanillaLike")
    private static void emitVanillaLikeLegacy(ImpModelSpec.VanillaLike spec, Generators generators) {
        var blocks = (net.neoforged.neoforge.client.model.generators.BlockStateProvider) generators.blockModels();
        var items = (net.neoforged.neoforge.client.model.generators.ItemModelProvider) generators.itemModels();
        ImpId vanilla = spec.vanillaBlock();

        var vanillaModel = blocks.models().getExistingFile(blockLoc(vanilla));
        blocks.simpleBlock(block(spec.block()), vanillaModel);
        // 物品模型同样指向原版方块模型
        items.withExistingParent(spec.block().path(), vanilla.namespace() + ":block/" + vanilla.path());
    }

    @Until("26.1")
    @Name("emitFlatItem")
    private static void emitFlatItemLegacy(ImpModelSpec.FlatItem spec, Generators generators) {
        var items = (net.neoforged.neoforge.client.model.generators.ItemModelProvider) generators.itemModels();
        items.basicItem(item(spec.item()));
    }

    // ── 旧版辅助（只在 @Until("26.1") 的产物里存在）───────────────────────

    /** 约定：方块贴图路径是 {@code <namespace>:block/<path>}。 */
    @Until("26.1")
    private static net.minecraft.resources.ResourceLocation blockLoc(ImpId id) {
        return blockLoc(id, "");
    }

    /** 约定：带后缀的方块贴图路径是 {@code <namespace>:block/<path><suffix>}。 */
    @Until("26.1")
    private static net.minecraft.resources.ResourceLocation blockLoc(ImpId id, String suffix) {
        return ImpIds.id(id.namespace(), "block/" + id.path() + suffix);
    }

    /** 物品模型直接引用同名方块模型 {@code <namespace>:block/<path>}。 */
    @Until("26.1")
    private static void itemFromBlockModel(
            net.neoforged.neoforge.client.model.generators.ItemModelProvider items, ImpId id) {
        itemFromBlockModel(items, id, "");
    }

    /** 物品模型引用 {@code <namespace>:block/<path><suffix>}。 */
    @Until("26.1")
    private static void itemFromBlockModel(
            net.neoforged.neoforge.client.model.generators.ItemModelProvider items, ImpId id, String suffix) {
        items.withExistingParent(id.path(), id.namespace() + ":block/" + id.path() + suffix);
    }

    // ── 注册表解析（两代 API 的名称差异在这里收敛）────────────────────────

    @Since("26.1")
    @Name("block")
    private static Block blockModern(ImpId id) {
        return BuiltInRegistries.BLOCK.getValue(id.unwrap());
    }

    @Until("26.1")
    @Name("block")
    private static Block blockLegacy(ImpId id) {
        return BuiltInRegistries.BLOCK.get(id.unwrap());
    }

    @Since("26.1")
    @Name("item")
    private static Item itemModern(ImpId id) {
        return BuiltInRegistries.ITEM.getValue(id.unwrap());
    }

    @Until("26.1")
    @Name("item")
    private static Item itemLegacy(ImpId id) {
        return BuiltInRegistries.ITEM.get(id.unwrap());
    }
}
