javac -cp src/ -d bin/ src/com/robot/AI.java
java -Xmx2048m -cp bin/:lib/mysql-connector-java-5.1.25-bin.jar:commons-codec-1.9.jar:commons-logging-1.2.jar:fluent-hc-4.5.1.jar:htmllexer.jar:htmlparser.jar:httpclient-4.5.1.jar:httpclient-cache-4.5.1.jar:httpclient-win-4.5.1.jar:httpcore-4.4.3.jar:httpmime-4.5.1.jar:jna-4.1.0.jar:jna-platform-4.1.0.jar com.robot.AI
