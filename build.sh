#!/bin/sh

set -e

version="$1"
echo "Called with version: ${version}"

echo "Compiling with maven"
mvn --batch-mode --update-snapshots compile install

echo "Running jpackage"
jpackage --type exe \
--verbose \
--input "./target/native" \
--module-path "./target/battery-status-${version}.jar" \
--add-modules "battery.status" \
--module "battery.status/de.holube.batterystatus.Main" \
--java-options "--enable-native-access=battery.status" \
--java-options "-Xint" \
--java-options "-XX:ActiveProcessorCount=2" \
--java-options "-XX:+UseG1GC" \
--java-options "-XX:+UseStringDeduplication" \
--java-options "-XX:MaxHeapFreeRatio=10" \
--java-options "-XX:MinHeapFreeRatio=5" \
--resource-dir "./jpackage" \
--name "battery-status" \
--app-version "${version}" \
--dest "./target/dist" \
--vendor "Tilman Holube" \
--win-upgrade-uuid "19c20ba8-f49e-43e7-8efe-40970f2be007" \
--win-menu

ls -al ./target/dist

exit 0
