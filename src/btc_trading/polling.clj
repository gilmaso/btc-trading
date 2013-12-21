(ns btc-trading.polling
  (:require [btc-trading.btc-china :as china]
            [clojure.core.async :as async :refer [>!! <!!
                                                  chan close! go
                                                  sliding-buffer thread]]))

(defn poll "Do some polling" [string]
  (let [c (chan (sliding-buffer 10))]
    (future
      (Thread/sleep 1000)
      (>!! c (string)))
    (println (<!! c))
    (close! c)))



(defn poll2 "This polling method is broken." [string]
  (let [c (chan (sliding-buffer 10))]
    (future
      (Thread/sleep 1000)
      (>!! c string)
      (close! c))
    {:channel c}))

(defn poll3 "This polling method works, but blocks main thread" [string]
  (let [c (chan (sliding-buffer 10))]
      (Thread/sleep 1000)
      (>!! c string)
      (close! c)
      {:channel c}))

(defn poll4 "Do some polling" [string]
  (let [c (chan (sliding-buffer 10))]
    (future
      (Thread/sleep 1000)
      (>!! c string)
      (close! c)
      {:channel c})))

(defn poll4 "Do some polling" [string millisecs]
  (let [c (chan (sliding-buffer))]
    (future
      (Thread/sleep millisecs)
      (>!! c string)
      (close! c)
      {:channel c})))

(defn poll5 "Do some polling" [string]
  (let [c (chan (sliding-buffer 10))]
    (async/thread
     (>!! c string))
    (close! c)
    {:channel c}))

(defn poll6 [function sleep]
  (let [c (chan (sliding-buffer 10))
        kill-switch (promise)]
    (async/thread
     (while (not (realized? kill-switch))
       (do
         (Thread/sleep sleep)
         (>!! c (function)))))
    {:channel c :kill-switch kill-switch}))

(defn poll7 [function sleep]
  (let [c (chan (sliding-buffer 10))
        kill-switch (promise)]
    (while (not (realized? kill-switch))
      (do
        (Thread/sleep sleep)
        (>!! c (function))))
    {:channel c :kill-switch kill-switch}))



(def pollster {})

(def my-promise (promise))

;(println (<!! (poll6 china/get-account-info 5000)))

;(deliver my-promise (merge pollster (poll6 china/get-account-info 5000)))

;(println @my-promise)
;(println (<!! (:channel @my-promise)))

;(println "before")

; (println  (<!! (:channel  (poll2 "It's poll 2")))) ; Broken

;(println  (<!! (:channel  (poll3 "It's poll 3")))) ;
;(println "After 3")

;(println (<!! (:channel (poll4 "It's poll 4")))) ; This needs a deref "@"
;(println (<!! (:channel @(poll4 "It's poll 4" 3000)))) ;
;(println "After 4")

(println (<!! (:channel (poll5 "It's poll 5")))) ;
;(println "after 5")

;(println "after")

;(Thread/sleep 1000)

;(println (<!! (:channel (poll5 "It's poll 5"))))



