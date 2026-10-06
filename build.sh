#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p build
javac --release 8 -encoding UTF-8 -d build src/*.java
jar cfe Airport_Flight_Board_Scheduler.jar AirportApp -C build .
echo "Built Airport_Flight_Board_Scheduler.jar"
