FROM techempower/maven:0.1

ADD ./ /armeria
WORKDIR /armeria
RUN mvn clean compile assembly:single
CMD java \
    -server \
    -XX:+UseNUMA \
    -XX:+UseParallelGC \
    -XX:+AggressiveOpts \
    -jar target/Armeria-0.1-jar-with-dependencies.jar
