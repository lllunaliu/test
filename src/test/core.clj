(ns test.core
    (:require
    [opennlp.nlp :as onlp]
    [clojure.java.io :as io]))

(def detokenize
    (onlp/make-detokenizer "models/english-detokenizer.xml"))

(import '[org.jsoup Jsoup])

;strip the html tag
(defn strip-html-tags
    [s]
    (.text (Jsoup/parse s)))


;tokenize the content of webpage
(defn get-content [url]
    (map #(.toLowerCase %)(re-seq #"\w+" (strip-html-tags(slurp url)))))

;save the result to the txt file named "result.txt"
(defn write-data [xs] (with-open [wrtr (io/writer "result.txt":append true)]
(doseq [[x,y] (seq xs)] (.write wrtr (str x " " y "\n")))))

;count the word frequencies at each length of the word tuple
(defn basic-count [content length]
        (->> content
        (partition length 1)
        (map detokenize)
        frequencies
        sort
        write-data))



;do the frequency count for word tuples 1 to 4
(defn word-count [url]
    (spit "result.txt" "")
    (map #(basic-count (get-content url) %) (range 1 5)))




