FROM node:alpine as builder

WORKDIR /web

COPY ./src/web .

RUN yarn
RUN yarn build

FROM nginx:alpine

COPY --from=builder /web/dist /usr/share/nginx/html