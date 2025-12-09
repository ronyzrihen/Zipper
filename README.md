### jenkins command
```bash
docker run -p 8080:8080 -v jenkins_home:/var/jenkins_home -d jenkins/jenkins:lts
```

### create a docker to allow jenkins building dockers
``` bash
docker run \            
  --name jenkins-docker \
  --rm \
  --detach \
  --privileged \
  --network jenkins \
  --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  --publish 2376:2376 \
  docker:dind
```
