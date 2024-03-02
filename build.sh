#!/bin/sh

set -e

version="$1"
echo "Called with version: ${version}"

echo "Compiling with maven"
mvn --batch-mode --update-snapshots compile verify
cp "./target/battery-status-${version}.jar" "./target/lib"

echo "Running jpackage"
jpackage --type exe \
--verbose \
--module-path "./target/lib" \
--add-modules "battery.status/de.holube.batterystatus.Main" \
--resource-dir "./jpackage" \
--name "battery-status" \
--app-version "${version}" \
--dest "./target/dist" \
--vendor "Tilman Holube" \
--win-upgrade-uuid "19c20ba8-f49e-43e7-8efe-40970f2be007" \
--win-menu

ls -al ./target/dist

exit 0
