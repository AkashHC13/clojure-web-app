(ns app.routes.auth
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
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
  (cond-> ""
   (invalid-email? email) (str"Invalid or already exists,")
   (invalid-password? password) (str "Password must be at least 6 characters,")
   (different-password? password password_confirmation) (str "Not same,")
   (not (some? phone)) (str "Please enter valid phone number,")
   (not (some? course_id)) (str "None selected")))

(defn handle-login [{:keys [body] :as _request}]
 (let [{:keys [email password]} body
       user (db/get-user-by-email email {:include-password? true})]
   (if (and user (= password (:password user)))
     (resp/response {:user user})
     (resp/status (resp/response {:error "Invalid username or password"}) 403))))

(defn handle-registration [{:keys [body] :as _request}]
  (let [{:keys [email password phone course_id]} body
        validation-errors (validate body)]
    (if (empty? validation-errors)
      (try
        (db/create-user {:email email :password password :phone phone :courses (str course_id)})
        (resp/response {:user (-> (db/get-user-by-email email) (dissoc :password :timestamp))})
        (catch Exception _ex
          (resp/status (resp/response "Something went wrong") 503)))
      (resp/status (resp/response validation-errors) 422))))
