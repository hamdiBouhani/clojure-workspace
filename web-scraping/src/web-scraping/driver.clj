(ns driver
  (:require [etaoin.api :as d]
            [clojure.data.json :refer [read-str]]
            [clojure.string :refer [escape]]))


(defn start [headless]
  (d/firefox {:headless headless}))

(defn quit [driver]
  (d/quit driver))

;; Navigate to product url (loc key)
(defn navigate-to-url [driver url]
  (d/go driver url))

(defn query-all [driver query]
  (d/query-all driver query))






(comment
  ;;(d/go driver "https://bellroy.com/products/key-tool")

  ;; Start firefox driver
  (def driver (start false))

  (navigate-to-url driver "https://bellroy.com/products/key-tool")
  ;; Query <script type="application/ld+json"> tags
  (def ld-json-query {:tag :script :type "application/ld+json"})
  (def ld-json-ids (d/query-all driver ld-json-query))
  (println ld-json-ids)


  ;; Remove '@' symbols and Build map 
  (def product-json
    (read-str
     (escape
      (d/get-element-inner-html-el driver (last ld-json-ids))
      {\@ ""})
     :key-fn keyword))

  ;; Remove '@' symbols and Build map 
  (def product-json
    (read-str
     (escape
      (d/get-element-inner-html-el driver (last ld-json-ids))
      {\@ ""})
     :key-fn keyword))
  (println product-json)

  ;; (d/quit driver)
  (quit driver)

  (println "end !!!"));;; end  comment

