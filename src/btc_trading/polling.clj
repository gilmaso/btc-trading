(ns btc-trading.polling
  (:require [btc-trading.btc-china :as china]
            [clojure.core.async :as async :refer [>!! <!!
                                                  chan close! go
                                                  sliding-buffer thread]]))



(defn poll [function sleep]
  (let [c (chan (sliding-buffer 10))
        kill-switch (promise)]
    (while (not (realized? kill-switch))
      (do
        (Thread/sleep sleep)
        (>!! c (function))))
    {:channel c :kill-switch kill-switch}))


(defn infinite-loop [function]
  (do
    (function)
    (future (infinite-loop function))
    nil)) ;; nil is necessary to avoid overflowing the stack with futures...


(def counter (atom 1))

(infinite-loop
 #(do
    (Thread/sleep 1000)
    (swap! counter inc)))

@counter


(def channel (chan (sliding-buffer 10)))
(infinite-loop
 #(do
    (Thread/sleep 5000)
    (>!! channel (china/get-market-depth 3))))

(println (<!! channel))

; I should consider using netty



