# Example of custom Java runtime using jlink in a multi-stage container build

FROM container-registry.oracle.com/java/openjdk:21-oraclelinux8 as jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
	--add-modules java.base,java.compiler,java.desktop,java.instrument,java.net.http,java.prefs,java.rmi,java.scripting,java.security.jgss,java.sql.rowset,jdk.jfr,jdk.management,jdk.management.agent,jdk.jcmd,jdk.jstatd,jdk.net,jdk.unsupported \
	--no-man-pages \
	--no-header-files \
	--compress=2 \
	--output /javaruntime


# Define your base image
FROM oraclelinux:8-slim

ENV JAVA_HOME /usr/java/openjdk-21
ENV PATH $JAVA_HOME/bin:$PATH

COPY --from=jre-build /javaruntime $JAVA_HOME

# Continue with your application deployment
COPY ./target/spring-todo-app.jar /app.jar
COPY entrypoint.sh /entrypoint.sh
COPY myprofile.jfc myprofile.jfc

RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chmod +x /entrypoint.sh
USER appuser
EXPOSE 1099

ENV JDK_JAVA_OPTIONS "--enable-preview"

CMD ["/entrypoint.sh"]
