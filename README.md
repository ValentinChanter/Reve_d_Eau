<p align="center">
  <a href="https://github.com/ValentinChanter/Reve_d_Eau">
    <img  src="https://i.imgur.com/ut5JvtJ.png" height="62" alt="logo">
    <h3 align="center">Rêve d'Eau</h3>
  </a>
</p>

<br/>

## Introduction

This web application is a marketplace that sell bottles of water.

[JEE](https://jakarta.ee/)'s JSP pages are used for the frontend along [Tailwind CSS](https://tailwindcss.com/) while Servlets are used for the backend.


## Requirements

- [Apache Tomcat](https://tomcat.apache.org/) 10.1.13 or newer
- Any of the following:
    - [OpenJDK](https://openjdk.org/) 19.0.2 or newer  
    - [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) 21.0.2 or newer
- [Docker](https://www.docker.com/) 25.0.1 or newer

Please refer to the setting up guides of above programs if some environment variables such as `JAVA_HOME` and `CATALINA_HOME` are needed during the following but not defined.

## Setting up

1. Clone this repo and access it
	```bash
	git clone 'https://github.com/ValentinChanter/Reve_d_Eau'
	cd Reve_d_Eau
	```

2. Load the database schemata
   	```bash
	java -jar scalardb-schema-loader-3.12.3.jar --config src/main/resources/META-INF/users.properties --schema-file users.json --coordinator
	java -jar scalardb-schema-loader-3.12.3.jar --config src/main/resources/META-INF/articles.properties --schema-file articles.json --coordinator
	```

3. Start the container running the Cassandra database
	```bash
	docker compose up -d
	```

4. Build the application
   - On Windows
   ```bash
   .\mvnw.cmd clean package
   ```

   - On Unix-based systems
   ```bash
   ./mvnw clean package
   ```
   
## Usage

1. Deploy the website using Tomcat
   - On Windows
	```bash
	<path\to\tomcat>\bin\catalina.bat run target\Marketplace-x.y.war
	```
	where `x.y` is the version of the project (e.g. `C:\Program Files\Apache Software Foundation\Tomcat 10.1\bin\catalina.bat run target\Marketplace-1.0.war`)

	- On Unix-based systems
	```bash
	<path/to/tomcat>/bin/catalina.sh run target/Marketplace-x.y.war
	```
	where `x.y` is the version of the project (e.g. `/opt/tomcat/bin/catalina.sh run target/Marketplace-1.0.war`)

2. Open [http://localhost:8080/Marketplace_war](http://localhost:8080/Marketplace_war) with your browser to see the app.
