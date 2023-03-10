(ns passmanger.password)

;; (defn generate-password
;;   "Generates a password of length n"
;;   [n]
;;   (let [chars "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+{}|:<>?"]
;;     (apply str (repeatedly n #(rand-nth chars)))))




(comment
  (def available-chars (reduce (fn [acc val]
                                 (str acc (char val))) "" (range 33 123)))
  (println available-chars)
  (range 33 123)
  (char 122)
  (rand-nth available-chars))


(defn generate-password [length]
  (let [available-chars (reduce (fn [acc val]
                                  (str acc (char val))) "" (range 33 123))]
    (loop [password ""]
      (if (= (count password) length)
        password
        (recur (str password (rand-nth available-chars)))))))
(comment
  (generate-password 100))