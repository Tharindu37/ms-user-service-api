```C:\keycloak-26.0.7\bin>kc.bat start-dev```
```
docker pull mysql
docker run --name mysql-db -e MYSQL_ROOT_PASSWORD=root -p 3333:3306 -d mysql:latest
docker exec -it mysql-db bash
mysql -u root -p
```
```
docker run -p 8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.0.7 start-dev
```