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

(ns btc-trading.polling
  (:gen-class))


(def ^:private spawned-threads (atom {}))


; Public functions

(defn spawn-thread [function interval name]
  "Takes a '(function) to be repeatedly called at a given interval.
  Also, takes a name which is simply metadata for display purposes.
  Returns a uuid."
  (let [kill-switch (promise)
        uuid (keyword (str (java.util.UUID/randomUUID)))
        start-time (System/currentTimeMillis)]
    (future
      (while (not (realized? kill-switch))
              (do
                (Thread/sleep interval)
                (println (System/currentTimeMillis))
                (eval function)))
      (swap! spawned-threads dissoc uuid)) ; Remove thread from spawned-threads listing
    (swap! spawned-threads conj {uuid
                                   {:name name
                                    :function function
                                    :start-times start-time
                                    :kill-switch kill-switch}}) ; Add threads to spawned-threads
    uuid)) ; Return the uuid to caller

(defn get-spawned-threads []
  @spawned-threads)










