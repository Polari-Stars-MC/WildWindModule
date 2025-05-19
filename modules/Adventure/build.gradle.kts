import org.polaris2023.mcmeta.extension.forge.ForgeLikeDependency
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeDependency
import org.polaris2023.mcmeta.extension.forge.neo.NeoForgeModsToml

dependencies {
    implementation(project(":Agricultural"))
    implementation(jarJar(
        group = "dev.xkmc",
        name = "fast_projectile_api",
        version = "[3.0.5,)"
    ))
}

val modId: String by project

configure<NeoForgeModsToml> {
    val modDev = dependencies().get()[modId]!!.toMutableList()
    modDev.add(NeoForgeDependency
        .builder()
        .type(NeoForgeDependency.Type.required)
        .like(ForgeLikeDependency
            .builder()
            .modId("ww_ag")
            .side(ForgeLikeDependency.Side.BOTH)
            .ordering(ForgeLikeDependency.Order.NONE)
            .versionRange("[0,)")
            .build())
        .build())
    dependencies().put(modId, modDev.toTypedArray())
}