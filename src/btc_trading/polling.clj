(ns btc-trading.polling)


(defn spawn-thread [function interval]
  "Takes a function to be repeatedly called at a given interval.
  Returns a kill switch
  Takes: function
        interval in milliseconds
  Returns: promise"
  (let [kill-switch (promise)]
    (future (while (not (realized? kill-switch))
              (do
                (Thread/sleep interval)
                (eval function))))
    kill-switch))




