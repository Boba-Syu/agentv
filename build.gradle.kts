plugins {
    kotlin("jvm") version "2.2.0"
}

group = "cn.bobasyu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val vertxVersion = "4.5.3"
val jacksonVersion = "2.17.2"
val slf4jVersion = "1.5.6"
val mockitoVersion = "5.12.0"
val ktormVersion = "4.1.1"
val okhttpVersion = "4.12.0"
val snakeyamlVersion = "2.3"
val postgresqlVersion = "42.7.7"
val guavaVersion = "33.4.8-jre"
val langchain4jVersion = "1.2.0"
val langchain4jMcpVersion = "1.2.0-beta8"

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("io.vertx:vertx-unit:${vertxVersion}")
    testImplementation("io.vertx:vertx-junit5:${vertxVersion}")
    implementation("io.vertx:vertx-core:${vertxVersion}")
    implementation("io.vertx:vertx-lang-kotlin:${vertxVersion}")
    implementation("io.vertx:vertx-web:${vertxVersion}")
    implementation("io.vertx:vertx-web-client:${vertxVersion}")
    implementation("io.vertx:vertx-auth-jwt:${vertxVersion}")
    implementation("io.vertx:vertx-auth-common:${vertxVersion}")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:${vertxVersion}")

    implementation("org.ktorm:ktorm-core:${ktormVersion}")
    implementation("org.ktorm:ktorm-jackson:${ktormVersion}")
    implementation("org.ktorm:ktorm-support-postgresql:${ktormVersion}")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")

    implementation("dev.langchain4j:langchain4j:${langchain4jVersion}")
    implementation("dev.langchain4j:langchain4j-ollama:${langchain4jVersion}")
    implementation("dev.langchain4j:langchain4j-mcp:${langchain4jMcpVersion}")

    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")

    implementation("org.yaml:snakeyaml:${snakeyamlVersion}")
    implementation("org.slf4j:slf4j-jdk14:${slf4jVersion}")
    implementation("org.mockito:mockito-core:${mockitoVersion}")
    implementation("com.squareup.okhttp3:okhttp:${okhttpVersion}")
    implementation("com.google.guava:guava:${guavaVersion}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}