#!/bin/sh

set -e

echo "Building client jar"
mvn --batch-mode --update-snapshots install package

echo "Running jpackage"
jpackage --type exe \
--verbose \
--input "./target" \
--main-jar "./battery-status.jar" \
--resource-dir "./jpackage" \
--name "battery-status" \
--app-version "2.0.0" \
--dest "./target/dist" \
--win-upgrade-uuid "19c20ba8-f49e-43e7-8efe-40970f2be007"

exit 0
