(ns vid9.core
  (:require [xtdb.api :as xt]))

;; https://docs.xtdb.com/storage/jdbc/

(def node (xt/start-node {:xtdb.jdbc/connection-pool {:dialect {:xtdb/module 'xtdb.jdbc.mysql/->dialect}
                                                      :db-spec {:host "localhost"
                                                                :dbname "xtdbdb"
                                                                :user "root"
                                                                :password "password"}}
                          :xtdb/tx-log {:xtdb/module 'xtdb.jdbc/->tx-log
                                        :connection-pool :xtdb.jdbc/connection-pool}
                          :xtdb/document-store {:xtdb/module 'xtdb.jdbc/->document-store
                                                :connection-pool :xtdb.jdbc/connection-pool}}))


(def products-1 [{:xt/id :towel1
                  :prod/name "Purple Towel"
                  :prod/qunatity 10
                  :prod/date-update #inst "2021-11-02T18:00:00.000-00:00"
                  :prod/updated-by "Allan"
                  :prod/price 200}

                 {:xt/id :facecloth1
                  :prod/name "Blue Facecloth"
                  :prod/qunatity 5
                  :prod/date-update #inst "2021-11-02T18:00:00.000-00:00"
                  :prod/updated-by "Allan"
                  :prod/price 50}])

(def products-2 [{:xt/id :towel1
                  :prod/name "Purple Towel"
                  :prod/qunatity 7
                  :prod/date-update #inst "2021-11-10T18:00:00.000-00:00"
                  :prod/updated-by "Allan"
                  :prod/price 200}

                 {:xt/id :facecloth1
                  :prod/name "Blue Facecloth"
                  :prod/qunatity 1
                  :prod/date-update #inst "2021-11-10T18:00:00.000-00:00"
                  :prod/updated-by "Allan"
                  :prod/price 50}])


(def products-3 [{:xt/id :towel1
                  :prod/name "Purple Towel"
                  :prod/qunatity 17
                  :prod/date-update #inst "2021-11-12T18:00:00.000-00:00"
                  :prod/updated-by "Dan"
                  :prod/price 210}

                 {:xt/id :facecloth1
                  :prod/name "Blue Facecloth"
                  :prod/qunatity 6
                  :prod/date-update #inst "2021-11-12T18:00:00.000-00:00"
                  :prod/updated-by "Allan"
                  :prod/price 55}])


(def products-4 [{:xt/id :towel1
                  :prod/name "Purple Towel"
                  :prod/qunatity 20
                  :prod/date-update #inst "2021-11-13T18:00:00.000-00:00"
                  :prod/updated-by "Dan"
                  :prod/price 210}

                 {:xt/id :facecloth1
                  :prod/name "Blue Facecloth"
                  :prod/qunatity 6
                  :prod/date-update #inst "2021-11-13T18:00:00.000-00:00"
                  :prod/updated-by "Dan"
                  :prod/price 55}])


(defn prepare-docs [docs]
  (mapv (fn [doc]
          [::xt/put doc #inst "2021-01-04T18:00:00.000-00:00"]) docs))

(comment
  (xt/submit-tx node
                (prepare-docs products-1)))

(def history (xt/entity-history (xt/db node) :towel1 :desc {:with-docs? true}))

(println history)


(->> (map ::xt/doc history)
     (filter #(= (get % :prod/updated-by) "Dan")))


;; (xt/q (xt/db node)
;;       '{:find [?e ?name ?quantity ?date ?updated-by ?price]
;;         :where [[?e :prod/name ?name]
;;                 [?e :prod/qunatity ?quantity]
;;                 [?e :prod/date-update ?date]
;;                 [?e :prod/updated-by ?updated-by]
;;                 [?e :prod/price ?price]]})


(xt/q (xt/db node) '{:find [?e]
                     :where [[?e :xt/id :towel1]]})

(comment
  (java.util.Date.);; => #inst "2023-03-13T08:51:58.756-00:00"
  (prepare-docs products-1)

  (+ 1 2))