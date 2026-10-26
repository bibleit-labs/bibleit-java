FROM eclipse-temurin:27-jdk AS verify
WORKDIR /workspace
COPY gradle/ gradle/
COPY gradlew build.gradle.kts settings.gradle.kts gradle.properties ./
RUN ./gradlew --no-daemon dependencies
COPY LICENSE THIRD_PARTY_NOTICES ./
COPY LICENSES/ LICENSES/
COPY src/ src/
COPY contract/ contract/
RUN java -version && ./gradlew --no-daemon build
