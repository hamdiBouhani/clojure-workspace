(ns shorturl.core
  (:require [ring.adapter.jetty :as ring-jetty]
            [ring.util.response :as r]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [shorturl.db :as db]
            [shorturl.slug :refer [generate-slug]])
  (:gen-class))


;; https://github.com/alndvz/vid4




(defn redirect-page [req]
  (let [slug (get-in req [:path-params :slug])
        url (db/get-url slug)]
    (if url
      (r/redirect url 307)
      (r/not-found "Not found"))))

(defn create-redirect [req]
  (let [url (get-in req [:body-params :url])
        slug (generate-slug)]
    (db/insert-redirect! slug url)
    (r/response (str "create slug " slug))))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     [":slug/" redirect-page]
     ["api/"
      ["redirect/" {:post create-redirect}]]
     ["" {:handler (fn [req] {:status 200 :body "Hello World"})}]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn start []
  (ring-jetty/run-jetty #'app {:port  3000
                               :join? false}))
(comment
  (def server (start))
  (.stop server))
