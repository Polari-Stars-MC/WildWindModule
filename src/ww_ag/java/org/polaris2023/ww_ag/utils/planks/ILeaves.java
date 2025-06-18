package org.polaris2023.ww_ag.utils.planks;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.LeavesBlock;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.common.registrate.entry.PlanksEntry;
import org.polaris2023.ww_ag.utils.ISelf;

/**
 * @author baka4n
 * @code @Date 2025/6/18 23:57:12
 */
public interface ILeaves<E extends WWRegistrate, T extends PlanksEntry<E>> extends ISelf<T> {
    T setLeaves(BlockEntry<LeavesBlock> entry);
}
