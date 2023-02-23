# perfomance-glance

## How to find out the modules needed by jlink

Uncomment the `maven-dependency-plugin` plugin in `pom.xml`:

```xml
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>${maven.dependency.plugin.version}</version>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/libs</outputDirectory>
                            <overWriteReleases>false</overWriteReleases>
                            <overWriteSnapshots>false</overWriteSnapshots>
                            <overWriteIfNewer>true</overWriteIfNewer>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```
Then run the following commands in a terminal window:

```shell
mvn verify -DskipTests
jdeps --ignore-missing-deps -q -recursive --multi-release 19 \
  --print-module-deps --class-path 'target/libs/*' target/spring-todo-app.jar
```

## How to start the setup

Place yourself in the root directory and run :
```shell
docker-compose up --build
```
You can access the following endpoints:
 
| Application | Local Endpoint        |
|-------------|-----------------------|
| Todo App    | http://localhost:8080 |
| Prometheus  | http://localhost:9090 |
| Grafana     | http://localhost:300  |

Now create some requests by running:

```shell
sh scripts/load.sh
```

### How to detect missing JVM NativeMemoryTracking flag

Please take a look at /scripts/flag.sh and:

```shell
sh scripts/flag.sh
```

### How to track Native Memory Used 
Run

```shell
sh scripts/track.sh
```
### How to query stats
Run

```shell
sh scripts/query.sh
```

## Prometheus
### Look at CPU Load Machine Total vs HTTP server requests

```shell
rate(jdk_CPULoadmachineTotal[1m]) 
or rate(http_server_requests_seconds_sum{method="POST", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="GET", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="PUT", uri="/api/todo"}[1m])
```

### Look at Garbage Collection Longest Pause vs HTTP server requests

```shell
rate(jdk_GarbageCollectionlongestPause[1m]) 
or rate(http_server_requests_seconds_sum{method="POST", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="GET", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="PUT", uri="/api/todo"}[1m])
```

### Look at Garbage Collection Sum of Pauses vs HTTP server requests

```shell
rate(jdk_GarbageCollectionsumOfPauses[1m]) 
or rate(http_server_requests_seconds_sum{method="POST", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="GET", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="PUT", uri="/api/todo"}[1m])
```
### Look at GCHeapSummary Heap Used

```shell
rate(jdk_GCHeapSummaryheapUsed[1m]) 
or rate(http_server_requests_seconds_sum{method="POST", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="GET", uri="/api/todo"}[1m]) 
or rate(http_server_requests_seconds_sum{method="PUT", uri="/api/todo"}[1m])
```


