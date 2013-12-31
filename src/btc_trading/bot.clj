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


(ns btc-trading.bot
  (:require [btc-trading.persistence :as d]
            [btc-trading.polling :as polling :only (spawn-thread
                                                    get-spawned-threads)]
            [btc-trading.btc-china :as btcc]))

(defn record-btcchina []
  "Stores btcchina market depth data in redis:
  Format: btcc:md:<time>:<market depth data"
  (let [current-time (str (System/currentTimeMillis))
        domain-key "btcc:md:"
        redis-value (btcc/get-market-depth 10)]
    (d/disk-append "timeseries" current-time)
    (d/disk-set (str domain-key current-time) redis-value)
    (str domain-key current-time)))

