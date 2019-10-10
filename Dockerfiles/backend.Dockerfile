FROM gradle:jdk11 AS builder

WORKDIR /jua/

COPY . .

RUN gradle assemble

FROM gradle:jdk11
WORKDIR /root/
COPY --from=builder /jua/build/libs/jua-0.0.0.jar ./jua.jar

CMD ["java", "-jar", "jua.jar", "--server"]
