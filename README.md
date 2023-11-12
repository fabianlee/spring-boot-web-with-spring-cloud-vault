# Spring Boot web using Cloud Vault library to fetch secrets directly from HashiCorp Vault server

This project is a Java Spring Boot web application that uses the Cloud Vault libraries
to fetch a secret directly from a HashiCorp Vault server.  

This code assumes the Vault Server uses the Kubernetes auth method, which means the 
Spring Boot web app should be deployed into a Kubernetes cluster running under a specific Kubernetes service account so it presents the correct JWT.

This app can fetch a secret from a remote or in-cluster Vault server without the need for a
 Vault sidecar by using the Cloud Vault libraries.  Generally, it is preferrable to communicate
 direcly to Vault so no intermediate secret representations are stored.

blog installing a dev Vault server into a Kubernetes cluster:

blog describing this Java Spring Boot web app:  

# Adding Vault sidecar

Even though the Spring Cloud Vault libraries allow a Spring application to fetch secrets directly from a Vault server, 
there are a multiple reasons you may want still to run a Vault sidecar:

  * Your particular app language does not have a fully-featured Vault client library
  * You want to shield your application from Vault server configuration details
  * You have a legacy application that must continue reading config/secrets from the filesystem/environment

This project also supports pointing at the Vault sidecar as well (http://localhost:8082).



## Create bootJar and OCI image with Docker

```
./gradlew
./gradlew bootJar
./gradlew docker [-Pversion=1.0.1 ]

```

# Creating tag

```
newtag=v1.0.1
git commit -a -m "changes for new tag $newtag" && git push
git tag $newtag && git push origin $newtag
```

# Deleting tag

```
# delete single local tag, then remote
todel=v1.0.1; git tag -d $todel && git push -d origin $todel
```

# Deleting release

```
todel=v1.0.1

# delete release and remote tag
gh release delete $todel --cleanup-tag -y

# delete local tag
git tag -d $todel
```

# Generate image locally then push latest to DockerHub during development lifecycle

```
# makes this image avaible on DockerHub: fabianlee/spring-boot-web-with-spring-cloud-vault:latest
buildah login --username <user> --password <password> docker.io
./gradlew buildah ; ./gradlew buildahPushDockerHub
```

# Restart deployment

```
kubectl rollout restart deployment spring-boot-web-vault -n vault
kubectl rollout status deployment  spring-boot-web-vault -n vault --timeout=90s
```


## Project initially created using Spring Starter

[Spring Starter Web UI](https://start.spring.io)

```
# define variables
id=spring-boot-web-with-spring-cloud-vault
artifact_id="${id//-}"
SpringAppClassName=SpringMain
version="0.0.1"
groupId="org.fabianlee"

# get starter and extract
curl https://start.spring.io/starter.tgz \
    -d type=gradle-project \
    -d dependencies=web,cloud-starter-vault-config \
    -d javaVersion=17 \
    -d bootVersion=3.0.12 \
    -d groupId=$groupId \
    -d artifactId=$artifact_id \
    -d name=$SpringAppClassName \
    -d baseDir=$id \
    -d version=$version \
| tar -xzvf -

cd $id

# make sure java17+
java --version

# download gradle wrapper
./gradlew
# run SpringBoot web app
./gradlew bootRun

```
