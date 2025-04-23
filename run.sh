#!/bin/zsh

docker build -t tonga-tourism .
docker run -p 80:80 tonga-tourism