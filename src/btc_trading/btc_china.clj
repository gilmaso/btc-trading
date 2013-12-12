; Copyright (C) 2013  gilmaso
;
; This program is free software: you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation, either version 3 of the License, or
; (at your option) any later version.
;
; This program is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
;
; You should have received a copy of the GNU General Public License
; along with this program.  If not, see <http://www.gnu.org/licenses/>.
;
; Email: gilmasog@gmail.com



(ns btc-trading.btc-china
  (:require [btc-trading.api_keys :as api-keys :only (btc-china-access-key btc-china-secret-key)]
            [btc-trading.encoding :as encoding :only (to-base64)]
            [btc-trading.helpers :as helpers :only (seq-to-csv)]
            [btc-trading.hmac :as hmac :only (sign-to-hexstring)]
            [org.httpkit.client :as client]
            [clojure.data.codec.base64 :as b64]
            [cheshire.core :as json]
            [clojure.core.async :as async :refer [chan close! go >! <!!]]))

; Reference Document: http://btcchina.org/api-trade-documentation-en


(def ^:private base-url "api.btcchina.com/api_trade_v1.php")

(def ^:private access-key api-keys/btc-china-access-key)

(def ^:private secret-key api-keys/btc-china-secret-key)

(defn- signature-string [tonce method params request-method]
  "Returns the parameters for the btc-china request api."
  (str "tonce=" tonce
       "&accesskey=" access-key
       "&requestmethod=" request-method
       "&id=" tonce
       "&method=" method
       "&params=" (helpers/seq-to-csv params)))

(defn- auth-string [signature-string]
  "Returns the authorization string which ends up in the header of the request."
  (let [access-hash (hmac/sign-to-hexstring secret-key signature-string)]
    (str "Basic " (encoding/to-base64 (str access-key ":" access-hash)))))

(defn- request-options [tonce method params auth-string]
  "Returns the map of request options required by the http client."
  (assoc {}
    :timeout 2000 ; ms
    :body (json/generate-string {"id" tonce
                                 "method" method
                                 "params" params})
    :headers {"Authorization" auth-string
              "Json-Rpc-Tonce" tonce}))

(defn- post-request [chan options]
  "Posts the request to the server based on options supplied.
  This function is supposed to get its options from request-options."
  (client/post (str "https://" base-url) options
          (fn [{:keys [status headers body error]}] ;; asynchronous handle response
            (if error
              (go (>! chan error))
              (go (>! chan body))))))

(defn- request [method params request-method]
  "Builds and sends a request to the server for a result."
  (let [tonce (str (* (System/currentTimeMillis) 1000))
        c (chan)]
    (post-request c
     (request-options
      tonce
      method
      params
      (auth-string
       (signature-string
        tonce
        method
        params
        request-method))))
    (json/parse-string (<!! (go (<! c))))))


; Public functions

(defn get-account-info []
  (request "getAccountInfo" [] "post"))

(defn get-market-depth []
  (request "getMarketDepth2" [] "post"))

(defn buy-btc [price amount]
  (request "buyOrder" [price amount] "post"))

(defn sell-btc [price amount]
  (request "sellOrder" [price amount] "post"))

(defn cancel-order [order-id]
  (request "cancelOrder" [order-id] "post"))

(defn request-withdrawal [currency amount]
  "Currency options: CNY, BTC
  NOTE: I currently do not have any funds in my BTCChina account.
  So, I get a -32000 internal server error. However, the standard
  python implementatio provided by BTCChina also gives such an error."
  (request "requestWithdrawal" [currency amount] "post"))

(defn get-deposits [currency]
  "Currency options: BTC
  Note: There should be a 'pending' parameter which takes the string 'false'
  which supposidly provides only non-pending deposits. However, the standard
  python implementation as provided by BTCChina is not working. So, this option
  is not available here."
  (request "getDeposits" [currency] "post"))

(defn get-orders
  "NOTE: get-orders shouldh have an open-only paramater which would supposedly give
  all orders (even completed orders?). Howerver, the python implementatio as provided
  by BTCChina does not work (it gives a -32019 invalid parameter error)."
  ([] (request "getOrders" [] "post")))

(defn get-order [order-id]
  "TODO: This needs to be tested agains a real-life order."
  (request "getOrder" [order-id] "post"))

(defn get-withdrawals []
  "Note: get-withdrawals should have a 'pending' options, but the standard python
  implementation does not provide a working version of this.
  Note: The BTCChina api apparently does not support 'CNY' as a withdrawal
  parameter."
  (request "getWithdrawals" ["BTC"] "post"))

(defn get-withdrawal [id]
  "TODO: Test with real withdrawals
  Note: get-withdrawals should have a 'pending' options, but the standard python
  implementation does not provide a working version of this."
  (request "getWithdrawal" [id] "post"))
