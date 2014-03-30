#!/bin/sh
BASE_DIR="${PWD}"

cd $BASE_DIR/project-code
$BASE_DIR/play-${PLAY_VERSION}/play test
cd $BASE_DIR/samples/sample-using-httpException/
$BASE_DIR/play-${PLAY_VERSION}/play test
cd $BASE_DIR
