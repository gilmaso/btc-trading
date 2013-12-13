(ns btc-trading.polling
  (:require [btc-trading.btc-china :as china]
            [clojure.core.async :as async :refer [>!! <!!
                                                  chan close! go
                                                  sliding-buffer]]))


(let [c (chan (sliding-buffer 10))]
  (future
    (>!! c (china/get-account-info))
    (>!! c (china/get-market-depth 10)))
  (println (<!! c))
  (println (<!! c))
  (close! c))




