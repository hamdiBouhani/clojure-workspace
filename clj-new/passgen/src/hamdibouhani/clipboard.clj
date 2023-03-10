(ns hamdibouhani.clipboard)

(import [java.awt.datatransfer StringSelection])

;; (defn copy-to-clipboard
;;   "Copies the given string to the clipboard"
;;   [s]
;;   (let [selection (StringSelection. s)]
;;     (.setContents selection selection)))

(defn ^sun.awt.X11.XClipboard get-clipboard []
  (-> (java.awt.Toolkit/getDefaultToolkit)
      (.getSystemClipboard)))


(defn clipboard []
  (.getSystemClipboard (java.awt.Toolkit/getDefaultToolkit)))



(defn copy [text]
  (let [^java.awt.datatransfer.StringSelection selection (StringSelection. text)]
    (.setContents (get-clipboard) selection selection)))

(comment
  (get-clipboard)
  (copy "hello"))