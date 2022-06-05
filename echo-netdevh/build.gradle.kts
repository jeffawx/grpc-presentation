import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    idea
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.google.protobuf")
    id("org.springframework.boot")
}

apply(plugin = "io.spring.dependency-management")

val grpcVersion: String by project
val grpcKotlinVersion: String by project
val protobufVersion: String by project
val grpcNetDevhVersion: String by project

dependencies {
    implementation("net.devh:grpc-spring-boot-starter:$grpcNetDevhVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                create("kotlin")
            }

            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}
