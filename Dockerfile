# frontend builder
FROM node:alpine AS front-builder

WORKDIR /web/

COPY ./src/web /web

RUN yarn && yarn build

# Java jar builder
FROM gradle:jdk11 AS jar-builder

WORKDIR /jua/

COPY . .

RUN gradle assemble

# Final image
FROM gradle:jdk11
WORKDIR /root/
COPY --from=jar-builder /jua/build/libs/jua-0.0.0.jar ./jua.jar
COPY --from=front-builder /web/dist/ ./src/main/resources/static/

CMD ["java", "-jar", "jua.jar", "--server"]