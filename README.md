# apache-cassandra-spring-boot

### Create AWS Keyspaces YAML (keyspaces.cform):
```yaml
AWSTemplateFormatVersion: '2010-09-09'
Description: AWS CloudFormation template for creating an Amazon Keyspaces Keyspace and Table.

Parameters:
  KeyspaceName:
    Description: Keyspace Name
    Type: String
    NoEcho: false
    AllowedPattern: '^[a-z_]{2,31}$'
    AllowedValues:
      - mvrpl
      - mvrpl_br
      - mvrpl_dev
    ConstraintDescription: Must be a valid keyspace name.

Resources:
  MyKeyspace:
    Type: AWS::Cassandra::Keyspace
    Properties:
      KeyspaceName: !Ref KeyspaceName
      Tags:
        - Key: Environment
          Value: Dev
        - Key: Application
          Value: testes

  MyTable:
    Type: AWS::Cassandra::Table
    Properties:
      KeyspaceName: !Ref MyKeyspace
      TableName: pessoas
      PartitionKeyColumns:
        - ColumnName: id
          ColumnType: uuid
      ClusteringKeyColumns:
        - Column:
            ColumnName: criado_em
            ColumnType: DATE
          OrderBy: DESC
      RegularColumns:
        - ColumnName: nome
          ColumnType: TEXT
        - ColumnName: email
          ColumnType: TEXT
      BillingMode:
        Mode: ON_DEMAND
      DefaultTimeToLive: 60
      PointInTimeRecoveryEnabled: false
      ReplicaSpecifications:
        - Region: sa-east-1
      EncryptionSpecification:
        EncryptionType: AWS_OWNED_KMS_KEY
      Tags:
        - Key: Environment
          Value: Dev
        - Key: TablePurpose
          Value: usuarios

Outputs:
  KeyspaceName:
    Description: Keyspaces de Marcos
    Value: !Ref MyKeyspace
  TableName:
    Description: Tabela de usuários
    Value: !Ref MyTable
```

### Run bash to create OR update resource:
```bash
aws cloudformation deploy \
--stack-name MyKeyspacesStack \
--template-file ./keyspaces.cform \
--parameter-overrides KeyspaceName=mvrpl_dev \
--capabilities CAPABILITY_IAM
```

### Run bash to delete resource:
```bash
aws cloudformation delete-stack --stack-name MyKeyspacesStack
```

### Run spark-shell 3.5.x to populate data on keyspaces table:

Use this file as [application.conf](src/main/resources/keyspaces-application.conf)

```bash
spark-shell \
--packages "software.aws.mcs:aws-sigv4-auth-cassandra-java-driver-plugin:4.0.9,com.datastax.spark:spark-cassandra-connector_2.12:3.5.1" \
--files "application.conf" \
--conf "spark.cassandra.connection.config.profile.path=application.conf"
```

### Create dataframe and upsert in keyspaces table:

```scala
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

var df = spark.createDataFrame(spark.sparkContext.parallelize(Seq(
    Row(java.sql.Date.valueOf("2025-08-02"): java.sql.Date, "usera@gmail.com": String, "User A": String),
    Row(java.sql.Date.valueOf("2023-02-22"): java.sql.Date, "userb@gmail.com": String, "User B": String),
    Row(java.sql.Date.valueOf("1989-11-12"): java.sql.Date, "userc@gmail.com": String, "User C": String),
    Row(java.sql.Date.valueOf("2020-04-18"): java.sql.Date, "userd@gmail.com": String, "User D": String),
    Row(java.sql.Date.valueOf("2025-02-18"): java.sql.Date, "usere@gmail.com": String, "User E": String),
  ), 5),
  new StructType().add("criado_em", DateType, true)
    .add("email", StringType, true)
    .add("nome", StringType, true)
)

df = df.withColumn("id", uuid()) // Generate random user ID

(df.write
.format("org.apache.spark.sql.cassandra")
.options(Map("table" -> "pessoas", "keyspace" -> "mvrpl_dev"))
.mode("append")
.save())
```

### Run spring application e make this request:
```bash
curl --location 'http://localhost:8080/users'
```
