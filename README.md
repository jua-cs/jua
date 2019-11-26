# jua


![Build Status](https://github.com/jua-cs/jua/workflows/Java%20CI/badge.svg?branch=master)

Jua is a lua interpreter implemented in java.

It features a CLI (repl + interpreter for files) and a web application.

You can access the web application [here](http://jua.herokuapp.com)  (disclaimer it won't work in https because it's using heroku's free tier).

## Installation

### Requirements:

- Java 11 or later
- Gradle 5 or later

OR

- [docker](#Docker)

[**SDKMAN!**](https://sdkman.io/) is a good solution to install and manage Gradle, Java SDK, etc...

### Setup

Clone the project:

```bash
git clone git@github.com:jua-cs/jua.git
```

## Usage

To build jua you can run:

`./gradlew assemble`

This will build a jar in `./build/libs/jua-0.0.0.jar`

that you can run doing:

`java -jar ./build/libs/jua-0.0.0.jar --help` (this will prompt you the help of the Jua CLI)

### REPL

To launch a lua REPL you can just do: `java -jar build/libs/jua-0.0.0.jar`

It will prompt you with a REPL where you can write lua code and it will be lexed, parsed and evaluated on the fly.

### File execution

To execute a specific file you can do: `java -jar build/libs/jua-0.0.0.jar <my_file.lua>`, for instance: `java -jar build/libs/jua-0.0.0.jar ./src/test/java/jua/evaluator/testdata/bool.lua`

This will execute the given lua file directly and exit shortly after.

### Execution API

To launch the web server only you can do: `java -jar build/libs/jua-0.0.0.jar --server`, this will start it on port `3000` by default (you can configure it using the `PORT` env variable).

You can then try it with: `curl -XPOST -H 'Content-Type: application/json' -d '{"code": "print(100 * 5)"}' http://localhost:3000/api/v1/interpreter`.

If you have [yarn](https://yarnpkg.com/lang/en/) and [nodejs](https://nodejs.org/en/) installed you can also run the web UI (or access it [here](http://jua.herokuapp.com)) to access this API by execution the `start-front-dev` script located in the `scripts` directory.

### Debug

There is also a `-d`, `--debug` flag that allows you to log what's happening during the different steps (lexing, parsing and evaluation)

## Tooling

To setup the pre-commit hooks (checks for linting of both the backend and the frontend) you can run:

`./gradlew installGitHooks`

You can also use the scripts in `./scripts`

- `start-front-dev` starts the frontend in development mode

### Docker

You can also run the whole stack using [docker](https://www.docker.com/) or [podman](https://podman.io/):

`docker build . -t jua && docker run -p 127.0.0.1:3000:3000 jua`

Then just launch your favorite browser and head to `http://localhost:3000/` to execute some *lua*.

### Documentation

Look a the more comprehensive documention [here](./DOCS.md) !

### Possible Improvements

- Add support for unicode
- Better stack traces
- Implement [coroutines](https://www.lua.org/pil/9.1.html)
- Implement `require` to be able to import libraries
- Implement the remaining builtins, and the missing features
- Improve performances (10x difference for fibonacci computation compared to the [official lua implementation](https://github.com/lua/lua))
- Add a code formatter (it could use the AST to do that)
- Allow to share lua snippets in the web UI
