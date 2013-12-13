(ns btc-trading.polling
  (:require [btc-trading.btc-china :as china]
            [clojure.core.async :as async :refer [chan close! go
                                                  >! >!! <! <!!
                                                  thread timeout]]))


(let [c (chan)]
  (future
    (>!! c (china/get-account-info))
    (>!! c (china/get-market-depth 10)))
  (println (<!! c))
  (println (<!! c)))




