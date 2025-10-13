plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Используем BOM для управления версиями
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation(platform("io.cucumber:cucumber-bom:7.15.0"))
    testImplementation(platform("org.assertj:assertj-bom:3.24.2"))

    // Основные зависимости Cucumber
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")

    // JUnit Platform
    testImplementation("org.junit.platform:junit-platform-suite")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // AssertJ
    testImplementation("org.assertj:assertj-core")

    // JUnit Jupiter для assertions (важно!)
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("cucumber.junit-platform.naming-strategy", "long")
}