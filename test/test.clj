(ns test
  (:require [clojure.string :as s]))

(defn foo [x]
  (let [y (inc x)]
    (println y)
    y))



(defn say-hi
  [& names]
  (s/join ", " names))


(comment
  ;; foo
  (foo 1)
  (foo 2)

  ;; say-hi 
  (say-hi "Jack" "Jill" "Bob"))

;;