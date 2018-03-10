# Architecture

## Domain Layer
- domain
    - model
    - repository: インターフェイスを持つのみで、実体はもたない
    - usecase: ドメインサービスと呼ばれるようなもの
## Application Layer
- service: アプリケーションサービス    


# Run
```
docker build -t tascala_mysql .

docker run --name tascala_mysql -e MYSQL_ROOT_PASSWORD=mysql -d -p 3306:3306 tascala_mysql
```

```
source .env
```

    
    