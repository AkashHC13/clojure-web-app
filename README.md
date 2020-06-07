# web application built with Clojure

[![pipeline status](https://gitlab.com/dzaporozhets/clojure-web-application/badges/master/pipeline.svg)](https://gitlab.com/dzaporozhets/clojure-web-application/commits/master)

The goal of this project is to make a blank web application with authentication and tests.
It can be used as an template for starting a new project on Clojure or for learning Clojure.

Merge requests to this project are welcome!

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

Install PostreSQL and create database. Details of database connection now 
are in `src/cma/db.clj`. 

    createdb cma

Run migrations:

    lein migratus migrate

To start a web server for the application, run:

    lein ring server

Now visit http://localhost:3000/ to see the app running.

## Tests
