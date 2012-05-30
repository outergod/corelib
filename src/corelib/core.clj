(ns corelib.core
  (:use overtone.core))

(definst kickdrum [note 60 level 3]
  (let [env1 (perc 0.001 1 80 -20),
        env2 (perc 0.001 1 1 -30),
        env3 (perc 0.001 1 300 -20),
        noise (lpf (white-noise) (+ (env-gen:kr env1) 10)),
        osc (* 2 (lpf (square (+ (env-gen:kr env3) (midicps note))))),
        out (* (env-gen:kr env2 1 1.0 0.0 1.0 FREE) (mix [noise osc]) (pow 10 level))]
    #_(distort (compander out out 1.0 1 0.5 0.01 0.01))
    (distort out)))

#_(defsynth dominator [freq 440 amp 0.1 gate 1]
  (let [portamento (lag 0.2 0.6),
        vibamount (poll (env-gen:kr (envelope [1] [0.0 0.4] :loop-node 1)
                                    (abs (hpz1:kr portamento)))),
        vibrato (lin-x-fade2:kr portamento (* portamento (lf-par:kr 3)) (- (* 2 vibamount) 1)),
        midfreqs (map #(+ vibrato (* 3.87 %) (* (lf-noise1:kr 2) 3)) (range -2 3))]
    (out 0
         (-> (saw midfreqs) (mix)
             (+ (mix (sin-osc:ar (map #(* vibrato %) [0.25 0.5 0.75])
                                 (map #(* 2 %) [1 0.3 0.2]))))
             (rlpf (* vibrato (lag (if (< vibrato 100) 1 32) 0.01)))
             (* (env-gen (asr) gate FREE))
             (* amp)
             (pan2)))))
