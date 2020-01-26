import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.3.0.BUILD-SNAPSHOT"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  val kotlinVersion = "1.3.61"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion
  kotlin("plugin.jpa") version kotlinVersion // TODO NEEDED?
}

group = "com.piotr.tictactoe"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
  mavenCentral()
  maven { url = uri("https://repo.spring.io/milestone") }
  maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
  implementation("mysql:mysql-connector-java:8.0.18")
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-mustache")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("io.jsonwebtoken:jjwt:0.2")
  implementation("org.springframework.boot:spring-boot-starter-security")
  runtimeOnly("com.h2database:h2:1.4.197")
  implementation("joda-time:joda-time:2.10.5")

  implementation("org.springframework.security.oauth:spring-security-oauth2:2.3.0.BUILD-SNAPSHOT")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}
