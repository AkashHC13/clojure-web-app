(ns app.helpers
  (:require [compojure.core :refer :all]
            [app.models.user :as user-db]
            [hiccup.form :refer :all]
            [hiccup.element :refer :all]))

(defn error-item [[error]]
  [:div.text-danger error])
