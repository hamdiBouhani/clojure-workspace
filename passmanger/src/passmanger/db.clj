(ns passmanger.db
  (:require [babashka.pods :as pods]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [babashka.fs :as fs]))

(pods/load-pod 'org.babashka/go-sqlite3 "0.1.0")

(require '[pod.babashka.go-sqlite3 :as sqlite3])

(def dbname "passmanger.db")

(defn create-db! []
  (when (not (fs/exists? dbname))
    (sqlite3/execute! dbname
                      (->
                       (h/create-table :passwords)
                       (h/with-columns [[:url :text]
                                        [:username :text]
                                        [[:unique nil :url :username]]])
                       (sql/format)))))


(defn insert-password [url username]
  (sqlite3/execute! dbname
                    (->
                     (h/insert-into :passwords)
                     (h/columns :url :username)
                     (h/values [[url username]])
                     (sql/format {:pretty true}))))


(defn list-passwords []
  (sqlite3/query dbname
                   (->
                    (h/select :url :username)
                    (h/from :passwords)
                    (sql/format {:pretty true}))))

(comment
  (insert-password "facebook.com" "hamdi-pw")
  (list-passwords)
  (create-db!)
  (fs/exists? dbname))




