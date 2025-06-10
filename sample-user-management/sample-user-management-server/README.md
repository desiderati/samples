# Sample User Management Server

## Commands to run the application

- ./gradlew lintKotlin
- ./gradlew generateSwagger
- ./gradlew test --fail-fast
- ./gradlew build -x test --no-daemon

## Knowledge Base

- A failure occurred while executing org.jetbrains.kotlin.gradle.internal.KaptExecution >
  java.lang.reflect.InvocationTargetException (no error message)

  > Solution: Check if the JDK configured in your development environment matches
  > the one specified in the `build.gradle` file.

---

- No matching variant of `org.springframework.boot:spring-boot-gradle-plugin:3.0.4` was found.
  The consumer was configured to find a runtime of a library compatible with Java 11, packaged as a jar, and its
  dependencies declared externally, as well as attribute `org.gradle.plugin.api-version ` with value `7.5.1`.

  > Solution: Change the Java version used by Gradle within IntelliJ.

---

- Plugin \[id: 'org.jetbrains.kotlin.jvm', version: '2.0.0'\] was not found in any of the following sources.
  Could not initialize SSL context. java.security.NoSuchAlgorithmException: Error constructing implementation
  (algorithm: Default, provider: SunJSSE, class: sun.security.ssl.SSLContextImpl$DefaultSSLContext)

  > Solution: All OpenJDK Java versions installed locally via the APT repository share
  > the same certificate directory — that is, they share the same `cacerts` file, even across older versions of Java.
  > This causes incompatibilities with newer Gradle versions, preventing it from communicating with repositories
  > to download dependencies. To avoid this, the default Java installation from the APT repository
  > should be replaced by a manually installed version.
