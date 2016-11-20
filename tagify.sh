#!/bin/sh
set -e

etags `find $PWD/src -name '*.java' -o -name '*.c' -o -name '*.h'`
