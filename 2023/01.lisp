;;; Advent of Code 2023
;;; Day 1: Trebuchet?!


(defun extract-nums (line)
  "Removes all non-numeric characters from an input string. Returns the string with only numbers."
  (with-output-to-string (s)  ; stream to string
    (let ((index 0))
      (loop for c across line   ; for each char in the input string
        ; check to see if the char is numeric (ascii code 48-57)
        do (cond
             ((and (> (char-code c) 47) (< (char-code c) 58)) (write-char c s)))
           (cond ((< index (- (length line) 2))
             (cond ((string= (subseq line index (+ index 3)) "one") (write-char #\1 s)))
             (cond ((string= (subseq line index (+ index 3)) "two") (write-char #\2 s)))
             (cond ((string= (subseq line index (+ index 3)) "six") (write-char #\6 s)))))
           (cond ((< index (- (length line) 3))
             (cond ((string= (subseq line index (+ index 4)) "zero") (write-char #\0 s)))
             (cond ((string= (subseq line index (+ index 4)) "four") (write-char #\4 s)))
             (cond ((string= (subseq line index (+ index 4)) "five") (write-char #\5 s)))
             (cond ((string= (subseq line index (+ index 4)) "nine") (write-char #\9 s)))))
           (cond ((< index (- (length line) 4))
             (cond ((string= (subseq line index (+ index 5)) "three") (write-char #\3 s)))
             (cond ((string= (subseq line index (+ index 5)) "seven") (write-char #\7 s)))
             (cond ((string= (subseq line index (+ index 5)) "eight") (write-char #\8 s)))))
           (setq index (+ index 1))))
      s))  ; return string


(defun first-last (str)
  "Returns the first and last char of a string"
  (with-output-to-string (s)
    (cond
      ((< 0 (length str))
          (write-char (char str 0) s)
          (write-char (char str (- (length str) 1)) s)))
    s))


(defun read-file (filename)
  "Loops through lines of input file, calls helper functions to get sum of outer numbers."
  (with-open-file (stream filename)
    (let ((sum 0))
      (loop for line = (read-line stream nil)
        while line
          do (let ((num (first-last (extract-nums line))))
             (cond ((< 0 (length num)) (setq sum (+ sum (parse-integer num)))))))
      (princ sum))))


(read-file "input.txt")
