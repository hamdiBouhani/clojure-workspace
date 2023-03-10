(ns hamdibouhani.passgen
  (:require [hamdibouhani.password :refer [generate-password]]
            [hamdibouhani.clipboard :refer [copy]]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-l" "--length LENGTH" "Password length"
    :default 8
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 100) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])
(copy "hello")
(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (let [arguments (parse-opts args cli-options)
        options (:options arguments)
        summary (:summary arguments)]
    (if (:help options)
      (println summary)
      (let [password (generate-password (:length options))]
        (copy password)
        (println password)))))



