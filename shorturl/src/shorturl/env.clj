(ns shorturl.env)

(def envars (clojure.edn/read-string (slurp "env.edn")))

(defn env [k]
  (or (k envars) (System/getenv (name k))))




(comment
  (name :HOME)
  (env :DBTYPE)
  (println envars))