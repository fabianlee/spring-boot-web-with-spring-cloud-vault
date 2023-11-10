owner=fabianlee
app=$(basename $PWD)
version=latest

set -x
docker build -t $owner/$app:$version -f src/main/resources/docker/Dockerfile .
buildCode=$?
set +x
[ $buildCode -eq 0 ] || exit $buildCode

#docker run --name test1 -it --rm --security-opt seccomp=unconfined -e JAVA_OPTS="-Xms128M -Xmx256M" $owner/$app:$version -- /bin/bash

set -x
docker run --name test1 -it --rm --network=host --security-opt seccomp=unconfined -e JAVA_OPTS="-Xms128M -Xmx256M" $owner/$app:$version -- java -version
set +x
