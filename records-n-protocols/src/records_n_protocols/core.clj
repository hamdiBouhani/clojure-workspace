(ns records-n-protocols.core)


(defprotocol Display
  (title [this])
  (description  [this description]))



(defrecord CarModel [make model]
  Display
  (title [this] (str " This is a" make " " model))
  (description [this description] (str " This is a" make " " model " is " description)))

(def fiat-500 (CarModel. "fiat" "500"))


(defrecord Product [name])
(def toaster (->Product "Toaster"))

(extend-protocol Display
  Product
  (title [this] (str " This Product is a " (:name this)))
  (description [this description] (str " The  is a " (:name this) " is " description)))


(comment
  (title toaster)

  (title fiat-500)
  (description fiat-500 "small car")
  ;;;;
  (def jeep-wrangler {:make "jeep" :model "wrangler"})
  (def (map->CarModel {:make "ford" :model "Focus"}))
  (:make jeep-wrangler)
  (:make fiat-500))




