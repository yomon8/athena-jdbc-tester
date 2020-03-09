# athena-jdbc-tester
Athena JDBC Connection Tester



## Required Variables

Name|Desc
:--|:--
REGION |AWS Region
SQL|Executed in Athena
OUTPUT_LOCATION|Athena will save query result on this location
LOGLEVEL| 0(disable) - 6(trace)

## Optional Variables

Name|Desc
:--|:--
WORKGROUP|Set Athena workgroup name


## Example 

```sh
$ docker run -it --rm \
    -e REGION=ap-northeast-1 \
    -e SQL="SELECT * FROM \"test_db\".\"test_tab\" limit 10;" \
    -e OUTPUT_LOCATION="s3://your-output-bucket/" \
    -e WORKGROUP=yourworkgroup \
    yomon8/athena-jdbc-tester
```
