package org.polaris2023.ww_ag.common.registrate.entry;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.NotNull;
import org.polaris2023.ww_ag.common.registrate.WWRegistrate;
import org.polaris2023.ww_ag.utils.ISelf;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

/**
 * xxx cobblestone xxx圆石（可选）
 * xxx stone xxx石
 * Smooth xxx Stone 平滑xxx石
 * Infested xxx Stone 被虫蚀的xxx石（可选）
 * xxx stone button xxx石按钮
 * xxx stone pressire plate xxx石压力板
 * xxx stone bricks xxx是赚
 * cracked xxx stone bricks 裂纹xxx石砖
 * chiseled xxx stone bricks 苔化xxx石砖
 * stone xxx slab xxx石楼梯
 * cracked xxx stone slab 裂纹xxx石楼梯（可选）
 * chiseled xxx stone slab 苔化xxx石楼梯（可选）
 * xxx stone wall xxx石墙
 * cracked xxx stone wall 裂纹xxx石墙
 * chiseled xxx stone wall 苔化xxx石墙（可选）
 *
 * @author baka4n
 * @code @Date 2025/6/24 22:06:59
 */
@SuppressWarnings({"unchecked", "ClassCanBeRecord"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoneEntry<T extends WWRegistrate, E extends StoneEntry<T, E>> implements
        ISelf<E> {
    public final String name;
    public final T registrate;

    @Setter
    @Accessors(fluent = true)
    public String zhCn,zhTw,zhHk;


    public String firstUpName() {
        return upFirstName(name);
    }

    public static String upFirstName(String name) {
        if (name.isEmpty()) return name;

        char[] chars = name.toCharArray();
        boolean capitalizeNext = true;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '_') {
                chars[i] = ' '; // 直接替换下划线为空格
                capitalizeNext = true;
            } else if (capitalizeNext) {
                chars[i] = Character.toUpperCase(chars[i]);
                capitalizeNext = false;
            }
            // 其他字符保持不变
        }
        return new String(chars);
    }

    public StoneEntry(T registrate, String name) {
        this.name = name;
        this.registrate = registrate;
    }

    @Override
    public E ww_ag$self() {
        return (E) this;
    }
}
