(ns app.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [migratus.core :as migratus]
            [app.routes.profile :as profile]
            [app.routes.auth :as auth]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(def migratus-config
  {:store :database
   :migration-dir "migrations"
   :db (or (System/getenv "DATABASE_URL") "postgresql://localhost:5432/cma")})

(defn init []
  (migratus/migrate migratus-config))

(defn not-found []
  {:status 404
   :message "Oh hoo! Route not found"})

(defn- wrap-exception-handling [handler]
  (fn [{:keys [_headers] :as request}]
    (try
      (handler request)
      (catch Exception e
        (response "Something went wrong. Needs to be looked immediately")))))

(defroutes app-routes
  (POST "/register" [] auth/handle-registration)
  (POST "/login" [] auth/handle-login)
  (POST "/profile/password/update" [current-password new-password confirm-password]
      (profile/update-password current-password new-password confirm-password))
  (GET "/api/user" [] profile/get-user)
  (route/resources "/")
  (route/not-found (not-found)))

(def app
  (-> app-routes
      wrap-exception-handling
      wrap-keyword-params
      wrap-params
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)
      (wrap-defaults api-defaults)))
