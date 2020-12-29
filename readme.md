# Stock Collector (PSCollector)

Collect information including company list, company statistics and company quotes.

First load company list and statistics from database or disk, then schedule updating the information.

## Build

Download dependency packages and generate .classpath and .project file for eclipse.

```bash
gradlew eclipse
```

Generate jar file in libs folder.
```bash
gradlew bootRepackage
```

## Run

```bash
java -jar -Xmx8g PSCollector.jar [-n]
```
-n: execute collector immediately, and then schedule updating.

## Database
1. 安装使用postgresql 9.5.6版本
2. 导入备份：psql -U postgres -d ps < ps_backup.sql
