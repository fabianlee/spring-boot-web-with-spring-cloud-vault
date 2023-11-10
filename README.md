# Example of SpringBoot webapp with Vault sidecar for secrets


## Project initially created using Spring Starter

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
