(ns legacy.core
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.reflect :as reflect]
            [clojure.test.check.clojure-test :refer [defspec]])
  (:import [java.io ByteArrayOutputStream PrintStream]))

;; Trivia Game Interpreter

(defmacro capture-output [& body]
  `(let [buffer# (ByteArrayOutputStream.)
         system-out# System/out
         out# (PrintStream. buffer# true "UTF-8")]
     (let [result#
           (try
             (System/setOut out#)
             ~@body
             (catch Throwable t# {:exception (.. t# getClass getSimpleName) :msg (.getMessage t#)})
             (finally
               (System/setOut system-out#)))]
       {:result result# :output (.toString buffer# "UTF-8")})))

(defmulti run-cmd (fn [_ _ [cmd & _]] cmd))

(defmethod run-cmd :createRockQuestion [game trace [_ i]]
  (conj trace
        (capture-output (.createRockQuestion game i))))

(defmethod run-cmd :add [game trace [_ name]]
  (conj trace
        (capture-output (.add game name))))

(defmethod run-cmd :wrongAnswer [game trace _]
  (conj trace
        (capture-output (.wrongAnswer game))))

(defmethod run-cmd :wasCorrectlyAnswered [game trace _]
  (conj trace
        (capture-output (.wasCorrectlyAnswered game))))

(defmethod run-cmd :roll [game trace [_ roll]]
  (conj trace
        (capture-output (.roll game roll))))

(defmethod run-cmd :howManyPlayers [game trace _]
  (conj trace
        (capture-output (.howManyPlayers game))))

(defmethod run-cmd :isPlayable [game trace _]
  (conj trace
        (capture-output (.isPlayable game))))

(defn run [game sequence]
  (reduce (partial run-cmd game) [] sequence))


;; Generation

(def add (gen/tuple (gen/return :add) gen/string))
(def roll (gen/tuple (gen/return :roll) (gen/elements (range 1 7))))
(def rock-question (gen/tuple (gen/return :createRockQuestion) gen/int))

(def step (gen/one-of [add
                       roll
                       rock-question
                       (gen/return [:isPlayable])
                       (gen/return [:wrongAnswer])
                       (gen/return [:wasCorrectlyAnswered])
                       (gen/return [:howManyPlayers])]))

(def program
   (gen/vector step 50 100))

;; Properties

(def equivalence
  (prop/for-all [p program]
                (let [r (run (new com.adaptionsoft.games.trivia.Game) p)
                      r' (run (new com.adaptionsoft.games.uglytrivia.Game) p)]
                  (= r r'))))

(defspec refactoring-should-preserve-behavior
  100
  equivalence)

;;(println (last (gen/sample program 1500)))

;;(println (run (new Game) (last (gen/sample program 400))))
