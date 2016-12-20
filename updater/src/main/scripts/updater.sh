#!/bin/bash

currentDir = `pwd`
java -jar updater.jar org.tanuneko.im.updater.Updater ${currentDir} $1 $2