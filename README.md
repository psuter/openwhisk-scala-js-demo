# OpenWhisk Scala.js sample actions

This repository contains two sample OpenWhisk actions written in Scala, with [Scala.js](https://scala-js.org).

To build and run the first one:

    cd hello-whisk
    sbt fullOptJS
    wsk action create hello-scala target/scala-2.11/hello-whisk-opt.js
    wsk action invoke -br hello-scala -p name reader

To build and run the second one:

    cd mnemonics
    sbt fullOptJS
    wsk action create mnemonics-scala target/scala-2.11/whisk-mnemonics-opt.js
    wsk action invoke -br mnemonics-scala -p number '"673694475"'
