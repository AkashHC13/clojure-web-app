(ns sample.routes.auth
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [app.models.user :as db]
            [ring.util.response :as resp]))

(defn- invalid-email? [email]
  (or
   (some? (db/get-user-by-email email))
   (not (some? (re-matches #".+\@.+\..+" email)))))

(defn- invalid-password? [password]
  (not (and password (>= (count password) 6))))

(defn- different-password? [password password_confirmation]
  (not (and password password_confirmation (= password password_confirmation))))

(defn validate [{:keys [email password password_confirmation phone course_id]}]
  (cond-> {}
    (invalid-email? email) (assoc :email "Invalid or already exists")
    (invalid-password? password) (assoc :password "Password must be at least 6 characters")
    (different-password? password password_confirmation) (assoc :password_confirmation "Not same")
    (not (some? phone)) (assoc :phone "Please enter valid phone number")
    (not (some? course_id)) (assoc :course_id "None selected")))

(defn handle-login [{:keys [body] :as _request}]
 (let [{:keys [email password]} body
       user (db/get-user-by-email email {:include-password? true})]
   (if (and user (= password (:password user)))
     (resp/response {:status 200
                     :user user})
     {:status 403
      :message "Invalid password"})))

(defn handle-registration [{:keys [body] :as _request}]
  (let [{:keys [email password phone course_id]} body
        validation-errors (validate body)]
    (if (empty? validation-errors)
      (try
        (db/create-user {:email email :password password :phone phone :courses (str course_id)})
        (resp/response {:user (-> (db/get-user-by-email email) (dissoc :password :timestamp))})
        (catch Exception ex
          {:status 503
           :message "Something went wrong"}))
      (resp/response {:status 400
                      :errors validation-errors}))))
