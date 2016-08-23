(ns peg-thing.render
  [:require [peg-thing.board :as board]
   [peg-thing.math :as peg-math]])

(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char) (range alpha-start alpha-end)))
(def pos-chars 3)

(def ansi-styles
  {:red   "[31m"
   :green "[32m"
   :blue  "[34m"
   :reset "[0m"})

(defn ansi
  "Produce a string which will apply an ansi style"
  [style]
  (str \u001b (style ansi-styles)))

(defn colorize
  "Apply ansi color to text"
  [text color]
  (str (ansi color) text (ansi :reset)))

(defn render-pos
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         (colorize "0" :blue)
         (colorize "-" :red))))

(defn row-positions
  "Return all positions in the given row"
  [row-num]
  (range (inc (or (peg-math/row-tri (dec row-num)) 0))
         (inc (peg-math/row-tri row-num))))

(defn row-padding
  "String of spaces needed to add to a row to center it"
  [row-num rows]
  (let [pad-legth (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-legth (repeat " ")))))

(defn render-row
  ""
  [board row-num]
  (str (row-padding row-num (:rows board))
       (clojure.string/join " " (map (partial render-pos board)
                                     (row-positions row-num)))))

(defn print-board
  "have the board printed on-screen"
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))

