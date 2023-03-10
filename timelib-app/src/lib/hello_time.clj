(ns lib.hello-time
  (:require [java-time.api :as jt]
            [java-time.repl]))

(defn now
  "Returns the current datetime"
  []
  (jt/instant))

(defn time-str
  "Returns a string representation of a datetime in the local time zone."
  [instant]
  (jt/format
   (jt/with-zone (jt/formatter "hh:mm a") (jt/zone-id))
   instant))

(defn run []
  (println "Hello world, the time is" (time-str (now))))