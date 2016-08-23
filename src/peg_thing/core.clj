(ns peg-thing.core
  (:gen-class)
  [:require [peg-thing.interaction :as interaction]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "welcome to peg-thing! let's get you a board!")
  (interaction/prompt-move (interaction/prompt-rows)))
