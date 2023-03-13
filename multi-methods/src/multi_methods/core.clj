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