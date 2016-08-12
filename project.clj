(defproject legacy "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :java-source-paths ["java"]
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]
                 [org.clojure/test.check "0.9.0"]]

  :profiles {:dev {:plugins [[com.jakemccrary/lein-test-refresh "0.16.0"]]
                   :dependencies [[pjstadig/humane-test-output "0.8.1"]]
                   :injections [(require 'pjstadig.humane-test-output)
                     (pjstadig.humane-test-output/activate!)]}}              )
