(ns core
  (:require [clojure.xml :as xml]
            [cache :as c]
            [driver :as driver]
            [etaoin.api :as e]
            [clojure.data.json :refer [read-str]]
            [somnium.congomongo :as m]))

(defn parse-xml [url]
  (try (xml/parse url)
       (catch Exception e
         (println
          (str "Caught Exception parsing xml: " (.getMessage e))))))

(comment
  (parse-xml "https://bellroy.com/sitemap.xml"))


(defn get-content
  [content]
  (into {} (map (fn [c] [(:tag c) (first (:content c))]) (:content content))))

(defn find-products
  "Crawls the url's sitemap and for each product, will call f with the site and the product"
  [site url f]
  (let [p (parse-xml url)
        tag (:tag p)]
    (println (str "Parsing URL: " url " for site " site))
    (if p
      (cond
        (= tag :urlset) (doseq [mm (map get-content (:content p))] (f site mm))
        (= tag :sitemapindex) (let [ccs (map get-content (:content p))
                                    pps (vec (map :loc ccs))]
                                (doseq [mm pps] (find-products site mm f)))
        :else (println "none"))
      (println (str "Unable to process url for " site)))
    (println "Done finding products")))

(comment
  (find-products "bellroy" "https://bellroy.com/sitemap.xml" println))

(c/clear-site "bellroy") ;; Clear cache first
(find-products "bellroy" "https://bellroy.com/sitemap.xml" c/product->cache)
(c/get-count "bellroy")

;; Popping product links example:
(c/cache->product "bellroy")
;; (c/cache->product "bellroy")

(def web-driver (driver/start false))


;; Navigate to product url (loc key)
(e/go web-driver (:loc (c/cache->product "bellroy")))

;; Query <script type="application/ld+json"> tags
(def ld-json-query {:tag :script :type "application/ld+json"})
(def ld-json-ids (e/query-all web-driver ld-json-query))

;; Only two ld+json script tags-- get the content
(read-str (e/get-element-inner-html-el web-driver (first ld-json-ids)))
;; => {"@context" "https://schema.org", "@type" "BreadcrumbList" ...}
(read-str (e/get-element-inner-html-el web-driver (last ld-json-ids)))
;; => {"@context" "https://schema.org/", "@type" "product" ...}

(require '[clojure.string :refer [escape]])

;; Remove '@' symbols and Build map 
(def product-json
  (read-str
   (escape
    (e/get-element-inner-html-el web-driver (last ld-json-ids))
    {\@ ""})
   :key-fn keyword))

(println product-json)
;; => {:context "https://schema.org/", :type "product", :brand "Bellroy" ...}

(driver/quit web-driver)


;; Mongo

;; Products database
(def mongo-conn
  (m/make-connection
   "products"
   :instances [{:host "127.0.0.1" :port 27017}]))

;; Set global connection
(m/set-connection! mongo-conn)

(println product-json)

;; Add product offers to "bellroy" collection
(m/mass-insert! "bellroy" [product-json])

(m/close-connection mongo-conn)
