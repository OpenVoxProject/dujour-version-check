(def trapperkeeper-version "4.3.0")
(def trapperkeeper-webserver-jetty10-version "1.1.0")

(defproject org.openvoxproject/dujour-version-check "1.1.1-SNAPSHOT"
  :description "Dujour Version Check library"
  :license {:name "Apache-2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.txt"}

  :pedantic? :abort
  :min-lein-version "2.9.1"

  ;; These are to enforce consistent versions across dependencies of dependencies,
  ;; and to avoid having to define versions in multiple places. If a component
  ;; defined under :dependencies ends up causing an error due to :pedantic? :abort,
  ;; because it is a dep of a dep with a different version, move it here.
  :managed-dependencies [[org.clojure/clojure "1.12.4"]
                         [commons-codec "1.15"]

                         [org.bouncycastle/bcpkix-jdk18on "1.83"]
                         [org.bouncycastle/bcpkix-fips "1.0.8"]
                         [org.bouncycastle/bc-fips "1.0.2.6"]
                         [org.bouncycastle/bctls-fips "1.0.19"]

                         [org.openvoxproject/trapperkeeper ~trapperkeeper-version]
                         [org.openvoxproject/trapperkeeper ~trapperkeeper-version :classifier "test"]
                         [org.openvoxproject/trapperkeeper-webserver-jetty10 ~trapperkeeper-webserver-jetty10-version]
                         [org.openvoxproject/trapperkeeper-webserver-jetty10 ~trapperkeeper-webserver-jetty10-version :classifier "test"]]

  :dependencies [[org.clojure/clojure]
                 [org.clojure/tools.logging "1.2.4"]

                 [prismatic/schema "1.4.1"]
                 [ring/ring-codec "1.3.0"]
                 [cheshire "5.10.2"]
                 [trptcolin/versioneer "0.2.0"]
                 [slingshot "0.12.2"]

                 [org.openvoxproject/http-client "2.2.0"]]

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD
                                     :sign-releases false}]]

  :profiles {:provided {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}
             :defaults {:dependencies [[org.openvoxproject/trapperkeeper :classifier "test" :scope "test"]
                                       [org.openvoxproject/kitchensink "3.5.3" :classifier "test" :scope "test"]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10 :classifier "test"]
                                       [ring-mock "0.1.5"]]}
                                  
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
