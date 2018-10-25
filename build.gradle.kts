import com.google.protobuf.gradle.ExecutableLocator
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    base
    java
    application
    idea
    id("com.google.protobuf") version "0.8.6"
    kotlin("jvm") version "1.2.71"
}

repositories {
    maven("http://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
    jcenter()
}

idea {
    // avoid idea build into "out" dirs
    module {
        outputDir = file("$buildDir/classes/java/main")
        testOutputDir = file("$buildDir/classes/java/test")
    }
}

application {
    group = "one.realme"
    version = "0.1.0"
    mainClassName = "one.realme.app.App"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

protobuf {
    protobuf.generatedFilesBaseDir = "src"
    protobuf.protoc(closureOf<ExecutableLocator> {
        artifact = "com.google.protobuf:protoc:3.6.1"
    })
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.netty:netty-all:4.1.30.Final")
    implementation("com.google.guava:guava:26.0-jre")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.bouncycastle:bcprov-jdk15on:1.60")
    implementation("org.rocksdb:rocksdbjni:5.15.10")
    implementation("com.github.ajalt:clikt:1.5.0")
    implementation("com.github.ajalt:mordant:1.2.0")
    implementation("com.google.protobuf:protobuf-java:3.6.1")

    testImplementation("com.typesafe:config:1.3.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.30.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks {
    // todo generate proto by us or protobuf gradle plugin?
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<Jar> {
        baseName = "realme_one"
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtime.map { if (it.isDirectory) it else zipTree(it) })
    }
}
