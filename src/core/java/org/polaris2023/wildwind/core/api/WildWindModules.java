package org.polaris2023.wildwind.core.api;
import java.util.Set;
public final class WildWindModules {
    private static final Set<String> IDS = Set.of("wild_wind_core", "wild_wind_wilderness", "wild_wind_barren");
    private WildWindModules() {}
    public static boolean owns(String namespace) { return IDS.contains(namespace); }
}
