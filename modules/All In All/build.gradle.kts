val projectNames = listOf(
    "Deco",
    "Adventure",
    "Agricultural",
    "Vanilla Plus Plus"
)

dependencies {
    for (projectName in projectNames) {
        implementation(project(":$projectName"))
        jarJar(project(":$projectName"))
    }
}