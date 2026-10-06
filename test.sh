#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p test-build
javac --release 8 -encoding UTF-8 -d test-build src/*.java tests/*.java
java -cp test-build FlightSchedulerTests
