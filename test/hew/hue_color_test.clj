(ns hew.hue-color-test
  (:require [midje.sweet :refer :all]
            [hew.hue-color :as hc]))

(defn s [[x y]]
  [(.setScale (bigdec x) 4 BigDecimal/ROUND_HALF_UP)
   (.setScale (bigdec y) 4 BigDecimal/ROUND_HALF_UP)])

(fact "R G B to X Y conversion for some samples"
      (s (hc/rgb-to-xy [249, 234, 214])) => [0.3548M, 0.3489M] ; Antique White
      (s (hc/rgb-to-xy [165, 40, 40])) => [0.6399M, 0.3041M] ; Brown
      (s (hc/rgb-to-xy [94, 158, 160])) => [0.2211M, 0.3328M] ; Cadet Blue
      (s (hc/rgb-to-xy [104, 104, 104])) => [0.3227M, 0.329M] ; Dim Grey
      (s (hc/rgb-to-xy [255, 104, 181])) => [0.4682M, 0.2452M]) ; Hot Pink
