FROM eclipse-temurin:27-jdk AS verify
WORKDIR /workspace
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw --batch-mode --no-transfer-progress dependency:go-offline
COPY LICENSE THIRD_PARTY_NOTICES ./
COPY LICENSES/ LICENSES/
COPY src/ src/
COPY contract/ contract/
RUN java -version && ./mvnw --batch-mode --no-transfer-progress verify
