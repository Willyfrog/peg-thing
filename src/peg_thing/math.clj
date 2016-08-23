(ns peg-thing.math)

(defn tri*
  "generates a lazy seq of triangular numbers"
  ([] (tri* 0 1))
  ([sum n] (let [new-sum (+ sum n)]
            (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))

(defn triangular?
  "is the number a triangular one?"
  [n]
  (= n (last (take-while #(>= n %) tri))))

(defn row-tri
  "get the last triangular number in the specified row"
  [row]
  (last (take row tri)))

(defn row-num
  "get the row where the number appears in"
  [number]
  (inc (count (take-while #(> number %) tri))))
