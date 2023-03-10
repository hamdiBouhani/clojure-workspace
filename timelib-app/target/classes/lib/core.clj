(ns lib.core
  (:require [lib.hello-time :refer [run]])
  (:gen-class))


(comment
  (run))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run))

(-main)