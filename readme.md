# jua

Jua is a lua interpreter implemented in java.

It features a CLI (repl + interpreter for files) and a web application.

## Usage

To build jua you can run:

`./gradlew assemble`

This will build a jar in `./build/libs/jua-0.0.0.jar`

that you can run doing:

`java -jar ./build/libs/jua-0.0.0.jar --help` (this will prompt you the help of the Jua CLI)

## Tooling

To setup the pre-commit hooks (checks for linting of both the backend and the frontend) you can run:

`./gradlew installGitHooks`

You can also use the scripts in `./scripts`

- `start-front-dev` starts the frontend in development mode
- `build-images` will build docker images for the frontend (`jua/frontend`) and the backend (`jua/backend`)

You can also run the whole stack using [docker-compose](https://docs.docker.com/compose/):

`docker-compose up`