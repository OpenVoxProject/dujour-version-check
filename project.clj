(defproject org.openvoxproject/dujour-version-check "1.1.2"
  :description "Dujour Version Check library"
  :license {:name "Apache-2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.txt"}

  :pedantic? :abort
  :min-lein-version "2.9.1"

  ;; Generally, try to keep version pins in :managed-dependencies and the libraries
  ;; this project actually uses in :dependencies, inheriting the version from
  ;; :managed-dependencies. This prevents endless version conflicts due to deps of deps.
  ;; Renovate should keep the versions largely in sync between projects.
  :managed-dependencies [[org.clojure/clojure "1.12.4"]
                         [org.clojure/tools.logging "1.3.1"]
                         [cheshire "5.13.0"]
                         [clj-time "0.15.2"]
                         [commons-codec "1.20.0"]
                         
                         [org.slf4j/slf4j-api "2.0.17"]
                         [prismatic/schema "1.4.1"]
                         [ring-mock "0.1.5"]
                         [ring/ring-codec "1.3.0"]
                         [slingshot "0.12.2"]
                         [trptcolin/versioneer "0.2.0"]
                         

                         [org.bouncycastle/bcpkix-jdk18on "1.83"]
                         [org.bouncycastle/bcpkix-fips "1.0.8"]
                         [org.bouncycastle/bc-fips "1.0.2.6"]
                         [org.bouncycastle/bctls-fips "1.0.19"]
                         [org.openvoxproject/http-client "2.2.3"]
                         [org.openvoxproject/kitchensink "3.5.5" :classifier "test" :scope "test"]
                         [org.openvoxproject/trapperkeeper "4.3.2"]
                         [org.openvoxproject/trapperkeeper "4.3.2" :classifier "test"]
                         [org.openvoxproject/trapperkeeper-webserver-jetty10 "1.1.3"]
                         [org.openvoxproject/trapperkeeper-webserver-jetty10 "1.1.3" :classifier "test"]]

  :dependencies [[org.clojure/clojure]
                 [org.clojure/tools.logging]

                 [prismatic/schema]
                 [ring/ring-codec]
                 [cheshire]
                 [trptcolin/versioneer]
                 [slingshot]

                 [org.openvoxproject/http-client]]

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD
                                     :sign-releases false}]]

  :profiles {:provided {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}
             :defaults {:dependencies [[org.openvoxproject/kitchensink :classifier "test" :scope "test"]
                                       [org.openvoxproject/trapperkeeper :classifier "test" :scope "test"]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10 :classifier "test"]
                                       [ring-mock]]}
                                  
             :dev-settings {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}
             :dev [:defaults :dev-settings]

             :fips-settings {:dependencies [[org.bouncycastle/bctls-fips]
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
                                              (throw unsupported-ex)))}
             :fips [:defaults :fips-settings]})
