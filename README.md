# clojure-ws
repository for testing the Clojure programming language



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