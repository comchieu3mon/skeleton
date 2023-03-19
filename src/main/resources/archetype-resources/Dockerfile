FROM ghcr.io/thg-harveynash/sr-java-based-image:v1.0.6

VOLUME /tmp

# Add Spring Boot app.jar to Container
ADD "target/*.jar" app.jar

ARG JAVA_OPTS

# Fire up our Spring Boot app by default
CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]