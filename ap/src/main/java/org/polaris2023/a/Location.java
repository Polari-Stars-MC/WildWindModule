package org.polaris2023.a;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author baka4n
 * @code @Date 2025/6/14 14:56:57
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Location {
    String value();//modid
}
