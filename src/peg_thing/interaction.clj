(ns peg-thing.interaction
  [:require [peg-thing.render :as render]
   [peg-thing.moves :as move]
   [peg-thing.board :as board]])

(declare prompt-move game-over)

(defn letter->pos
  "convert letter to number"
  [letter]
  (inc (- (int (first letter)) render/alpha-start)))

(defn get-input
  "get input from user with a default"
  ([] (get-input nil))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

(defn characters-as-strings
  "get only valid characters and discard any other input"
  [input]
                                        ; (apply str (filter (fn [character]
  (map str (filter (fn [character]
                  (let [input-value (int character)]
                    (and (<= render/alpha-start input-value)
                       (>= render/alpha-end input-value))))
                input)))

(defn user-entered-invalid-move
  ""
  [board]
  (println "That was an invalid move!! Try again.")
  (prompt-move board))

(defn user-entered-valid-move
  ""
  [board]
  (if (move/can-move? board)
    (prompt-move board)
    (game-over board)))

(defn prompt-move
  "ask the user to move"
  [board]
  (println "Here is your board:")
  (render/print-board board)
  (println "move from where to where? Enter two letters:")
  (let [input (map letter->pos (characters-as-strings (get-input)))]
    (if-let [new-board (move/make-move board (first input) (second input))]
      (user-entered-valid-move new-board)
      (user-entered-invalid-move board))))

(defn prompt-empty-peg
  "ask for the initial removed peg"
  [board]
  (println "here is your board:")
  (render/print-board board)
  (println "which peg would you like removed? [a]")
  (let [peg (get-input "a")]
    (move/remove-peg board (letter->pos peg))))


(defn prompt-rows
  "ask the user how many rows should the board have"
  []
  (println "How many rows would you like your board to have? [5]")
  (let [rows (Integer. (get-input 5))
        board (board/new-board rows)]
    (prompt-empty-peg board)))


(defn game-over
  [board]
  (let [remaining-pegs (count (filter :pegged (vals board)))]
    (println "Game over! you had " remaining-pegs " pegs left")
    (render/print-board board)
    (println "would you like to play again? [Y/n]")
    (let [input (get-input "y")]
      (if (= "y" input)
        (prompt-rows)
        (do
          (println "Bye!")
          (System/exit 0))))))
