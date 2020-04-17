import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.2.4.RELEASE"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  val kotlinVersion = "1.3.61"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion
  kotlin("plugin.jpa") version kotlinVersion
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
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  val springBootStarter = "2.2.4.RELEASE"
  implementation("org.springframework.boot:spring-boot-starter:$springBootStarter")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootStarter")
  implementation("org.springframework.boot:spring-boot-starter-mustache:$springBootStarter")
  implementation("org.springframework.boot:spring-boot-starter-web:$springBootStarter")
  implementation("org.springframework.boot:spring-boot-starter-security:$springBootStarter")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf:$springBootStarter")
  implementation("org.springframework.boot:spring-boot-starter-websocket:$springBootStarter")
  testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootStarter") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.2")
  implementation("joda-time:joda-time:2.10.5")
  implementation("org.springframework.security.oauth:spring-security-oauth2:2.4.0.RELEASE")
  implementation("org.postgresql:postgresql:42.2.9")
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
