package org.polaris2023.ww_ag.common.init;

import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2core.init.reg.simple.Reg;
import net.minecraft.resources.ResourceLocation;
import org.polaris2023.ww_ag.WWAgMod;

/**
 * @author baka4n
 * @code @Date 2025/6/8 22:39:19
 */
public class ModDataComponents {
    public static final DCReg DCREG = DCReg.of(WWAgMod.REG);
    public static final  DCVal<ResourceLocation> MILK_TYPE = DCREG.loc("milk_type");
    public static void register() {}
}
