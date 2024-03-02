#!/bin/sh

set -e

version="$1"
echo "Called with version: ${version}"

echo "Compiling with maven"
mvn --batch-mode --update-snapshots compile
cp target/battery-status-*.jar target/lib

echo "Running jlink"
jlink --module-path "./target/lib" \
--add-modules "battery.status" \
--launcher Launcher=battery.status/de.holube.batterystatus.Main \
--compress 2 \
--no-header-files \
--no-man-pages \
--strip-debug \
--output "./target/jlink-out"

echo "Running jpackage"
jpackage --type exe \
--verbose \
--input "./target" \
--app-image "./target/jlink-out" \
--resource-dir "./jpackage" \
--name "battery-status" \
--app-version "${version}" \
--dest "./target/dist" \
--vendor "Tilman Holube" \
--win-upgrade-uuid "19c20ba8-f49e-43e7-8efe-40970f2be007" \
--win-menu

ls -al ./target/dist

exit 0
