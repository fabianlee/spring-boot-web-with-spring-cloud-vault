# SpringBoot webapp with Vault sidecar for secrets


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
# makes this image avaible on DockerHub: fabianlee/spring-boot-with-vault-sidecar:latest
buildah login --username <user> --password <password> docker.io
./gradlew buildah ; ./gradlew buildahPushDockerHub
```

# Restart deployment

```
kubectl rollout restart deployment spring-boot-web-sidecar -n vault && kubectl rollout status deployment spring-boot-web-sidecar -n vault --timeout=90s
```


## Project initially created using Spring Starter

[Spring Starter Web UI](https://start.spring.io)

```
# define variables
id=spring-boot-with-vault-sidecar
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
