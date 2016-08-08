(ns hew.tempmap)

(defn to-color
  "Gives the corresponding xy color for given temperature reading"
  [temp]
  (condp #(<= (get %1 0) %2 (get %1 1)) temp
    [0 4] [0.139, 0.081]                                    ;blue
    [5 10] [0.6531, 0.2834]                                 ;red
    [0.3402, 0.356]))                                       ;beige


