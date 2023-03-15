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
