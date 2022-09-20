#!/bin/bash
set -euxo pipefail

pushd ../symphony-extension
  bash ./build.sh
popd

mvn spring-boot:build-image -DskipTests
