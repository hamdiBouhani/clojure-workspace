# clojure-ws
repository for testing the Clojure programming language


# Projects :

## clj-new:

It's simple commend line applications that create a password generator using Clojure.

In this project, I used [clj-new](https://github.com/seancorfield/clj-new) to create a new project.

`clj-new` Generate new projects from Leiningen or Boot templates, or clj-template projects, using just the clojure command-line installation of Clojure!

## clj-template:
It's template project i download to get an ideas on the structure of clojure projects


## clj-tuto:
It's simple test  project to add testing  using `test-runner` and  how to add `spec` testing lib in clojure project.

```clojure
{:deps
 {org.clojure/core.async {:mvn/version "1.3.610"}
  speclj/speclj {:mvn/version "3.4.3"}
  toucan/toucan {:mvn/version "1.18.0"}}

 :aliases
 {:test {:extra-paths ["test" "test/tuto"]
         :extra-deps {io.github.cognitect-labs/test-runner {:git/tag "v0.5.1" :git/sha "dfb30dd"}}
         :main-opts ["-m" "cognitect.test-runner"]
         :exec-fn cognitect.test-runner.api/test}}}
```

```clojure
  (ns tuto.core_spec
    (:require [speclj.core :refer [describe it should should= should-not run-specs]]
              [tuto.core :refer :all]))


  ;; https://github.com/slagyr/speclj

  (describe "my-reduce"
            (it "should reduce a collection"
                (should= 1 (reduce + 1 '()))))

  (describe "Truth"
            (it "is true"
                (should true))
            (it "is not false"
                (should-not false)))

  (run-specs)

```

## core-async:

in this project, I try to understand to how core-async work.

```clojure
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

```

## multi-methods:

in this project, I try to understand how multi-methods concept 

```clojure
(ns multi-methods.core)


(def user1 {:name "John" :contact-method "sms"})
(def user2 {:name "Mark" :contact-method "phone"})
(def user3 {:name "Travis" :contact-method "email"})

(defn sms-user [user]
  (str "Sending SMS to" (:name user)))

(defn email-user [user]
  (str "Sending email to" (:name user)))

(defn phone-user [user]
  (str "Phoning" (:name user)))

;; (defn dispatch-notify-function [user]
;;   (case (:contact-method user)
;;     "sms" sms-user
;;     "email" email-user
;;     "phone" phone-user)



(defmulti notify-user :contact-method)
(defmethod notify-user "sms" [user] (sms-user user))
(defmethod notify-user "phone" [user] (phone-user user))
(defmethod notify-user :default [user] (email-user user))

(notify-user user1)
(notify-user user2)
```


## my-backend:

in this project, I tried to implement small micro-service using jetty.

```clojure
(ns my-backend.core
  (:require
   [compojure.core :as c]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.defaults :as ring-defaults])
  (:gen-class))

(defonce server (atom nil))

(defn routes []
  (c/routes
   (c/GET "/" [] {:status 200
                  :body "OK!!!"})
   (c/GET "/hello" [] {:status 200
                       :body "Hello World!!!"})
   (c/GET "/hello/:name" [name] {:status 200
                                 :body (str "Hello " name "!!!")})))

(defn handler [req]
  ((routes) req))

;; (defn wrap-defaults [handler]
;;   (ring-defaults/wrap-defaults handler ring-defaults/site-defaults))

(defn start-jetty! []
  (reset!
   server
   (jetty/run-jetty (ring-defaults/wrap-defaults
                     #'handler
                     ring-defaults/site-defaults) {:join? false
                                                   :port 3000})))

(defn stop-jetty! []
  (.stop @server)
  (reset! server nil))

(defn -main [& args]
  (start-jetty!))

(-main)

#_(start-jetty!)
```

## my-stuff:
In this project, I tried to create project using lein new.

```
$ lein new app my-stuff
```

Generating a project called my-stuff based on the 'app' template.

```
$ # see how it looks like using the "tree" command
$ tree -F -a --dirsfirst my-stuff/

my-stuff/
├── doc/
│   └── intro.md
├── resources/
├── src/
│   └── my_stuff/
│       └── core.clj
├── test/
│   └── my_stuff/
│       └── core_test.clj
├── CHANGELOG.md
├── .gitignore
├── .hgignore
├── LICENSE
├── project.clj
└── README.md
```

## passmanger:
In this project, I tried to create  small password manger using [Basbashka](https://book.babashka.org/) environment.

`Babashka` is a scripting environment made with `Clojure`, compiled to native with `GraalVM`. The primary benefits of using babashka for scripting compared to the JVM are fast startup time and low memory consumption. 

Babashka comes with batteries included and packs libraries like clojure.tools.cli for parsing command line arguments and cheshire for working with JSON. Moreover, it can be installed just by downloading a self-contained binary.

## py-from-clj:
in this project, I tried to call python lib for clojure following this [artical](https://gigasquidsoftware.com/blog/2021/03/15/breakfast-with-zero-shot-nlp/)

that tries to call `numpy` `pytorch` `transformers` `lime` from clojure code

```clojure
(ns clj.core
  (:require
   [libpython-clj2.python :as py]
   [libpython-clj2.require :refer [require-python]]))




;; Next we will need to install the required python deps:
;; pip install numpy torch transformers lime
(require-python '[transformers :bind-ns :as py-transformers])

;;https://huggingface.co/tasks/zero-shot-classification#:~:text=Zero%2Dshot%20text%20classification%20is,examples%20from%20previously%20unseen%20classes.
(def classifier (py/py. py-transformers "pipeline" "zero-shot-classification"))

(def text "French Toast with egg and bacon in the center with maple syrup on top. Sprinkle with powdered sugar if desired.")

(def labels ["breakfast" "lunch" "dinner"])

(classifier text labels)

(require-python '[lime.lime_text :as lime])
(require-python 'numpy)


(def explainer (lime/LimeTextExplainer :class_names labels))


(defn predict-probs
  [text]
  (let [result (classifier text labels)
        result-scores (get result "scores")
        result-labels (get result "labels")
        result-map (zipmap result-labels result-scores)]
    (mapv (fn [cn]
            (get result-map cn))
          labels)))


(defn predict-texts
  [texts]
  (println "lime texts are " texts)
  (numpy/array (mapv predict-probs texts)))


(predict-texts [text])


(def exp-result
  (py/py. explainer "explain_instance" text predict-texts
       :num_features 6
       :num_samples 100))


(py/py. exp-result "save_to_file" "explanation.html")
```

## shorturl

In this project i try to build small micro-service that stor url in mysql db
using those deps

```clojure
{:path ["src"]
 :deps {org.clojure/clojure {:mvn/version "1.10.0"}
        org.clojure/java.jdbc {:mvn/version "0.7.12"}
        mysql/mysql-connector-java {:mvn/version "8.0.30"}
        com.github.seancorfield/honeysql {:mvn/version "2.4.1002"}
        javax.servlet/servlet-api {:mvn/version "2.5"}
        ring/ring {:mvn/version "1.9.0"}
        metosin/reitit {:mvn/version "0.5.12"}
        metosin/muuntaja {:mvn/version "0.6.8"}}}
```

## web-scraping

In this project I tried to create web scraping script.

```clojure
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

```

## timelib-app

in this project i tried to test Clojure Java Interop

by calling java from clojure

## vid9
XTDB history API - [Clojure Tutorial](https://www.youtube.com/watch?v=coNzTkrcAIc&t=11s)

[XTDB](https://xtdb.com/) is a general-purpose bitemporal database for SQL, Datalog & graph queries.

## scratch

Clojure tutorial - use an atom to make a run once function wrapper
following this [tuto](https://www.youtube.com/watch?v=1zoNfM70cR0) that cover:

* clojure problem-solving.
* creating good docstrings.
* using atoms. 
* and checking thread safety.