# web application built with Clojure

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

## Routes
* `/register` : Register a new user (POST)
* `/login` : Login api for a user (POST)
* `/api/user?email=<email-id>` : Find user by email (GET)

## Deploying clojure app using Heroku

https://devcenter.heroku.com/articles/deploying-clojure
