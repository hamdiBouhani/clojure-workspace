## [Include a test source directory](https://clojure.org/guides/deps_and_cli#extra_paths)

Typically, the project classpath includes only the project source, not its test source by default. You can add extra paths as modifications to the primary classpath in the make-classpath step of the classpath construction. To do so, add an alias :test that includes the extra relative source path "test":

```
{:deps
 {org.clojure/core.async {:mvn/version "1.3.610"}}

 :aliases
 {:test {:extra-paths ["test"]}}}
```

Apply that classpath modification and examine the modified classpath by invoking clj -A:test -Spath:

```
$ clj -A:test -Spath
test:
src:
/Users/me/.m2/repository/org/clojure/clojure/1.11.1/clojure-1.11.1.jar:
... same as before (split here for readability)
```

Note that the test dir is now included in the classpath.

## Use a test runner to run all tests
You can extend the :test alias in the previous section to include the cognitect-labs test-runner for running all clojure.test tests:
https://github.com/cognitect-labs/test-runner

Extend the :test alias:

```
{:deps
 {org.clojure/core.async {:mvn/version "1.3.610"}}

 :aliases
 {:test {:extra-paths ["test"]
         :extra-deps {io.github.cognitect-labs/test-runner {:git/tag "v0.5.1" :git/sha "dfb30dd"}}
         :main-opts ["-m" "cognitect.test-runner"]
         :exec-fn cognitect.test-runner.api/test}}}
```

And then execute the test runner using the default config (run all tests in -test namespaces under the test/ dir):

>> clj -X:test

## Add an optional dependency

Aliases in the deps.edn file can also be used to add optional dependencies that affect the classpath:

```
{:aliases
 {:bench {:extra-deps {criterium/criterium {:mvn/version "0.4.4"}}}}}
```
Here the :bench alias is used to add an extra dependency, namely the criterium benchmarking library.

You can add this dependency to your classpath by adding the :bench alias to modify the dependency resolution: clj -A:bench.

## Add a dependency from the command line

It can be helpful to experiment with a library without adding it to an existing deps.edn file or creating one.

```
$ clojure -Sdeps '{:deps {org.clojure/core.async {:mvn/version "1.5.648"}}}'
Clojure 1.11.1
user=> (require '[clojure.core.async :as a])
nil
```

Note that due to escaping rules, it’s best to wrap the config data in single quotes.

## Preparing source dependency libs

Some dependencies will require a preparation step before they can be used on the classpath. These libs should state this need in their deps.edn:

```
{:paths ["src" "target/classes"]
 :deps/prep-lib {:alias :build
                 :fn compile
                 :ensure "target/classes"}}
```