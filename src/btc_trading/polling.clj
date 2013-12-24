(ns btc-trading.polling)


(def ^:private spawned-threads (atom {}))

(defn spawn-thread [function interval name]
  "Takes a function to be repeatedly called at a given interval.
  Returns a uuid.
  Use the uuid to acc.
  Takes: function
         interval in milliseconds
  Returns: promise"
  (let [kill-switch (promise)
        uuid (keyword (str (java.util.UUID/randomUUID)))
        start-time (System/currentTimeMillis)]
    (future
      (while (not (realized? kill-switch))
              (do
                (Thread/sleep interval)
                (eval function)))
      (swap! spawned-threads dissoc uuid)) ; Remove thread from spawned-threads listing
    (swap! spawned-threads conj {uuid
                                   {:name name
                                    :function function
                                    :start-time start-time
                                    :kill-switch kill-switch}}) ; Add threads to spawned-threads
    uuid)) ; Return the uuid to caller

(defn get-spawned-threads []
  @spawned-threads)










