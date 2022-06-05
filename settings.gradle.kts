pluginManagement {
    val kotlinVersion: String by settings
    val protobufPluginVersion: String by settings
    val springBootVersion: String by settings
    val grpcktplusVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("com.google.protobuf") version protobufPluginVersion
        id("org.springframework.boot") version springBootVersion
        id("com.airwallex.grpc-spring") version grpcktplusVersion
    }

    repositories {
        mavenLocal()
        maven("https://artistry.airwallex.com/repository/lib-release/libs-release-local")
        gradlePluginPortal()
    }
}

rootProject.name = "grpc-demo"

include("echo-netdevh", "echo-grpcktplus", "user-netdevh", "user-grpcktplus", "user-grpcktplus-no-mapping")
