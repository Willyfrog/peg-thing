(ns peg-thing.board
  [:require [peg-thing.math :as peg-math]])

(defn connect
  "Form a mutual connection between two positions"
  [board max-position position neighbour destination]
  (if (<= destination max-position)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbour)) board [[position destination] [destination position]])
    board))

(defn connect-right
  "Form a connection to the right and a reverse one from the destination"
  [board max-pos position]
  (let [neighbour (inc position)
        destination (inc neighbour)]
    (if-not (or (peg-math/triangular? neighbour) (peg-math/triangular? position))
      (connect board max-pos position neighbour destination)
      board)))

(defn connect-down-left
  ""
  [board max-pos position]
  (let [row (peg-math/row-num position)
        ; triangular numbers math, current-row + number get's their lower-left neighbour
        neighbour (+ row position)
        destination (+ row 1 neighbour)]
    (connect board max-pos position neighbour destination)))

(defn connect-down-right
  ""
  [board max-pos position]
  (let [row (peg-math/row-num position)
        neighbour (+ row 1 position)
        destination (+ row 2 neighbour)]
    (connect board max-pos position neighbour destination)))

(defn add-position
  "adds a peg and its connections"
  [board max-pos position]
  (let [pegged-board (assoc-in board [position :pegged] true)]
    (reduce (fn [new-board connect-function] (connect-function new-board max-pos position))
            pegged-board
            [connect-right connect-down-left connect-down-right])))

(defn add-position-threaded
  "same as the non-threaded one but a little clearer"
  [board max-pos position]
  (let [pegged-board (assoc-in board [position :pegged] true)]
    (-> pegged-board
       (connect-right max-pos position)
       (connect-down-left max-pos position)
       (connect-down-right max-pos position)
       )))

(defn new-board
  ""
  [rows]
  (let [initial-board {:rows rows}
        max-pos (peg-math/row-tri rows)]
    (reduce (fn [board position] (add-position board max-pos position))
            initial-board
            (range 1 (inc max-pos)))))
