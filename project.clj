(defproject org.openvoxproject/dujour-version-check "1.1.0"
  :description "Dujour Version Check library"
  :license {:name "Apache-2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.txt"}

  :parent-project {:coords [org.openvoxproject/clj-parent "7.6.3"]
                   :inherit [:managed-dependencies]}

  :plugins [[lein-parent "0.3.9"]]

  :dependencies [[org.clojure/clojure]
                 [org.clojure/tools.logging]
                 [prismatic/schema]
                 [org.openvoxproject/http-client]
                 [ring/ring-codec]
                 [cheshire]
                 [trptcolin/versioneer]
                 [slingshot]]

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD
                                     :sign-releases false}]]

  :profiles {:provided {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}
             :defaults {:dependencies [[org.openvoxproject/trapperkeeper :classifier "test" :scope "test"]
                                       [org.openvoxproject/kitchensink :classifier "test" :scope "test"]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10 :classifier "test"]
                                       [ring-mock "0.1.5"]]}
             :dev [:defaults {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}]
             :fips [:defaults {:dependencies [[org.bouncycastle/bctls-fips]
                                              [org.bouncycastle/bcpkix-fips]
                                              [org.bouncycastle/bc-fips]]
                               :jvm-opts ~(let [version (System/getProperty "java.version")
                                                [major minor _] (clojure.string/split version #"\.")
                                                unsupported-ex (ex-info "Unsupported major Java version. Expects 17 or 21."
                                                                 {:major major
                                                                  :minor minor})]
                                            (condp = (java.lang.Integer/parseInt major)
                                              17 ["-Djava.security.properties==./dev-resources/java.security.jdk17-fips"]
                                              21 ["-Djava.security.properties==./dev-resources/java.security.jdk21-fips"]
                                              (throw unsupported-ex)))}]})
