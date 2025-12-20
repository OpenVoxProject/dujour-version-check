(defproject org.openvoxproject/dujour-version-check "1.0.1-SNAPSHOT"
  :description "Dujour Version Check library"

  :parent-project {:coords [org.openvoxproject/clj-parent "7.4.1-SNAPSHOT"]
                   :inherit [:managed-dependencies]}

  :plugins [[lein-parent "0.3.8"]]

  :dependencies [[org.clojure/clojure]
                 [org.clojure/tools.logging]
                 [prismatic/schema]
                 [org.openvoxproject/http-client]
                 [ring/ring-codec]
                 [cheshire]
                 [trptcolin/versioneer]
                 [slingshot]]

  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD
                                     :sign-releases false}]]

  :profiles {:provided {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}
             :defaults {:dependencies [[org.openvoxproject/trapperkeeper :classifier "test" :scope "test"]
                                       [org.openvoxproject/kitchensink :classifier "test" :scope "test"]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty9]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty9 :classifier "test"]
                                       [ring-mock "0.1.5"]]}
             :dev [:defaults {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}]
             :fips [:defaults {:dependencies [[org.bouncycastle/bctls-fips]
                                              [org.bouncycastle/bcpkix-fips]
                                              [org.bouncycastle/bc-fips]]
                               :jvm-opts ~(let [version (System/getProperty "java.version")
                                                [major minor _] (clojure.string/split version #"\.")
                                                unsupported-ex (ex-info "Unsupported major Java version. Expects 8 or 11."
                                                                 {:major major
                                                                  :minor minor})]
                                            (condp = (java.lang.Integer/parseInt major)
                                              1 (if (= 8 (java.lang.Integer/parseInt minor))
                                                  ["-Djava.security.properties==./dev-resources/java.security.jdk8-fips"]
                                                  (throw unsupported-ex))
                                              11 ["-Djava.security.properties==./dev-resources/java.security.jdk11-fips"]
                                              17 ["-Djava.security.properties==./dev-resources/java.security.jdk17-fips"]
                                              21 ["-Djava.security.properties==./dev-resources/java.security.jdk21-fips"]
                                              (throw unsupported-ex)))}]})
