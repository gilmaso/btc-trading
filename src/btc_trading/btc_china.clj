(ns btc-trading.btc-china
  (:require [btc-trading.api_keys :as api-keys :only (btc-china-access-key btc-china-secret-key)]
            [btc-trading.encoding :as encoding :only (to-base64)]
            [btc-trading.hmac :as hmac :only (sign-to-hexstring)]
            [org.httpkit.client :as client]
            [clojure.data.codec.base64 :as b64]
            [cheshire.core :as json]))


(def base-url "api.btcchina.com/api_trade_v1.php")
;(def base-url "127.0.0.1:8000/")

(def access-key api-keys/btc-china-access-key)

(def secret-key api-keys/btc-china-secret-key)

(def request-method "post")

(def method "getAccountInfo")

(def tonce (str (* (System/currentTimeMillis) 1000)))
;(def tonce 100)

(def signature-parameters
  "Returns the parameters for the btc-china request api."
  (str "tonce=" tonce
       "&accesskey=" access-key
       "&requestmethod=" request-method
       "&id=1"
       "&method=" method
       "&params="))
(println (str "sig params" signature-parameters))

(def access-hash (hmac/sign-to-hexstring secret-key signature-parameters))
(println (str "access-hash " access-hash))

(def auth-string (str "Basic " (encoding/to-base64 (str access-key ":" access-hash))))
(println (str "auth-string " auth-string))

(def json-body
  "Returns the body for the json request"
  (json/generate-string {;"tonce" tonce
                         ;"accesskey" access-key
                         ;"requestmethod" request-method
                         "id" "1"
                         "method" method
                         "params" []}))
(println json-body)


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

