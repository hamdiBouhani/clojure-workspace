(ns scratch.core)


;; 
;; "once"
;; debouncer /rate limiter
;; mutiple windows
;; invalidation
;; peek


(defn say-hi [username] (let [s (str "hello, " username)]
                          (println s)
                          s))


(comment
  (say-hi "Steve"))

(defn wrap-identity [f]
  (fn [] (f)))

(comment

  (let [run?_ (atom false)
        run? @run?_])
  ;;; say_hi
  (let [f1 say-hi
        f2 (wrap-identity f1)]
    (f2)))


(defn wrap-once
  "Returns a  function that wraps given function `f`.so that:
    - First call returns {:okay (f)}
    - Subsequent calls return nil, without calling `f`.
   "
  [f]
  (let [run?_ (atom false)]
    (fn once-fn [& args]
      ;; if previously run: noop , return nil
      ;; if not previously run: run , return {:okay (apply f args)}

      ;; clojure.core/compare-and-set!
      ;; Atomically sets the value of the atom to newval if and only if the current value of the atom is oldval.
      ;; Returns true if the value was set, else false.

      ;; we changed the value from false to true
      (when (compare-and-set! run?_ false true)
        {:okay (apply f args)})
      ;;we didn't change the value (e.g it was already true) so we return nil
      )))


(comment
  (let [wf (wrap-once say-hi)]
    (println "---")
    [(wf "Steve") (future (wf "Steve")) (wf "Steve") (future (wf "Steve")) (wf "Steve") (wf "Steve")]))


;; (future (wf "Steve")) call over thead pool

;; (defn wrap_once-in_a_while
;;   "
;;    ;; TODO  >=
;;   "
;;   [msecs-period f]
;;   (let [last-executed_ (atom nil)]
;;     (fn wrapper-fn
;;       " 
;;        if current time is >= PERIOD msecs since last execution, return {:okay (f)}
;;        else return nil
;;       "
;;       [& args]
;;       (let [now (System/currentTimeMillis)
;;             ?last-executed @last-executed_]

;;         (if-let  [last-executed ?last-executed]
;;           ;; Have perviously executed f
;;           (let [elapsed-time (- now last-executed)]
;;             (if (>= elapsed-time msecs-period)
;;               (let [result (try
;;                              (do {:okay (apply f args)})
;;                              (catch Throwable t  {:error t}))]
;;                 (reset! last-executed now)
;;                 result)
;;               ;; else
;;               ;; not enough time has elapsed
;;               ))
;;           ;; else
;;           ;; Have no pervious exection
;;           ))

;;       ;;(apply f args)
;;       )))


(defn wrap_once-in_a_while
  "
   ;; TODO  >=
  "
  [msecs-period f]
  (let [last-executed_ (atom 0)]
    (fn warpper [& args]
      (let [now (System/currentTimeMillis)
            last-executed @last-executed_
            elapsed (- now last-executed)]
        (when (>= elapsed msecs-period)
          (let [result (try
                         (do {:okay (apply f args)})
                         (catch Throwable t {:error t}))]
            (reset! last-executed_ now)
            result))))))

(comment

  (let [wf (wrap_once-in_a_while  100 say-hi)]
    (println "---")
    [(wf "1")  (wf "2") (wf "3")]))
