(ns peg-thing.moves)

(defn pegged?
  "Is posittion pegged?"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "remove the peg from board"
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "put a peg in the board"
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "move a peg from origin to destination"
  [board from to]
  (place-peg (remove-peg board from) to))

(defn valid-moves
  "Return a map of valid moves for position, where key is destination and value is the jumped position."
  [board position]
  (into {}
        (filter (fn [[destination jumped]] (and (pegged? board jumped) (not (pegged? board destination))))
                (get-in board [position :connections]))))

(defn valid-move?
  "return jumped position if move is valid"
  [board from to]
  (get (valid-moves board from) to))

(defn make-move
  "make a valid move, removing the jumped peg"
  [board from to]
  (if-let [jumped (valid-move? board from to)]
    (move-peg (remove-peg board jumped) from to)))

(defn can-move?
  "is there any valid movements in the board?"
  [board]
  (some (comp not-empty
              (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))
