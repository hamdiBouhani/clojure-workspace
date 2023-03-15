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