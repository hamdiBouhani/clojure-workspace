(ns my-stuff.core
  (:require [clj-http.client :as http])
  (:gen-class))


(def response (http/get "https://leiningen.org"))

(keys response)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println response))
