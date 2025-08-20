/*
 * This project uses @Incubating APIs which are subject to change.
 */

import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

application {
    // Define the main class for the application.
    mainClass = "com.pantrylogger.PantryLoggerBackend"
}

val wiremockVersion: String by project

plugins {
    id("buildlogic.java-application-conventions")
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.sonarqube") version "5.0.0.4638"
    id("jacoco-report-aggregation")
    id("pmd")
    id("checkstyle")
}

version = "0.0.1-SNAPSHOT"

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "checkstyle")
    apply(plugin = "pmd")
    repositories {
        mavenCentral()
    }
    tasks.withType(JavaCompile::class) {
        sourceCompatibility = JavaVersion.VERSION_21.toString()
        options.compilerArgs.add("-Werror")
    }
    sonar {
        properties {
            property("sonar.sources", "src/main")
            property("sonar.tests", "src/test")
        }
    }
    pmd {
        toolVersion = "7.1.0"
        isConsoleOutput = true
    }

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter:5.13.4")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()

        maxHeapSize = "1G"

        testLogging {
            events("passed")
        }
    }

    tasks.withType<Test> {
        finalizedBy(tasks.withType<JacocoReport>())
    }
    tasks.withType<JacocoReport> {
        dependsOn(tasks.withType<Test>())
        reports {
            html.required.set(true)
        }
    }
}

dependencies {
    "implementation"("org.springframework.boot:spring-boot-starter")
    "implementation"("org.springframework.boot:spring-boot-autoconfigure")
    "implementation"("org.springframework.boot:spring-boot")
    "implementation"("org.springframework.boot:spring-boot-configuration-processor")



    "testImplementation"("org.springframework.boot:spring-boot-starter-web")
    "testImplementation"("org.springframework.boot:spring-boot-starter-test")
    "testImplementation"("org.wiremock:wiremock-jetty12:$wiremockVersion")
}

subprojects {
    group = "com.pantrylogger"
    pmd {
        ruleSetFiles = files("../config/pmd.xml")
    }
    checkstyle {
        configFile = file("../config/checkstyle.xml")
    }
    tasks.withType<JacocoReport> {
        reports {
            xml.required.set(false)
        }
    }
}
pmd {
    ruleSetFiles = files("./config/pmd.xml")
}
checkstyle {
    configFile = file("./config/checkstyle.xml")
}
tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
    }
}
sonar {
    properties {
        property("sonar.projectKey", "pantry-logger_backend")
        property("sonar.organization", "pantry-logger")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sourceEncoding", "UTF-8")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "build/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml," +
                    "../build/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml"
        )
        //property("sonar.qualitygate.wait", "true")
    }
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    docker {
        builder.set("paketobuildpacks/builder-jammy-base:latest")
        imageName.set(System.getenv("IMAGE_NAME"))
        tags.set(
            listOf(
                "${System.getenv("IMAGE_NAME")}:${System.getenv("SHORT_SHA")}"
            )
        )
        publish.set(true)
        environment.set(
            mapOf(
                "BP_OCI_SOURCE" to System.getenv("BP_OCI_SOURCE")
            )
        )
        docker {
            publishRegistry {
                url.set(System.getenv("CR_URL"))
                username.set(System.getenv("CR_USERNAME"))
                password.set(System.getenv("CR_PASSWORD"))
            }
        }
    }
}