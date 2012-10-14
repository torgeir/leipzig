(ns whelmed.songs.SKA
  (:use
        [whelmed.melody]
        [whelmed.scale]
        [whelmed.instrument]
        [overtone.live :only [ctl at midi->hz now stop]]))

(def then follow)

(defn demo
  ([notes] (demo notes major))
  ([notes scale]
    (->> notes
      (skew :time (bpm 90))
      (skew :duration (bpm 90))
      (skew :pitch (comp C scale))
      play)))

(def bass
  (->>
      (phrase
        [3/2 1 1/2 1]
        [0   0   2 4])
    (then
      (phrase
        [3/2 1 1/2 1]
        [5   5   4 2]))
    (with :part ::bass)
    (skew :pitch (comp lower lower))))

(def wish-you-were-here-again 
  (->>
      (phrase
        [2/3 1/3 3/3 3/3 2/3 13/3]
        [0 1 0 4 0 0])
    (then
      (phrase
        [2/3 1/3 3/3 3/3 3/3 2/3 1/3 2/3 3/3 4/3]
        [0 1 0 4 0 2 3 2 1 0]))
    (skew :pitch raise)
    (with :part ::melody)))

(defn cluster [duration pitches]
  (map
    #(zipmap
       [:time :duration :pitch]
       [0 duration %])
    pitches))

(defn chord [degree duration]
  (->> (triad degree) vals (cluster duration)))

(def rhythm
  (->>
    (->> (chord 0 1)
      (after 1)
      (times 2))
    (accompany (->> (chord -2 1)
      (after 1)
      (times 2)
      (after 4)))
    (with :part ::rhythm)))

(def suns-on-the-rise 
  (->>
    (->> [(triad 1)] (skew :i #(+ % 1/2)) (skew :v #(+ % 1/2)) (mapcat vals) (cluster 4))
    (then (chord -2 4))
    (then (chord 0 4))
    (with :part ::rhythm)))

(def oooh
  (->>
    (phrase
      [3 1/3 2/3 3 2/3 1/3 3]
      [3 4 3 2 0 -1 0]) 
    (skew :pitch raise)
    (with :part ::melody)))

(def and-if-you-lived-here
  (->>
    (chord 0 4)
    (then (chord -3 4))
    (then
      (cluster 4
        (-> (triad 1) (update-in [:iii] #(+ % 1/2)) vals)))
    (then
      (cluster 4
        (-> (triad -2) (update-in [:iii] #(+ % 1/2)) vals)))))

(def ska
  (->>
    (->> bass
      (times 2)
      (then (->> bass (accompany rhythm) (times 2)
            (accompany wish-you-were-here-again)
            (times 2)))
      (then (accompany oooh suns-on-the-rise))
      (then (->> bass (times 2) (after -4)))
      (skew :pitch (comp E minor)))
    (then (->> and-if-you-lived-here 
      (times 4)
      (skew :pitch lower)
      (skew :pitch (comp B flat major))))
    (skew :time (bpm 150))
    (skew :duration (bpm 200))))

;(play ska)
