;
; (C) Copyright 2014 Ben Gimpert (ben@somethingmodern.com)
;
; All rights reserved. This program and the accompanying materials
; are made available under the terms of the Eclipse Public License v1.0
; which accompanies this distribution, and is available at
; http://www.eclipse.org/legal/epl-v10.html
;
(ns skream.core-test
  (:use clojure.math.numeric-tower)
  (:require [clojure.test :refer :all]
            [skream.core :refer :all])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; REPL Command
;; (do (use 'skream.core :reload) (use 'skream.core-test :reload) (skream-tests))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Unit Testing
;;
(defn ==ish
  ([a b] (==ish a b 0.0001))
  ([a b tol] (<= (abs (- a b)) tol)))
        
(deftest skream-tests
  (let [rand-norms '(0.0996  -0.8638  -0.5301  -0.4719  0.5818  0.3007  -0.6977  -0.2422
                      -1.8605  1.0952  0.2654  -0.8047  0.0071  0.0626  0.8189  -1.5884
                      1.2105  0.5946  -1.3816  0.3886  0.9375  1.1488  -0.2005  -1.0280
                      -0.7527  -0.0053  -0.5811  0.3122  1.5208  0.6932  0.0000  0.1466
                      -0.7595  0.5789  -0.0434  0.0629  -1.1237  1.1789  0.4249  1.0017
                      -0.5345  0.1315  0.7524  -1.4787  -0.3104  0.0752  -0.3503  1.6192
                      0.4982  1.0956  -0.7007  0.8820  1.0417  -1.8690  -1.2849  -0.1481
                      0.3985  0.0618  0.5842  -0.1504  0.7734  -0.1522  1.5846  -1.3061
                      -0.8828  0.2448  -0.3419  -0.6238  -0.1372  -2.3761  -0.1749  -1.9418
                      -1.0453  0.0241  -0.1978  -0.3921  -1.1483  -0.4503  0.3478  -0.1123
                      -0.4305  -1.7038  -1.9198  0.0096  -0.6390  1.1418  -0.6148  -0.0597
                      0.7738  0.2539  -2.0195  -0.8485  -0.0782  0.4092  1.0648  0.1518
                      2.5392  -0.1377  -0.8733  -1.1188  0.9664  0.4195  -2.2305  -2.0216
                      -1.0534  0.6712  0.3692  -0.3186  0.6408  0.1104  2.4247  0.5927
                      0.4044  1.5121  1.5630  0.3943  0.4078  -0.1252  1.0035  -0.9098
                      0.0773  -2.0205  -0.2536  -0.3150  -0.3188  0.3167  0.7170  -1.0069
                      0.2006  -1.0029  -1.1447  -0.7976  -1.3231  0.6172  -0.8459  0.6595
                      -0.4122  0.3781  -0.5586  1.0548  1.2127  1.6540  -0.9606  -0.3159
                      0.6625  0.5377  0.7741  0.4624  -0.3317  -1.8241  -0.5843  0.1201
                      1.2967  0.7319  0.3955  1.3159  0.4219  -1.3451  -0.1113  0.2860
                      1.3961  0.9504  0.2229  1.0046  -0.2460  0.7709  -0.1926  -1.0649
                      -0.0899  1.0263  0.1018  0.0614  0.2612  -2.3625  1.4971  0.0844
                      -2.4409  -0.3309  -1.2786  1.0753  -0.9183  -1.0352  -0.4193  0.4535
                      0.0227  1.6220  0.5533  -1.2269  0.9096  -0.1904  -0.3980  -0.1426
                      0.2957  -0.5174  -0.1093  -1.0298  -0.4347  1.2669  -1.2035  1.0573)]
    
    (testing "Skream Math Support"
             (is (== 4 (get-mean [1 2 3 4 5 6 7])))
             (is (== 120 (get-factorial 5)))
             (is (== 2598960 (get-combinations 52 5)))
             )

    (testing "Skream Hashing & Utility"
             (is (== (* 123 10) (apply + (get-vector-of-n 123 10))))
             (is (== 2296 (apply + (get-sha1-hash-bytes 1234))))
             (is (== 2193 (apply + (get-sha1-hash-bytes '(1 2 3 [4 5])))))
             (is (= '(\? \s \+ \f \ \? \s \ \? \ \? \2 \# \? \? \o \? \? \? \?)
                    (map char (.getBytes (get-sha1-hash-string 1234) "ASCII"))))
             (is (= '(\? \D \? \0 \? \( \L \? \? \? \? \space \) \? \  \< \) \ \X \?)
                    (map char (.getBytes (get-sha1-hash-string '(1 2 3 [4 5])) "ASCII"))))
             (is (== 1714123678 (get-32bit-sha1-hash 1234)))
             (is (== 814498975 (get-32bit-sha1-hash '(1 2 3 [4 5]))))
             (is (== 1933921417 (get-32bit-sha1-hash 1234 1234)))
             (is (== 242567668 (get-32bit-sha1-hash '(1 2 3 [4 5]) 1234)))
             (is (== 699981744 (get-jenkins-hash 123)))
             (is (== 596015325 (get-jenkins-hash "foo")))
             (is (== 3015569295 (get-jenkins-hash '(1 2 3 [4 5]))))
             )

    (testing "Skream Basics"
             (is (== 0 (:count (track-count (create-skream)))))
             (is (== 123 (:mean (add-num (track-mean (create-skream)) 123))))
             (is (nil? (:last (track-last (create-skream)))))
             (is (== 123 (:last (add-num (track-last (create-skream)) 123))))
             (is (== 234 (:last (add-num (track-last (create-skream)) 123 234))))
             (let [sk1 (add-num (track-min-max (create-skream)) 123)
                   sk2 (add-num sk1 234)
                   sk3 (add-num sk2 234)
                   sk4 (add-num sk3 -1)]
               (is (= [:max] (get-changed-keys sk1 sk2)))
               (is (empty? (get-changed-keys sk2 sk3)))
               (is (= [:min] (get-changed-keys sk3 sk4)))
               (is (or (= [:max :min] (get-changed-keys sk1 sk4)) (= [:min :max] (get-changed-keys sk1 sk4))))
               ))

    (testing "Skream Variance"
             (let [sk (add-num (-> (create-skream)
                                 track-population-standard-deviation
                                 track-population-variance
                                 track-variance
                                 track-sum-sq-diffs
                                 track-default)
                               1 2 3 4 5 6)]
               (is (== 6 (:count sk)))
               (is (== 1 (:min sk)))
               (is (== 6 (:max sk)))
               (is (== 3.5 (:mean sk)))
               (is (== 21 (:sum sk)))
               (is (== 17.5 (:sum-sq-diffs sk)))
               (is (== 3.5 (:var sk)))
               (is (== 35/12 (:pop-var sk)))
               (is (==ish 1.8708 (:stdev sk)))
               (is (==ish 1.7078 (:pop-stdev sk)))
               ))
    
    (testing "Skream Moving Averages"
             (let [sk (add-num (-> (create-skream)
                                 track-exponential-moving-average
                                 track-moving-average
                                 track-window)
                               1 2 3 4 5 6 7 8 9 10 11 12 13 14 15)]
               (is (==ish 9.0794 (get sk [:ema 0.125])))
               (is (== 21/2 (get sk [:ma 10])))
               (is (= [6 7 8 9 10 11 12 13 14 15] (get sk [:win 10])))
               ))
    
    (testing "Skream Moments"
             (let [unstd-sk (add-num (track-unstandardized-moment (track-sum-sq-diffs (create-skream)) 6)
                                     1 2 3 4 5 6 7 8 9 10 11 12 13 14 75)]
               (is (== 4480 (get unstd-sk [:unstd-moment 2])))
               (is (== (get unstd-sk :sum-sq-diffs) (get unstd-sk [:unstd-moment 2])))
               (is (== 245700 (get unstd-sk [:unstd-moment 3])))
               (is (== 15792952 (get unstd-sk [:unstd-moment 4])))
               (let [sk (add-num (track-moment (-> (create-skream)
                                                 track-skew
                                                 track-kurtosis) 6)
                                 1 2 3 4 5 6 7 8 9 10 11 12 13 14 75)]
                 (is (== 14 (get sk [:moment 2])))
                 (is (==ish 42.9220 (get sk [:moment 3])))
                 (is (==ish 154.2281 (get sk [:moment 4])))
                 (is (==ish 3.5376 (get sk :skew)))
                 (is (==ish 13.1789 (get sk :kurt)))
                 )))
    
    (testing "Skream Sketch Structure"
             (let [sketch (create-sketch 3 5)]
               (is (= [[0 0 0 0 0] [0 0 0 0 0] [0 0 0 0 0]] sketch))
               (is (= [0 0 0 0 0] (nth sketch 0)))
               (is (= 0 (nth (nth sketch 0) 0)))
               (let [modded-sketch (modify-sketch sketch 1 2 inc)]
                 (is (= [[0 0 0 0 0] [0 0 1 0 0] [0 0 0 0 0]] modded-sketch))
                 )
               ))
    
    (testing "Skream Membership"
             (let [sk (add-num (track-member-ish? (create-skream)) 1 2 3 4 5 1 2 3 4 5 4 3 2 1 2 3 4 5)]
               (is (member-ish? sk 3))
               (is (not (member-ish? sk 123)))
               ))
    
    (testing "Skream Sketches"
             (let [d 16 w 8
                   sk (track-element-counts-ish (track-default (create-skream)) d w)
                   sk (reduce add-num sk (get-vector-of-n 123 25))
                   sk (reduce add-num sk (get-vector-of-n 234 75))
                   sk (reduce add-num sk (get-vector-of-n 123 15))
                   sk (reduce add-num sk (get-vector-of-n 345 15))
                   sk (reduce add-num sk (get-vector-of-n 234 30))
                   ]
               (is (= 160 (:count sk)))
               (is (= 40 (get-element-count-ish sk d w 123)))
               (is (= 105 (get-element-count-ish sk d w 234)))
               ))
    
    (testing "Skream Range Counts"
             (let [simple-sk (add-num (track-element-range-count
                                        (track-element-range-count
                                          (track-element-range-count
                                            (track-default (create-skream))
                                            3 8)
                                          nil 6)
                                        8 nil)
                                      1 2 3 4 5 6 7 8 9 10 11 12 13 14 75)
                   norm-sk (add-num (track-element-normal-range-count
                                      (track-element-normal-range-count
                                        (track-element-normal-range-count
                                          (track-default (create-skream))
                                          1.0 1.25)
                                        nil 1.30)
                                      2 nil)
                                    1 2 3 4 5 6 7 8 9 10 11 12 13 14 75)]
               (is (== 5 (get simple-sk [:range-count 3 8])))
               (is (== 5 (get simple-sk [:range-count nil 6])))
               (is (== 8 (get simple-sk [:range-count 8 nil])))
               (is (== 2 (get norm-sk [:normal-range-count 1.0 1.25])))
               (is (== 4 (get norm-sk [:normal-range-count nil 1.30])))
               (is (== 1 (get norm-sk [:normal-range-count 2 nil]))))
             )
    
    (testing "Skream Histograms"
             (do
               (let [num-buckets 25
                     sk (track-histogram (track-default (create-skream)) -3 +3 num-buckets)
                     sk (apply add-num (cons sk rand-norms))
                     range-keys (filter (fn [key] (and (vector? key) (= :range-count (first key)))) (keys sk))
                     range-keys (sort (fn [a b] (compare (nth a 1) (nth b 1))) range-keys)]
                 (is (= 200 (:count sk)))
                 (is (= num-buckets (count range-keys)))
                 (is (= 16 (get sk [:range-count 3/25 9/25]))) 
                 )
               (let [num-buckets 25
                     sk (track-normal-histogram (track-default (create-skream)) num-buckets)
                     sk (apply add-num (cons sk rand-norms))
                     range-keys (filter (fn [key] (and (vector? key) (= :normal-range-count (first key)))) (keys sk))
                     range-keys (sort (fn [a b] (compare (nth a 1) (nth b 1))) range-keys)]
                 (is (= 200 (:count sk)))
                 (is (= num-buckets (count range-keys)))
                 (is (= 14 (get sk [:normal-range-count nil -1.75068607125217])))
                 (is (= 8 (get sk [:normal-range-count -1.1749867920660904 -0.9944578832097528])))
                 (is (= 6 (get sk [:normal-range-count 1.7506860712521692 nil])))
                 )))
    
    (testing "Skream P2 Quantiles"
             (do
               (let [sk (track-median-ish (track-default (create-skream)))
                     sk (apply add-num (cons sk rand-norms))]
                 (is (==ish 0.003231 (get-in sk [[:quantile 0.5] :quantile])))
                 (is (==ish 0.003231 (:median sk))))
               (let [sk (track-quantile-ish (track-default (create-skream)) 1/4)
                     sk (apply add-num (cons sk rand-norms))]
                 (is (==ish -0.5723 (get-in sk [[:quantile 1/4] :quantile])))
                 )))
    
    (testing "Skream HLL Cardinality Estimation"
             (do
               (let [b 6 sk (add-num (track-distinct-value-count-ish (create-skream) b) 1 2 3 4 5 1 2 3 4 5 4 3 2 1 2 3 4 5)]
                 (is (= 5 (:dv (get sk [:hll b])))))
               (let [b 8 sk (add-num (track-distinct-value-count-ish (create-skream) b) 1 2 3 4 5 1 2 3 4 5 4 3 2 1 2 3 4 5)]
                 (is (= 5 (:dv (get sk [:hll b])))))
               (let [b 6 sk (track-distinct-value-count-ish (track-default (create-skream)) b)
                     sk (apply add-num (cons sk rand-norms))]
                 (is (= 200 (:count sk)))
                 (is (= 177 (:dv (get sk [:hll b])))))
               (let [b 8 sk (track-distinct-value-count-ish (track-default (create-skream)) b)
                     sk (apply add-num (cons sk rand-norms))]
                 (is (= 200 (:count sk)))
                 (is (= 196 (:dv (get sk [:hll b])))))
               (let [b 10 sk (track-distinct-value-count-ish (track-default (create-skream)) b)
                     sk (apply add-num (cons sk rand-norms))]
                 (is (= 200 (:count sk)))
                 (is (= 198 (:dv (get sk [:hll b])))))
               ))
    
    (testing "Skream Random Range Counts"
             (let [rand-norm-sk (track-normal-histogram (track-default (create-skream)) 100)
                   rand-norm-sk (apply add-num (cons rand-norm-sk rand-norms))
                   hist-keys (filter (fn [key] (and (vector? key) (= (first key) :normal-range-count))) (keys rand-norm-sk))
                   hist-keys (sort (fn [a b] (compare (nth a 1) (nth b 1))) hist-keys)
                   count-vector (loop [i 0 current-count-vector []]
                                  (if (>= i (count hist-keys))
                                    current-count-vector
                                    (let [key (nth hist-keys i)
                                          key-count (get rand-norm-sk key)]
                                      ;(println "Count" i key key-count)
                                      (recur (inc i)
                                             (conj current-count-vector (get rand-norm-sk (nth hist-keys i)))))))]
               (is (== (count rand-norms) (:count rand-norm-sk)))
               (is (==ish 0.9453 (:stdev rand-norm-sk)))
               (is (== (dec (count rand-norms)) (apply + count-vector)))  ; one less, because the first add does not have a STDEV to normalize against
               (is (== 3 (get rand-norm-sk [:normal-range-count nil -2.3263478740408408])))
               (is (== 0 (get rand-norm-sk [:normal-range-count -0.4676987991145082 -0.43991316567323374])))
               (is (== 2 (get rand-norm-sk [:normal-range-count 2.3263478740408408 nil])))
               ))
    
    (testing "Skream Multi Counts"
             (let [msk (assoc-multi-skream (create-multi-skream)
                                           :sk1 (track-default (create-skream))
                                           :sk2 (track-count (create-skream)))
                   msk (track-multi-count msk)
                   msk (alias-stat msk :foo :mcount)
                   msk (apply add-multi-num msk :sk1 rand-norms)
                   msk (apply add-multi-num msk :sk2 rand-norms)]
               (is (contains? msk :mcount))
               (is (contains? msk :foo))
               (is (contains? (meta msk) :sub-sk-map))
               (is (contains? (get (meta msk) :sub-sk-map) :sk1))
               (is (contains? (get (meta msk) :sub-sk-map) :sk2))
               (is (not (contains? (get (meta msk) :sub-sk-map) :sk3)))
               (is (== 400 (get msk :mcount)))
               (is (== 400 (get msk :foo)))
               (is (== 200 (meta-get-in msk [:sub-sk-map :sk1 :count])))
               (is (==ish 0.9453 (meta-get-in msk [:sub-sk-map :sk1 :stdev])))
               (is (== 200 (meta-get-in msk [:sub-sk-map :sk2 :count])))
               (is (nil? (meta-get-in msk [:sub-sk-map :sk2 :stdev])))
               ))
    
    (testing "Skream Co-occurence Multi Adding"
             (let [msk (assoc-multi-skream (create-multi-skream)
                                           :sk1 (track-default (create-skream))
                                           :sk2 (track-count (create-skream)))
                   msk (track-multi-count msk)
                   msk (add-co-multi-num msk :sk1 123 :sk2 234)
                   msk (add-co-multi-num msk :sk1 -2 :sk2 5)
                   msk (add-co-multi-num msk :sk1 222 :sk2 345)]
               (is (== 6 (get msk :mcount)))
               (is (== 3 (meta-get-in msk [:sub-sk-map :sk1 :count])))
               (is (==ish 112.2512 (meta-get-in msk [:sub-sk-map :sk1 :stdev])))
               (is (== 3 (meta-get-in msk [:sub-sk-map :sk2 :count])))
               (is (nil? (meta-get-in msk [:sub-sk-map :sk2 :stdev])))
               ))

    (testing "Skream Co-occurence Multi Counts"
             (do
               (let [msk (assoc-multi-skream (create-multi-skream)
                                             :sk1 (track-default (create-skream))
                                             :sk2 (track-default (create-skream)))
                     msk (track-multi-count msk)
                     msk (track-multi-co-histogram msk :sk1 :sk2 -3 +3 15)
                     msk (loop [current-msk msk
                                current-rand-norms rand-norms]
                           (if (empty? current-rand-norms) current-msk
                             (let [x1 (nth current-rand-norms 0)
                                   x2 (nth current-rand-norms 1)]
                               (recur (add-co-multi-num current-msk :sk1 x1 :sk2 x2)
                                      (rest (rest current-rand-norms))))))]
                 (is (== 4 (get-in msk [[:co-hist :sk1 :sk2] [[:range-count -1/5 1/5] [:range-count -1/5 1/5]]])))
                 (is (== 4 (get-in msk [[:co-hist :sk1 :sk2] [[:range-count -1/5 1/5] [:range-count -1/5 1/5]]])))
                 (is (nil? (get-in msk [[:co-hist :sk1 :sk2] [[:range-count -3 -13/5] [:range-count -3 -13/5]]])))
                 )
               (let [msk (assoc-multi-skream (create-multi-skream)
                                             :sk1 (track-default (create-skream))
                                             :sk2 (track-default (create-skream)))
                     msk (track-multi-count msk)
                     msk (track-multi-co-normal-histogram msk :sk1 :sk2 15)
                     msk (loop [current-msk msk
                                current-rand-norms rand-norms]
                           (if (empty? current-rand-norms) current-msk
                             (let [x1 (nth current-rand-norms 0)
                                   x2 (nth current-rand-norms 1)]
                               (recur (add-co-multi-num current-msk :sk1 x1 :sk2 x2)
                                      (rest (rest current-rand-norms))))))]
                 (is (== 2 (get-in msk [[:co-norm-hist :sk1 :sk2] [[:normal-range-count 0.4307272992954576 0.6229257232100875] [:normal-range-count -1.501085946044025 -1.1107716166367854]]])))
                 (is (== 1 (get-in msk [[:co-norm-hist :sk1 :sk2] [[:normal-range-count 0.8416212335729142 1.1107716166367854] [:normal-range-count 0.2533471031357997 0.4307272992954576]]])))
                 (is (nil? (get-in msk [[:co-norm-hist :sk1 :sk2] [[:range-count -3 -13/5] [:range-count -3 -13/5]]])))
                 )))
               
    (testing "Skream Multi Mutual Information"
             (let [msk (assoc-multi-skream (create-multi-skream)
                                           :sk1 (track-default (create-skream))
                                           :sk2 (track-default (create-skream))
                                           :sk3 (track-default (create-skream)))
                   msk (track-multi-mutual-information msk :sk1 :sk2 -3 +3 15)
                   msk (track-multi-mutual-information msk :sk1 :sk3 -3 +3 15)
                   msk (loop [current-msk msk
                              current-rand-norms rand-norms]
                         (if (empty? current-rand-norms) current-msk
                           (let [x1 (nth current-rand-norms 0)         ; f(x1) = noise
                                 x2 (+ x1 (nth current-rand-norms 1))  ; f(x2) = f(x1) + noise, informative!
                                 x3 (nth current-rand-norms 1)]        ; f(x3) = noise
                             (recur (add-co-multi-num current-msk :sk1 x1 :sk2 x2 :sk3 x3)
                                    (rest (rest current-rand-norms))))))]
               (is (> (get msk [:mutinf :sk1 :sk2]) (get msk [:mutinf :sk1 :sk3])))
               (is (==ish 1.1024 (get msk [:mutinf :sk1 :sk2])))
               (is (==ish 0.5851 (get msk [:mutinf :sk1 :sk3])))
               ))
    ))

