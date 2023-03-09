(ns resultful-crud.user
  (:require [schema.core :as s]
            [resultful-crud.string-util :as str]
            [resultful-crud.models.user :refer [User]]
            [buddy.hashers :as hashers]
            [clojure.set :refer [rename-keys]]
            [toucan.db :as db]
            [ring.util.http-response :refer [created ok not-found]]
            [compojure.api.sweet :refer [POST GET PUT DELETE api routes]]))

(defn valid-username? [name]
  (str/non-blank-with-max-length? 50 name))

(defn valid-password? [password]
  (str/length-in-range? 5 50 password))

(s/defschema UserRequestSchema
  {:username (s/constrained s/Str valid-username?)
   :password (s/constrained s/Str valid-password?)
   :email (s/constrained s/Str str/email?)})

;; Takes the id of the new user returned by Toucan and returns the ring’s created HTTP response
(defn id->created [id]
  (created (str "/users/" id) {:id id}))

;; Canonicalize the request by hashing the password, the rename the key password with password_hash (to match the column name in the database)
(defn canonicalize-user-req [user-req]
  (-> (update user-req :password hashers/derive)
      (rename-keys {:password :password_hash})))

(defn create-user-handler [create-user-req]
  (->> (canonicalize-user-req create-user-req)
       (db/insert! User) ;; Insert into the table using Toucan’s insert! function
       :id
       id->created))

(defn user->response [user]
  (if user
    (ok user)
    (not-found)))

(defn get-user-handler [user-id]
  (-> (User user-id)
      (dissoc :password_hash)
      user->response))

(defn get-users-handler []
  (->> (db/select User)
       (map #(dissoc % :password_hash))
       ok))

(defn update-user-handler [id update-user-req]
  (db/update! User id (canonicalize-user-req update-user-req))
  (ok))

(defn delete-user-handler [user-id]
  (db/delete! User :id user-id)
  (ok))

(def user-routes
  [(POST "/users" []
     :body [create-user-req UserRequestSchema]
     (create-user-handler create-user-req))
   (GET "/users/:id" []
     :path-params [id :- s/Int]
     (get-user-handler id))
   (GET "/users" []
     (get-users-handler))
   (PUT "/users/:id" []
     :path-params [id :- s/Int]
     :body [update-user-req UserRequestSchema]
     (update-user-handler id update-user-req))
   (DELETE "/users/:id" []
     :path-params [id :- s/Int]
     (delete-user-handler id))])


(def swagger-config
  {:ui "/swagger"
   :spec "/swagger.json"
   :options {:ui {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

; (def app (apply routes user-routes))
(def app (api {:swagger swagger-config} (apply routes user-routes)))
