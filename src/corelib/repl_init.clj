(ns corelib.repl-init
  (:use overtone.core))

(boot-external-server)
(volume 1.0)

(require 'corelib.core)
