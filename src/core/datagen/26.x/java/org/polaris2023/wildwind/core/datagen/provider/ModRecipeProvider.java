package org.polaris2023.wildwind.core.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.polaris2023.wildwind.core.WildWindCore;
import org.polaris2023.wildwind.core.data.ModRecipeSpecs;
import org.polaris2023.wildwind.imp.data.ImpRecipeWriter;

import java.util.concurrent.CompletableFuture;

/**
 * 核心模块的配方生成器（箭矢组合配方暂未启用）。
 *
 * <p>内容逻辑已按 ADR-0005 迁到 {@link ModRecipeSpecs}（纯数据规格，三族共用）；
 * 本类只剩"把该版本的发射上下文交出去"这一层极薄适配。</p>
 *
 * @author baka4n
 * @author Mon-Landis
 * @author mdjfnfn
 * @author safe_saffawa
 */
public class ModRecipeProvider extends RecipeProvider {

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        ImpRecipeWriter.apply(ModRecipeSpecs.recipes(), new ImpRecipeWriter.Sinks() {
            @Override
            public Object output() {
                return output;
            }

            @Override
            public Object items() {
                return items;
            }

            @Override
            public Object has(Object item) {
                // 显式限定外层实例：否则会与匿名类自己的 has(Object) 撞名，
                // 只能依赖"重载解析挑更具体的 ItemLike"这一隐式规则。
                return ModRecipeProvider.this.has((net.minecraft.world.level.ItemLike) item);
            }
        });
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Wild Wind " + WildWindCore.MOD_ID + " Recipe Provider";
        }
    }
}
