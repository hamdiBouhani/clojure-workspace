
## Clojure Tutorial 
### Getting Started with Clojure 
[How To Setup The Environment with VS Code](https://www.youtube.com/watch?v=WY4Yc03g-gU)

1- Install OpenJDK

2- Install Leingen Environment 

* [Leingen:](https://leiningen.org/)

Leiningen is the easiest way to use Clojure. With a focus on project automation and declarative configuration, it gets out of your way and lets you focus on your code.

* [Leingen Tutorial](https://codeberg.org/leiningen/leiningen/src/branch/stable/doc/TUTORIAL.md)

Leiningen is for automating Clojure projects without setting your hair on fire. If you experience your hair catching on fire or any other frustrations while following this tutorial, please let us know.

It offers various project-related tasks and can:

- create new projects
- fetch dependencies for your project
- run tests
- run a fully-configured REPL
- compile Java sources (if any)
- run the project (if the project isn't a library)
- generate a maven-style "pom" file for the project for interop
- compile and package projects for deployment
- publish libraries to repositories such as Clojars
- run custom automation tasks written in Clojure (leiningen plug-ins)

On ubuntu, its quite easy. Download executable file, make it executable and place it in system path.

[Installing leiningen 2 on Ubuntu](https://stackoverflow.com/questions/25838169/installing-leiningen-2-on-ubuntu)

```
    $ wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
    $ chmod +x lein
    $ sudo mv lein /usr/local/bin
```

You can also move it any directory which is in system path.

  ```
    $ lein -v
    Leiningen 2.6.1 on Java 1.8.0_77 Java HotSpot(TM) 64-Bit Server VM
  ```

3- install VS Code 

4- Install Plugin in VS Code for Clojure ENV

[Get Started with Clojure in VS Code :](https://www.youtube.com/watch?v=O6GrXXhCzCc)

See how to use the the Calva Extension with its Getting Started Clojure REPL to create your first Clojure program really quickly. Also find out how to use it to teach yourself the basics of Clojure.


# Creating a Project:
```
$ lein new app my-stuff
```

Generating a project called my-stuff based on the 'app' template.

```
$ # see how it looks like using the "tree" command
$ tree -F -a --dirsfirst my-stuff/

my-stuff/
├── doc/
│   └── intro.md
├── resources/
├── src/
│   └── my_stuff/
│       └── core.clj
├── test/
│   └── my_stuff/
│       └── core_test.clj
├── CHANGELOG.md
├── .gitignore
├── .hgignore
├── LICENSE
├── project.clj
└── README.md
```

In this example we're using the app template, which is intended for an application project rather than a library. Omitting the app argument will use the default template, which is suitable for libraries.


## Directory Layout

Here we've got your project's README, a `src/` directory containing the code, a `test/` directory, and a `project.clj` file which describes your project to Leiningen. The `src/my_stuff/core.clj` file corresponds to the `my-stuff.core` namespace.

## Filename-to-Namespace Mapping Convention:

Note that we use `my-stuff.core` instead of just my-stuff since single-segment namespaces are discouraged in Clojure as using those would imply classes are being assigned to the default (no-name) package.

Also note that if a Clojure namespaces segment contains a dash `(-)`, the corresponding `path/filename` will contain an underscore (_) instead. This is due to the fact that Java disallows dashes in identifiers, in particular in package and class names. A Clojure "dash-adorned" namespace identifier is thus mapped to a Java-compatible "underscore-adorned" package identifier. This change is reflected in pathnames as these must match the package and class names.

## project.clj:

Your `project.clj` file will start off looking something like this:

```clojure
(defproject my-stuff "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main ^:skip-aot my-stuff.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

```

If you don't fill in the :description with a short sentence, your project will be harder to find in search results, so start there. Be sure to fix the :url as well. At some point you'll need to flesh out the README.md file too, but for now let's skip ahead to setting :dependencies. Note that Clojure is just another dependency here. Unlike most languages, it's easy to swap out any version of Clojure.

## Dependencies
 
### Overview:
Clojure is a hosted language and Clojure libraries are distributed the same way as in other JVM languages: as jar files.

Jar files are basically just .zip files with a little extra JVM-specific metadata. They usually contain .class files (JVM bytecode) and .clj source files, but they can also contain other things like config files, JavaScript files or text files with static data.

Published JVM libraries have identifiers (artifact group, artifact id) and versions based on Maven naming conventions.

### Artifact IDs, Groups, and Versions:
You can search [Clojars](https://clojars.org/search?q=clj-http) using its web interface or via `lein search $TERM`.

```shell
hamdi@hamdi-XPS-9320:~$ lein search clj-http
Searching central ...
[pro.juxt.clojars-mirrors.clj-http/clj-http "3.12.2"]
Searching clojars ...
[clj-http "3.12.3"]
  A Clojure HTTP library wrapping the Apache HttpComponents client.
[clj-http-fake "1.0.3"]
  Helper for faking clj-http requests in testing, like Ruby's fakeweb.
[org.sharetribe/aws-sig4 "0.1.4"]
  Middleware to add AWS signature v4 signing to clj-http requests.
[clj-oauth2 "0.2.0"]
  clj-http and ring middlewares for OAuth 2.0
[b-ryan/clj-http-mock "0.6.0"]
  Mock responses to clj-http requests.
[clj-http-hystrix "0.1.6"]
  A Clojure library to wrap clj-http requests as hystrix commands
[org.clj-commons/clj-http-lite "1.0.13"]
  A lite version of clj-http that uses the jre's HttpURLConnection
[martian-clj-http "0.1.17-SNAPSHOT"]
  clj-http implementation for martian
[stuarth/clj-oauth2 "0.3.2"]
  clj-http and ring middlewares for OAuth 2.0
[org.clojars.osbert/clj-oauth2 "0.1.9"]
  clj-http and ring middlewares for OAuth 2.0
[org.clojars.ub-hyleung/clj-http-mock "0.5.0"]
  Mock responses to clj-http requests.
[clj-http-mock "0.4.0"]
  Mock responses to clj-http requests.
[onaio/clj-oauth2 "0.3.2"]
  clj-http and ring middlewares for OAuth 2.0
[com.github.oliyh/martian-clj-http "0.1.22-SNAPSHOT"]
  clj-http implementation for martian
[wkf/clj-http "2.0.0"]
  A Clojure HTTP library wrapping the Apache HttpComponents client.
[com.gfredericks.forks.clj-http-fake/clj-http-fake "1.0.3-37e38b42"]
  Helper for faking clj-http requests. For testing. You monster.
[opentable/clj-http "3.0.0-beta-1"]
  A Clojure HTTP library wrapping the Apache HttpComponents client.
[twosigma/clj-http "3.9.1-ts2"]
  A Clojure HTTP library wrapping the Apache HttpComponents client.
[mavericklou/clj-oauth2 "0.5.2"]
  clj-http and ring middlewares for OAuth 2.0
[thirtyspokes/hindrance "1.1.0"]
  A convenience wrapper for using OAuth JWT credentials flow with clj-http.
[com.telenordigital.data-insights/clj-oauth2 "0.7.2"]
  clj-http and ring middlewares for OAuth 2.0
[racehub/clj-http "1.0.1"]
  A Clojure HTTP library wrapping the Apache HttpComponents client.
[com.farmlogs/looper "0.3.0"]
  Drop-in clj-http replacement with retries
[emil0r/clj-oauth2 "0.6.0"]
  clj-http and ring middlewares for OAuth 2.0 [fork]
hamdi@hamdi-XPS-9320:~$ 
```
On the Clojars page for clj-http at the time of this writing it shows this:

```clojure
[clj-http "3.12.3"]
```

It also shows the Maven and Gradle syntax for dependencies. You can copy the Leiningen version directly into the `:dependencies` vector in `project.clj`. So for instance, if you change the :dependencies line in the example `project.clj` above to

```clojure
 :dependencies [[org.clojure/clojure "1.11.1"]
                [clj-http "3.12.3"]]
```
Leiningen will automatically download the clj-http jar file and make sure it is on your classpath. If you want to explicitly tell lein to download new dependencies, you can do so with `lein deps`, but it will happen on-demand if you don't.

Within the vector, "clj-http" is referred to as the "artifact id". "2.0.0" is the version. Some libraries will also have "group ids", which are displayed like this:

```[com.cedarsoft.utils.legacy/hibernate "1.3.7"]```

The group id is the part before the slash. Especially for Java libraries, it's often a reversed domain name. Clojure libraries often use the same group-id and artifact-id (as with clj-http), in which case you can omit the group-id. If there is a library that's part of a larger group (such as ring-jetty-adapter being part of the ring project), the group-id is often the same across all the sub-projects.

### Snapshot Versions


Sometimes versions will end in "-SNAPSHOT". This means that it is not an official release but a development build. Relying on snapshot dependencies is discouraged but is sometimes necessary if you need bug fixes, etc. that have not made their way into a release yet. However, snapshot versions are not guaranteed to stick around, so it's important that non-development releases never depend upon snapshot versions that you don't control. Adding a snapshot dependency to your project will cause Leiningen to actively go seek out the latest version of the dependency daily (whereas normal release versions are cached in the local repository) so if you have a lot of snapshots it will slow things down.


### Repositories

Dependencies are stored in artifact repositories. If you are familiar with Perl's CPAN, Python's Cheeseshop (aka PyPi), Ruby's rubygems.org, or Node.js's NPM, it's the same thing. Leiningen reuses existing JVM repository infrastructure. There are several popular open source repositories. Leiningen by default will use two of them: `clojars.org` and `Maven Central`.

`Clojars` is the Clojure community's centralized Maven repository, while Central is for the wider JVM community.

You can add third-party repositories by setting the `:repositories` key in `project.clj`. See the [sample.project.clj](https://codeberg.org/leiningen/leiningen/src/stable/sample.project.clj) for examples on how to do so. This sample uses additional repositories such as the Sonatype repository which gives access to the latest SNAPSHOT development version of a library (Clojure or Java). It also contains other relevant settings regarding repositories such as update frequency.

### Checkout Dependencies

Sometimes it is necessary to develop two or more projects in parallel, the main project and its dependencies, but it is very inconvenient to run `lein install` and restart your repl all the time to get your changes picked up. Leiningen provides a solution called checkout dependencies (or just checkouts). To use it, create a directory called checkouts in the project root, like so:

```
my-stuff/
│
├── checkouts/    <--- here
│
├── doc/
│   └── intro.md
├── resources/
├── src/
│   └── my_stuff/
│       └── core.clj
├── test/
│   └── my_stuff/
│       └── core_test.clj
├── CHANGELOG.md
├── .gitignore
├── .hgignore
├── LICENSE
├── project.clj
└── README.md
```

Then, under the checkouts directory, create symlinks to the root directories of projects you need. The names of the symlinks don't matter: Leiningen just follows all of them to find project.clj files to use. Traditionally, they have the same name as the directory they point to.

```
my-stuff/
├── checkouts/
│   ├── commons -> [link to /code/company/commons]
│   └── suchwow -> [link to /code/oss/suchwow]
.
```


## Running Code

Enough setup; let's see some code running. Start with a REPL (read-eval-print loop):

```clojure

  $ cd my-stuff
  $ lein repl
  nREPL server started on port 55568 on host 127.0.0.1 - nrepl://127.0.0.1:55568
  REPL-y 0.5.1, nREPL 0.8.3
  Clojure 1.10.1
  OpenJDK 64-Bit Server VM 1.8.0_222-b10
      Docs: (doc function-name-here)
            (find-doc "part-of-name-here")
    Source: (source function-name-here)
  Javadoc: (javadoc java-object-or-class-here)
      Exit: Control+D or (exit) or (quit)
  Results: Stored in vars *1, *2, *3, an exception in *e

  my-stuff.core=>

```

The REPL is an interactive prompt where you can enter arbitrary code to run in the context of your project. Since we've added clj-http to `:dependencies` earlier, we are able to load it here along with code from the my-stuff.core namespace in your project's own src/ directory:

```clojure
  my-stuff.core=> (require 'my-stuff.core)
  nil
  my-stuff.core=> (my-stuff.core/-main)
  Hello, World!
  nil
  my-stuff.core=> (require '[clj-http.client :as http])
  nil
  my-stuff.core=> (def response (http/get "https://leiningen.org"))
  #'my-stuff.core/response
  my-stuff.core=> (keys response)
  (:status :headers :body :request-time :trace-redirects :orig-content-encoding)
```

The call to `-main` shows both println output ("Hello, World!") and the return value (nil) together.

Built-in documentation is available via doc, and you can examine the source of functions with source:

```clojure 
my-stuff.core=> (source -main)
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
nil

my-stuff.core=> ; use control+d to exit
```


If you already have code in a -main function ready to go and don't need to enter code interactively, the run task is simpler:

```shell
$ lein run
Hello, World!
```

Providing a `-m` argument will tell Leiningen to look for the `-main` function in another namespace. Setting a default :main in project.clj lets you omit -m.

For `long-running` lein run processes, you may wish to save memory with the higher-order trampoline task, which allows the Leiningen JVM process to exit before launching your project's JVM.

```shell
$ lein trampoline run -m my-stuff.server 5000
```
If you have any Java to be compiled in :java-source-paths or Clojure namespaces listed in :aot, they will always be compiled before Leiningen runs any other code, via any run, repl, etc. invocations.

### Tests

We haven't written any tests yet, but we can run the failing tests included from the project template:

```clojure
$ lein test

lein test my-stuff.core-test

lein test :only my-stuff.core-test/a-test

FAIL in (a-test) (core_test.clj:7)
FIXME, I fail.
expected: (= 0 1)
  actual: (not (= 0 1))
```
Ran 1 tests containing 1 assertions.
1 failures, 0 errors.
Tests failed.
Once we fill it in the test suite will become more useful. Sometimes if you've got a large test suite you'll want to run just one or two namespaces at a time; lein test my-stuff.core-test will do that. You also might want to break up your tests using test selectors; see lein help test for more details.

Running lein test from the command-line is suitable for regression testing, but the slow startup time of the JVM makes it a poor fit for testing styles that require tighter feedback loops. In these cases, either keep a repl open for running the appropriate call to clojure.test/run-tests or look into editor integration such as clojure-test-mode.

Keep in mind that while keeping a running process around is convenient, it's easy for that process to get into a state that doesn't reflect the files on disk: functions that are loaded and then deleted from the file will remain in memory, making it easy to miss problems arising from missing functions (often referred to as "getting slimed"). Because of this it's advised to do a lein test run with a fresh instance periodically in any case, perhaps before you commit.

