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
