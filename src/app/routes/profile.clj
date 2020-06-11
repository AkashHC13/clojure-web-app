(ns app.routes.profile
  (:require [compojure.core :refer :all]
            [ring.util.response :as resp]
            [app.helpers :refer :all]
            [app.models.user :as db]))

(defn update-password [new-password confirm-password]
  (if (= new-password confirm-password)
    (do
      (db/update-user (:id 1) {:password new-password})
      (resp/redirect "/login"))
    (str "Confirmation password does not match"))
  (str "Incorrect current password"))

(defn get-user [{:keys [params] :as _request}]
  (try
    (let [user (-> (db/get-user-by-email (:email params))
                   (dissoc :password :timestamp))]
      (if user
        (resp/response {:user user})
        (resp/status (resp/response "User not found") 404)))
    (catch Exception e
      (resp/status (resp/response "User not found") 404))))
