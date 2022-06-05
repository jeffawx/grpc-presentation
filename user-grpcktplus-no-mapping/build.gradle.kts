plugins {
    id("com.airwallex.grpc-spring")
}

val commonsValidatorVersion: String by project

dependencies {
    implementation("commons-validator:commons-validator:$commonsValidatorVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
}
