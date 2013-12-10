(ns btc-trading.btc-china
  (:require [btc-trading.api_keys :as api-keys :only (btc-china-access-key btc-china-secret-key)]
            [btc-trading.encoding :as encoding :only (to-base64)]
            [btc-trading.hmac :as hmac :only (sign-to-hexstring)]
            [org.httpkit.client :as client]
            [clojure.data.codec.base64 :as b64]
            [cheshire.core :as json]))


(def base-url "api.btcchina.com/api_trade_v1.php")

(def access-key api-keys/btc-china-access-key)

(def secret-key api-keys/btc-china-secret-key)

(def request-method "post")

(def method "getAccountInfo")

(def tonce (str (* (System/currentTimeMillis) 1000)))

(def signature-parameters
  "Returns the parameters for the btc-china request api."
  (str "tonce=" tonce
       "&accesskey=" access-key
       "&requestmethod=" request-method
       "&id=" tonce
       "&method=" method
       "&params="))

(def access-hash (hmac/sign-to-hexstring secret-key signature-parameters))

(def auth-string (str "Basic " (encoding/to-base64 (str access-key ":" access-hash))))

(def json-body
  "Returns the body for the json request"
  (json/generate-string {"id" tonce
                         "method" method
                         "params" []}))


(def options {:timeout 2000           ; ms
              :body json-body
              :headers {"Authorization" auth-string
                        "Json-Rpc-Tonce" tonce}})
(println options)
(client/post (str "https://" base-url) options
          (fn [{:keys [status headers body error]}] ;; asynchronous handle response
            (if error
              (println "Failed, exception is " error)
              (println "Async HTTP GET: " status))
            (println (str status headers body))))

