#!/bin/sh

cd project-code
../play-${PLAY_VERSION}/play test
cd ../samples/sample-using-httpException/
../play-${PLAY_VERSION}/play test
cd ../..