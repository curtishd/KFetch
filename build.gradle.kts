plugins {
    kotlin("jvm") version "2.0.21"
}

group = "me.cdh"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-nop:2.0.16")
    implementation("info.picocli:picocli:4.7.6")
    implementation("com.github.oshi:oshi-core:6.6.5")
//    testImplementation(kotlin("test"))
}

//tasks.test {
//    useJUnitPlatform()
//}
kotlin {
    jvmToolchain(21)
}
tasks.jar {
    manifest {
        attributes(
            mapOf("Main-Class" to "me.cdh.Main"))
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks{
    compileJava{
        dependsOn(compileKotlin)
        doFirst{
            options.compilerArgs= listOf(
                "--module-path",classpath.asPath
            )
        }
    }
    compileKotlin {
        destinationDirectory.set(compileJava.get().destinationDirectory)
    }
    jar{
        duplicatesStrategy=DuplicatesStrategy.EXCLUDE
    }
}