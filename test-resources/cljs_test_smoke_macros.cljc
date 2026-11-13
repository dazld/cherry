(ns cljs-test-smoke-macros)

;; Macros that use the SCI ctx at expansion time (resolve, macroexpand-1),
;; as e.g. core.async's go does.
(defmacro expands-to-if? [form]
  (= (quote if) (first (macroexpand-1 form))))

(defmacro resolves? [sym]
  (some? (resolve sym)))

#?(:cherry
   (defmethod cljs.test/assert-expr 'smoke/throws?
     [_menv msg form]
     (let [body (rest form)]
       `(try
          (do ~@body)
          (cljs.test/report {:type :fail :message ~msg :expected '~form :actual "no exception thrown"})
          (catch :default e#
            (cljs.test/report {:type :pass :message ~msg :expected '~form :actual e#})
            e#)))))
