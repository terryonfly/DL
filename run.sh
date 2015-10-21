javac -cp src/:lib/mysql-connector-java-5.1.25-bin.jar:lib/htmllexer.jar:lib/htmlparser.jar -d bin/ src/com/robot/AI.java
java -Xmx2048m -cp bin/:lib/mysql-connector-java-5.1.25-bin.jar:lib/htmllexer.jar:lib/htmlparser.jar com.robot.AI
