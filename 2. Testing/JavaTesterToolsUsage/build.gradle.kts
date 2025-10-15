plugins {
    java
}

group = "com.example"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 4 для всех тестов
    testImplementation("junit:junit:4.13.2")

    // Cucumber для JUnit 4
    testImplementation("io.cucumber:cucumber-java:7.12.0")
    testImplementation("io.cucumber:cucumber-junit:7.12.0")

    // AssertJ
    testImplementation("org.assertj:assertj-core:3.24.2")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.slf4j:slf4j-api:1.7.36")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

// Конфигурация для ВСЕХ тестов
tasks.test {
    useJUnit()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
    systemProperty("file.encoding", "UTF-8")

}