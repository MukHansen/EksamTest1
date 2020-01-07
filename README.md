Master  
[![Build Status](https://travis-ci.org/cph-ms782/Course-Assignment-3_Backend.svg?branch=master)](https://travis-ci.org/cph-ms782/Course-Assignment-3_Backend)  

Dev  
[![Build Status](https://travis-ci.org/cph-ms782/Course-Assignment-3_Backend.svg?branch=dev)](https://travis-ci.org/cph-ms782/Course-Assignment-3_Backend)  

# Course-Assignment-3  


### endpoints  

**All**  

 * /api/info  
 * /api/info/all  
 * /api/sw  
 * /api/login  

**User**  

 * /api/sw/data  
 * /api/info/user  

**Admin**  

 * /api/sw/data  
 * /api/info/user  
 * /api/info/admin  
 * /api/info/fill  

--------------------------------------------------------------
Nedenstående er en minimalistisk guide til opsætning i forbindelse med deployment igennem travis.
--------------------------------------------------------------

1.  MySql
    opret lokal(xxx + xxx_test) og droplet(xxx) database.

2. Netbeans Backend Projekt. (Project Files -> pom.xml).
    kontrollér pom.xml linie 18(remote.server).
    Husk at kontrollér at der bruges https og ikke http.

3. Netbeans Backend Projekt. 
(Source packages -> utils -> EMF_Creator.java)
    sørg for at connection i EMF_Creator linie 121 	"CONNECTION" er unik.

4. Droplet(Terminal/GitBash)
    ssh ind i dropletten, og brug
    - sudo nano /opt/tomcat/bin/setenv.sh -
    opret ny export med værdien fra "CONNECTION" i step 3.

5. Netbeans Backend Projekt
(Other sources -> src/main/resources -> default package -> config.properties)
    kontrollér database navne i config.properties.

6. Travis.yml fil i rod mappen.
    Ændre database navn i linie 40(CREATE DATABASE ??) til database navnet valgt i step 1.

7. https://travis-ci.org/
    på travis sæt REMOTE_USER til "script_user" og REMOTE_PW til script_user's password.
    
    -- brug følgende kommando for at finde password til brugeren hvis nødvendigt.
    sudo nano /opt/tomcat/conf/tomcat-users.xml



HUSK AT CLEAN AND BUILD HVIS DER ER ERRORS I IMPORTS.
