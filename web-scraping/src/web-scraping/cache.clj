(ns cache
  (:require [taoensso.carmine :as car]
            [clojure.string :refer [includes?]]))

;; https://github.com/ptaoussanis/carmine

;; Redis Setup
(def redis-conn {:pool {} :spec {:uri "redis://localhost:6379/"}})
(defmacro wcar* [& body] `(car/wcar redis-conn ~@body))

(defn clear-site
  [^String site]
  (wcar* (car/del site)))

(defn get-count
  [^String site]
  (wcar* (car/llen site)))

(defn product->cache [site mmap]
  (when-let [url (:loc mmap)]
    (when (includes? url "/products")
      (wcar* (car/rpush site mmap)))))

(defn cache->product [site] (wcar* (car/lpop site)))