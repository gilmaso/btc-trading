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

(ns btc-trading.hmac
    (:import (javax.crypto Mac)
             (javax.crypto.spec SecretKeySpec)))

(def algorithm "HmacSHA1")

(defn- return-signing-key [key mac]
  "Get an hmac key from the raw key bytes given some 'mac' algorithm.
  Known 'mac' options: HmacSHA1"
    (SecretKeySpec. (.getBytes key) (.getAlgorithm mac)))

(defn- sign-to-bytes [key string]
  "Returns the byte signature of a string with a given key, using a SHA1 HMAC."
  (let [mac (Mac/getInstance algorithm)
        secretKey (return-signing-key key mac)]
    (-> (doto mac
          (.init secretKey)
          (.update (.getBytes string)))
        .doFinal)))

; Formatting
(defn- bytes-to-hexstring [bytes]
  "Convert bytes to a String."
  (apply str (map #(format "%02x" %) bytes))) ; "%02x" preserves leading zeros



; Public functions
(defn sign-to-hexstring [key string]
  "Returns the HMAC SHA1 hex string signature from a key-string pair."
  (bytes-to-hexstring (sign-to-bytes key string)))
