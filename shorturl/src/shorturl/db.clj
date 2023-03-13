(ns shorturl.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :refer :all]))


;; https://github.com/clojure/java.jdbc
;; https://github.com/seancorfield/honeysql

(def mysql-db {:dbtype "mysql"
               :dbname "shorturl"
               :user "root"
               :password "password"})

(defn query [q]
  (j/query mysql-db  q))

(defn insert! [q]
  (j/db-do-prepared mysql-db  q))

(defn insert-redirect! [slug url]
  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values [[slug url]])
               (sql/format))))

(defn get-url [slug]
  (let [q (-> (select :url)
              (from :redirects)
              (where [:= :slug slug])
              (sql/format))
        row (first (query  q))]
    (get row :url)))


;; OR use this instead of the above
;; (defn get-url [slug]
;;   (-> (query (-> (select :*)
;;                  (from :redirects)
;;                  (where [:= :slug slug])
;;                  (sql/format)))
;;       first
;;       :url)) 



(comment

;;   (defn create-tables []
;;     (j/query mysql-db
;;              ["CREATE TABLE IF NOT EXISTS redirects (
;;               id INT NOT NULL AUTO_INCREMENT,
;;               short VARCHAR(255) NOT NULL,
;;               long VARCHAR(255) NOT NULL,
;;               PRIMARY KEY (id)
;;             )"]))

  (query (-> (select :*)
             (from :redirects)
             (sql/format)))

  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values [["abc" "https://www.youtube.com/watch?v=0mrguRPgCzI"]])
               (sql/format)))

  (insert-redirect! "axyz" "https://www.youtube.com/watch?v=0mrguRPgCzI")

  (get-url "axyz"))