(ns legacy.core
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.reflect :as reflect]
            [clojure.test :refer [deftest is]])
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


(defn abstract-output [{:keys [output result] :as k}])


(defn- run-full [game sequence]
   (reduce (partial run-cmd game) [] sequence))

(defn- exception? [{r :result}]
  (and (map? r)
       (:exception r)))

(defn run [game sequence]
  (let [r (run-full game sequence)
        [pre [e & _]] (partition-by exception? r)
        p (concat pre [e])]
        p))


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
   (gen/vector step))

;; Properties

(defn run-refactored [p]
  (run (new com.adaptionsoft.games.trivia.Game) p))

(defn run-original [p]
  (run (new com.adaptionsoft.games.uglytrivia.Game) p))



(def equivalence
  (prop/for-all [p program]
                (let [r (run-refactored p)
                      r' (run-original p)]
                  (= r r'))))


(deftest refactoring-should-preserve-behavior
  (let [{:keys [result seed] :as r} (tc/quick-check 500 equivalence)]
    (when (not= result :ok)
      (let [[p & _] (get-in r [:shrunk :smallest])]
        (is (= (run-original p) (run-refactored p)) (str "Sequence: " p))))))


;;(println (last (gen/sample program 1500)))

;;(println (run (new Game) (last (gen/sample program 400))))
