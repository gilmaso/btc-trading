(ns btc-trading.polling
  (:require [btc-trading.btc-china :as china]
            [clojure.core.async :as async :refer [chan close! go
                                                  >! >!! <! <!!
                                                  thread]]))







(defn mything []
  (let [c (chan)]
    (do
      (thread (>!! c china/get-market-depth))
      (thread (>!! c "you"))
      (println (<!! c))
    (println (<!! c))
    (close! c))))

(mything)



