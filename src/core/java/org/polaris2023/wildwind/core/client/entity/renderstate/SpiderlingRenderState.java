package org.polaris2023.wildwind.core.client.entity.renderstate;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.util.Map;

/**
 * 幼蛛渲染状态喵~
 * <p>
 * GeckoLib 用 mixin 给 {@code EntityRenderState} 注入 {@link GeoRenderState}，编译期不可见，
 * 导致渲染器与图层只能强转。这里显式实现同一契约并自持数据表，让类型在编译期就已成立。
 *
 * @author safe_saffawa
 */
public class SpiderlingRenderState extends LivingEntityRenderState implements GeoRenderState {
    private final Map<DataTicket<?>, Object> geckolibData = new Reference2ObjectOpenHashMap<>();

    @Override
    public <D> void addGeckolibData(DataTicket<D> dataTicket, D data) {
        geckolibData.put(dataTicket, data);
    }

    @Override
    public boolean hasGeckolibData(DataTicket<?> dataTicket) {
        return geckolibData.containsKey(dataTicket);
    }

    @Override
    public Map<DataTicket<?>, Object> getDataMap() {
        return geckolibData;
    }
}
