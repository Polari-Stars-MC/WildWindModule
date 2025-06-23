package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ILanguage;
import org.polaris2023.ww_ag.utils.ISelf;

/**
 * @author baka4n
 * @code @Date 2025/6/21 11:41:41
 */
public interface IBoat<E extends WWRegistrate, T extends PlanksEntry<E, T>> extends ISelf<T> {
    T setBoat(ItemEntry<BoatItem> entry);
    T setChestBoat(ItemEntry<BoatItem> entry);
    default T boat(NonNullUnaryOperator<Item.Properties> properties) {
        T self = ww_ag$self();
        var b = ILanguage.convert1(self.registrate.item(self.name+"_boat", p -> new BoatItem(false, self.btp.get().getValue(), properties.apply(p))))
                .ww_ag$zh_cn(self.zhCn + "木船")
                .ww_ag$zh_tw(self.zhTw+"木船")
                .ww_ag$zh_hk(self.zhHk+"木船")
                .ww_ag$self()
                .lang(self.firstUpName() + " Boat")
                .tag(ItemTags.BOATS)
                .recipe((c, p) -> {
                    RegistrateRecipeProvider
                            .woodenBoat(p, c.get(), self.planks.get());
                })
                .defaultModel();
        return setBoat(b.register());
    }
    default T boat() {return boat(NonNullUnaryOperator.identity());}
    default T chestBoat(NonNullUnaryOperator<Item.Properties> properties) {
        T self = ww_ag$self();
        var b = ILanguage.convert1(self.registrate.item(self.name+"_chest_boat", p -> new BoatItem(true, self.btp.get().getValue(), properties.apply(p))))
                .ww_ag$zh_cn(self.zhCn + "木运输船")
                .ww_ag$zh_tw(self.zhTw+"木運輸船")
                .ww_ag$zh_hk(self.zhHk+"木運輸船")
                .ww_ag$self()
                .lang(self.firstUpName() + " Chest Boat")
                .tag(ItemTags.CHEST_BOATS)
                .recipe((c, p) -> {
                    RegistrateRecipeProvider.chestBoat(p, c.get(), self.planks.get());
                })
                .defaultModel();
        return setChestBoat(b.register());
    }
    default T chestBoat() {return chestBoat(NonNullUnaryOperator.identity());}
}
