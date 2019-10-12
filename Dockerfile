# frontend builder
FROM node:alpine AS front-builder

WORKDIR /web/

COPY ./src/web /web

RUN yarn && yarn build

# Java jar builder
FROM gradle:jdk11 AS jar-builder

WORKDIR /jua/

COPY . .
COPY --from=front-builder /web/dist/ /jua/src/main/resources/static/

RUN gradle assemble

# Final image
FROM openjdk:11.0.4-jre
WORKDIR /root/
COPY --from=jar-builder /jua/build/libs/jua-0.0.0.jar ./jua.jar

CMD ["java", "-jar", "jua.jar", "--server"]