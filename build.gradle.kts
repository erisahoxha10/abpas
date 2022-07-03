import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("java")
	id("org.jetbrains.kotlin.jvm") version "1.5.10"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.springframework.boot") version "2.7.1"
	application
//	id("io.spring.dependency-management") version "1.0.11.RELEASE"
//	kotlin("jvm") version "1.6.21"
//	kotlin("plugin.spring") version "1.6.21"
//	kotlin("plugin.jpa") version "1.6.21"
}

group = "com.abpas"
version = "0.0.1-SNAPSHOT"
//java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("mysql:mysql-connector-java:8.0.15")
//	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

//tasks.withType<KotlinCompile> {
//	kotlinOptions {
//		freeCompilerArgs = listOf("-Xjsr305=strict")
//		jvmTarget = "1.8"
//	}
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
//
//tasks.withType<Jar> {
//	manifest {
//		attributes["Main-Class"] = "src.main.kotlin.com.abpas.AbpasApplication"
//	}
//}

application() {
	mainClass.set("com.abpas.AbpasApplication")
}
