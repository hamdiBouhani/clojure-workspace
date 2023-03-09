(+ 1 2 3)

(str "Hello" " " "World")


; You can have multi-variadic functions, too
(defn hello3
  ([] "Hello World")
  ([name] (str "Hello " name)))

(hello3 "Jake")
(hello3)


; Functions can pack extra arguments up in a seq for you
(defn count-args [& args]
  (str "You passed " (count args) " args: " args))


(count-args 1 2 3)


; You can mix regular and packed arguments
(defn hello-count [name & args]
  (str "Hello " name ", you passed " (count args) " extra args"))


(hello-count "Finn" 1 2 3)