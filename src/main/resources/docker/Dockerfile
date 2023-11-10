#
# Used to build minimal base image that takes care of custom packages, users, and other run commands
# that jib does not handle
#
# Then we have gradle+jib do the packaging on top of this minimal image
#
# https://stackoverflow.com/questions/58686955/docker-cmd-or-run-using-jib-build
#
# openjdk is deprecated
FROM eclipse-temurin:21.0.1_12-jre-jammy

# load specific set of OS packages for testing (ps,curl,swaks,nc,netstat,jq)
# create non-root user and group
# -l and static IDs assigned to avoid delay in lookups and system logging
# modification of 'docker-clean' needed on jammy to avoid post-install apt error trying to clean files
ARG THE_USER_ID=1001
ARG THE_GROUP_ID=1001
RUN DEBIAN_FRONTEND=noninteractive && \
  ls -l /etc/apt/apt.conf.d && \
  sed -i -e 's/^APT/#APT/' -e 's/^DPkg/#DPkg/' /etc/apt/apt.conf.d/docker-clean && \
  cat /etc/apt/apt.conf.d/docker-clean && \
  apt update && \
  apt install -q -y -o Dpkg::Options::="--force-confnew" procps curl swaks netcat net-tools jq && \
  rm -rf /var/lib/apt/lists/* && \
  /usr/sbin/groupadd -g $THE_GROUP_ID spring && \
  /usr/sbin/useradd -l -u $THE_USER_ID -G spring -g $THE_GROUP_ID spring && \
  mkdir logs && chgrp spring logs && chmod ug+rwx logs

##### EVERYTHING BELOW IS APP SPECIFIC AND WILL BE DONE BY JIB IN NEXT LAYER ######

# run as non-root
USER spring:spring

# main REST service and OpenAPI /swagger-ui/index.html
EXPOSE 8080

# files needed to run
COPY build/libs/springbootwithvaultsidecar.jar springbootwithvaultsidecar.jar
COPY README.md .

# for debugging
#CMD ["/bin/bash"]
CMD ["java","-jar","springbootwithvaultsidecar.jar"]


