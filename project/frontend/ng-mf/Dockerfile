# Stage 1
FROM node:16-alpine as build-step

ARG APP_NAME
ENV APP_NAME ${APP_NAME}

RUN mkdir -p /app
COPY . /app
WORKDIR /app
# COPY ["decorate-angular-cli.js", "package*.json", "/app/"]
RUN npm i
CMD npx nx serve ${APP_NAME} --host=0.0.0.0 --disable-host-check