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

(ns btc-trading.persistence
  (:require [taoensso.carmine :as car :refer (wcar)]))


(def ^:private server1-conn {:pool {} :spec {}})

(defmacro ^:private wcar* [& body] `(car/wcar server1-conn ~@body))


; Public functions
(defn disk-get [key]
  (wcar* (car/get key)))

(defn disk-set [key value]
  (wcar* (car/set key value)))

