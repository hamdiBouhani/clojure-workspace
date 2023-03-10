
(ns passmanger.app
  (:require [clojure.tools.cli :refer [parse-opts]]
            [passmanger.db :as db]
            [passmanger.password :refer [generate-password]]
            [passmanger.stash :as stash]
            [passmanger.clipboard :refer [copy]]
            [table.core :as t]))

;; Goals
;; Fast starting Clojure scripting alternative for JVM Clojure
;; Easy installation: grab the self-contained binary and run. No JVM needed.
;; Familiar: targeted at JVM Clojure users
;; Cross-platform: supports linux, macOS and Windows
;; Interop with commonly used classes (System, File, java.time.*, java.nio.*)
;; Multi-threading support (pmap, future)
;; Batteries included (tools.cli, cheshire, ...)

;; https://book.babashka.org
;; https://github.com/babashka/babashka
;; https://github.com/babashka/pods
;; https://github.com/babashka/pod-registry
;; https://github.com/cldwalker/table
;; https://github.com/clojure/tools.cli
;; https://github.com/seancorfield/honeysql
;; https://github.com/babashka/pod-babashka-go-sqlite3
;; https://github.com/rorokimdim/stash
;; https://github.com/rorokimdim/stash/blob/master/scripting-examples/babashka/stash.clj



(def cli-options
  [["-l" "--length Length" "Password length"
    :default 40
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-g" "--generate" "Generate new password"]
   [nil "--list"]])

(defn password-input []
  (println "Enter your master key:")
  (String. (.readPassword (System/console))))

(defn -main [& args]
  (let [parsed-options (parse-opts args cli-options)
        url (first (:arguments parsed-options))
        username (second (:arguments parsed-options))
        options (:options parsed-options)]
    ;; (println parsed-options)
    (cond
      (:generate options) (do

                            (stash/stash-init (password-input))
                            (let [password (generate-password (:length options))]
                              (db/insert-password url username)
                              (stash/add-password url username password)
                              (println "Password copied to clipboard")
                              (copy password)))

      (:list options) (t/table (db/list-passwords)))))

(comment

  (t/table (db/list-passwords))
  (-main "--list")
  (-main "facebook.com test-pw -g")
  (-main))