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
      (resp/response (if user
                       {:status 200
                        :user user}
                       {:status 404
                        :user {}})))
    (catch Exception e
      (resp/response {:status 404
                      :body "User not found"}))))
