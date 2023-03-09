(ns resultful-crud.core
  (:require [toucan.db :as db]
            [toucan.models :as models]
            [ring.adapter.jetty :refer [run-jetty]]
            [compojure.api.sweet :refer [routes]]
            [resultful-crud.user :refer [user-routes]])
  (:gen-class))

;Initialising Toucan

; Toucan is a library that provides a simple interface to the database.

;; Toucan requires us to provide two information to initialise itself

;;   * A database connection specification.
;;   * Toucan requires that all models live in specific predictable namespaces and we have to provide the root namespace where it can find the models.
(def db-spec
  {:dbtype "postgres"
   :dbname "restful-crud"
   :user "postgres"
   :password "postgres"})

(def app (apply routes user-routes))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (db/set-default-db-connection! db-spec)
  (models/set-root-namespace! 'resultful-crud.models)
  (run-jetty app {:port 3000}))
