(ns core
  (:require
   [clojure.core.async :refer [chan >!! <!! thread put! take! go >! <! go-loop]]
   [hato.client :as hc]
   [clojure.data.json :as j]))



(let [c (chan 10)]
  (thread (doseq [i (range 10)]
            (println "putting in channel c" i)
            (>!! c i)))
  (thread (doseq [i (range 10)]
            (println "getting from channel c" (<!! c)))))

(let [c (chan)
      data "data !!!"]
  (thread (put! c data (fn [x] (println "putting" x))))
  (thread (take! c (fn [x] (println "getting" x)))))


;; gorountine
(let [c (chan)]
  (go (doseq [i (range 10)]
        (println "putting in channel c" i)
        (>! c i)))
  (go (doseq [i (range 10)]
        (println "getting from channel c" (<! c)))))

(comment
  (doseq [i (range 10)]
    (println "putting" i)))


;; practical example

;; (defn get-user [id]
;;   (let [url (str "https://reqres.in/api/users/" id)]
;;     (hc/get url)))


(defn get-user [id]
  (let [url (str "https://reqres.in/api/users/" id)
        data (get (-> (hc/get url)
                      :body
                      j/read-json) :data)]
    data))

(defn send-email [email]
  (Thread/sleep 2000)
  (println "sending email to" email))

(defn notify-user [id]
  (let [user (get-user id)
        email (get user :email)]
    (send-email email)))

(defn process-users []
  (let [c (chan)]

    (thread (doseq [i (range 1 5)]
              (println "putting in channel c user" i)
              (>!! c (get-user i))))

    (thread (loop []
              (when-some [user (<!! c)]
                (println user)
                (send-email (get user :email)))
              (recur)))))



(defn process-users-go []
  (let [c (chan)]

    (go (doseq [i (range 1 5)]
          (>! c (get-user i))))

    (go-loop []
      (when-some [user (<! c)]
        (send-email (get user :email)))
      (recur))))

(comment
  (def test-url "https://reqres.in/api/users/2")


  (hc/get test-url)

  (-> (hc/get test-url)
      :body)

  (get-user 2)
  (send-email "hamdi@test.com")
  (notify-user 2)
  (process-users)
  (process-users-go))
