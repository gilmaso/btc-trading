(ns btc-trading.jobs
  (:require [clojure.core.async :as async :refer [>!! <!!
                                                  chan close! go
                                                  sliding-buffer thread]]))

(defrecord job [name time-created function])

(defn create-job-listing [] (atom {}))

(defn create-job [] "")
