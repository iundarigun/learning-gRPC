import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "1.4.32"
    id("com.google.protobuf") version "0.8.16"
    id("idea")
}

group = "br.com.devcave.grpc"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

val gRPCVersion = "1.38.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.grpc:grpc-netty-shaded:$gRPCVersion")
    implementation("io.grpc:grpc-protobuf:$gRPCVersion")
    implementation("io.grpc:grpc-stub:$gRPCVersion")

    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.12.0"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$gRPCVersion"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

sourceSets.main.get().allSource.srcDir("generated/source")

idea {
    module {
        generatedSourceDirs.plusAssign(file("build/generated/source"))
    }
}