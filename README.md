Pre -requisites


Docker installed and running  ( https://docs.docker.com/desktop/install/mac-install/ )
kubectl ( https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/#install-with-homebrew-on-macos )
minikube ( https://minikube.sigs.k8s.io/docs/start/ )
```
#summmary of commands
brew install kubectl 
brew install minikube  
minikube start
dapr init --kubernetes --wait
dapr status -k
```

Adding redis for pub/sub and state store


```
brew install helm
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install redis bitnami/redis --set image.tag=6.2
kubectl get pods
```
Will see the redis container running in the Kubernetes cluster

Adding components

Inside the project daprDemo create the components folder and add the redis-state and redis-pubsub yaml files.
```
kubectl apply -f components/redis-state.yaml
kubectl apply -f components/redis-pubsub.yaml
```

Should see this message
```
component.dapr.io/pubsub created
component.dapr.io/statestore created
```




Create Java Springboot modules app

Create daprDemo by IntelliJ spring boot app  with all developer tools checked (Spring version 2.7.3, lombok)

IMPORTANT NOTE: execute the following command in every terminal you use to set the docker repository to be accesed by k8s:
```
eval $(minikube docker-env)
```




Adding InsightEngine app to k8s cluster


Build the maven app
```
mvn clean install
```
testing it out
```
dapr run --app-id insight-engine --components-path ../components -- java -jar target/InsightEngine-0.0.1-SNAPSHOT.jar
```
build the docker image with the latest changes
```
docker build -t insight-engine:1.0 .
```


Adding ApplicationProcessingService app to k8s cluster

Build the maven app
```
mvn clean install
```
testing it out
```
dapr run --app-port 8080 --app-id application-processing --components-path ../components -- java -jar target/ApplicationProcessingService-0.0.1-SNAPSHOT.jar
```
build the docker image with the latest changes
```
docker build -t application-processing:1.0 .
```
finally deploy on k8s cluster
```
kubectl apply -f deploy/local.yaml
```
ó
```
kubectl create -f deploy/local.yaml
```

See the logs and should find the messages  of publisher and subscriber events:
kubectl logs --selector=app=insight-engine -c insight-engine --tail=-1



```
kubectl logs --selector=app=application-processing -c application-processing --tail=-1
```



Zipkin for distributed tracing


```
kubectl create deployment zipkin --image openzipkin/zipkin
kubectl apply -f components/observability.yaml
```
Start the Zipkin service dashboard
```
kubectl port-forward svc/zipkin 9411:9411
```

On your browser, go to http://localhost:9411 and you should see the Zipkin UI.