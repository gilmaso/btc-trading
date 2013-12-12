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

(ns btc-trading.helpers)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; This file contains helpful functions which, generally speaking,  ;
; convert some data into other data or test data for some quality. ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Value testing ;;
(def not-nil? (complement nil?))



;; Data munging ;;
(defn seq-to-csv [seq]
  "Returns a csv string from a sequence."
  (if (not-nil? seq)
    (if (> (count seq) 1)
      (apply str (interpose "," seq))
      (get seq 0))
    []))

