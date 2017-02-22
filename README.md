# WebDisk

This is kind of useless web application
The task was just to practice several technologies:

+ Servlet/JSP, JSTL tags
+ JDBC, use DataSource pool
+ MySQL
+ Maven
+ Implement a security realm

### Bonus tasks:
- add some Javadoc
- make some unit tests with JUnit; use Mockito to mock objects
- add TLS/SSL

----------

ConnectionPool class uses static methods to access schema (webdisk)
it looks for database resource defined in /src/main/webapp/META-INF/context.xml
it uses it as DataSource

UserDBTest class connects to DB directly using /src/test/resources/mytest.properties
it uses another temporary schema (temp_xxx) which it deletes after it finishes

    /src/test/resources/tables.sql should be used once manually to create initial tables
		DROP DATABASE IF EXISTS webdisk;
		CREATE DATABASE webdisk;
		USE webdisk;
		<execute tables.sql file>
and this file is also used by UserDBTest class when it creates the same tables in the temporary schema

--------------

## HTTPS(TLS/SSL):
```
generated ".keystore" file
	cd \Program Files\Java\jdk1.8.0_73\bin
	keytool -genkey -alias tomcat -keyalg RSA
moved ".keystore" file to C:\dev\workspace\Servers
Added to "C:\dev\workspace\Servers\Tomcat v8.0 Server at localhost-config\server.xml" :
	<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS" 
           keystoreFile="C:/dev/workspace/Servers/.keystore" keystorePass="changeit"/>
```
         
## File download/upload examples

+ Download examples:  
	(!) Java Servlet Download File Example: http://www.codejava.net/java-ee/servlet/java-servlet-download-file-example  
	Java servlet to download file from database: http://www.codejava.net/java-ee/servlet/java-servlet-to-download-file-from-database  
	Servlet code to download text file from website: https://www.mkyong.com/servlet/servlet-code-to-download-text-file-from-website-java/  
	How to download file from website: https://www.mkyong.com/java/how-to-download-file-from-website-java-jsp/  
	How to return a binary file as an attachment: Joel Murach, Michael Urban - Murach's Java Servlets and JSP; page 564  
  
+ Upload examples:  
	(!) Upload files to database (Servlet + JSP + MySQL): http://www.codejava.net/coding/upload-files-to-database-servlet-jsp-mysql  
	Java File Upload Example with Servlet 3.0 API: http://www.codejava.net/java-ee/servlet/java-file-upload-example-with-servlet-30-api  
	Insert file data into MySQL database using JDBC: http://www.codejava.net/java-se/jdbc/insert-file-data-into-mysql-database-using-jdbc  

--------------

## Building project
Local build (need local MySQL running if you want to run tests):  
`mvn clean package -DskipTests`

Run locally:  
This way it won't work cause web-runner.jar does not have tomcat-dbcp-8.0.33.jar and it won't construct DB resource declared in `META-INF/context.xml`  
`java -jar  target/dependency/webapp-runner.jar target/*.war --enable-naming --path /MyWebdisk`

Run locally and remotely:  
`java -cp 'target/dependency/*' webapp.runner.launch.Main target/*.war --enable-naming --path /MyWebdisk`

Deploy war:  
This way it won't work as you can't change the Procfile and so you can't specify classpath to `target/dependency/tomcat-dbcp-8.0.33.jar`  
`mvn clean heroku:deploy-war -DskipTests`

Deploy jar:  
`mvn clean heroku:deploy -DskipTests`
