(ns btc-trading.hmac
    (:import (javax.crypto Mac)
             (javax.crypto.spec SecretKeySpec)))

(def algorithm "HmacSHA1")

(defn return-signing-key [key mac]
  "Get an hmac key from the raw key bytes given some 'mac' algorithm.
  Known 'mac' options: HmacSHA1
  Dev Status: Verified agains the Python implementation (2013-12-08)."
    (SecretKeySpec. (.getBytes key) (.getAlgorithm mac)))

(defn sign-to-bytes [key string]
  "Returns the byte signature of a string with a given key, using a SHA1 HMAC.
  Dev Status: Verified agains the Python implementation (2013-12-08)."
  (let [mac (Mac/getInstance algorithm)
        secretKey (return-signing-key key mac)]
    (-> (doto mac
          (.init secretKey)
          (.update (.getBytes string)))
        .doFinal)))

; Formatting
(defn bytes-to-hexstring [bytes]
  "Convert bytes to a String.
  Dev Status: Verified agains the Python implementation (2013-12-08)."
  (apply str (map #(format "%02x" %) bytes)))



; Public functions
(defn sign-to-hexstring [key string]
  "Returns the HMAC SHA1 hex string signature from a key-string pair.
  Dev Status: Verified against the Python implementation (2013-12-08)."
  (bytes-to-hexstring (sign-to-bytes key string)))

(sign-to-hexstring "my-key" "my-data")
