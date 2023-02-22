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


