plugins {
    id("com.airwallex.grpc-spring")
    id("com.squareup.wire")
}

val grpcktplusVersion: String by project
val commonsValidatorVersion: String by project

dependencies {
    implementation("commons-validator:commons-validator:$commonsValidatorVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
}

wire {
    protoPath {
        srcJar("com.airwallex.grpc:mapstruct-protobuf:$grpcktplusVersion")
    }

    // kotlin will throws: "Modifiers [OVERRIDE] are not allowed on Kotlin parameters. Allowed modifiers: [VARARG, NOINLINE, CROSSINLINE]"
    // Seems a bug in wire
    // kotlin {}

    java {}
}
