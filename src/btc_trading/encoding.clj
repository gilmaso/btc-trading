(ns btc-trading.encoding
  (:require [clojure.data.codec.base64 :as b64]))

(defn to-base64 [original]
  (String. (b64/encode (.getBytes original)) "UTF-8"))
