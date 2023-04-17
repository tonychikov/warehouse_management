# warehouse_management

### Task

At krieger we manage the company’s warehouses.
Each warehouse is identified by a unique 3-digit code, name and address.
Inside a warehouse we have the bays, where the goods are stored. Bays are organized on rows
like:

Bird view of the warehouse 

Row 1

|     |      |     |     |      |     |
|-----|------|-----|-----|------|-----|

Row 2

|     |      |     |     |      |     |
|-----|------|-----|-----|------|-----|

Front view of a bay row

|       |       |       |       |       |       |       |       |       |        |
|-------|-------|-------|-------|-------|-------|-------|-------|-------|--------|
| 1.3.1 | 1.3.2 | 1.3.3 | 1.3.4 | 1.3.5 | 1.3.6 | 1.3.7 | 1.3.8 | 1.3.9 | 1.3.10 |
| 1.2.1 | 1.2.2 | 1.2.3 | 1.2.4 | 1.2.5 | 1.2.6 | 1.2.7 | 1.2.8 | 1.2.9 | 1.2.10 |
| 1.1.1 | 1.1.2 | 1.1.3 | 1.1.4 | 1.1.5 | 1.1.6 | 1.1.7 | 1.1.8 | 1.1.9 | 1.1.10 |

Each rectangle on the picture is a bay, identified by 3 attributes (row number, shelf number, level
number). This combination is unique per warehouse. Besides this, each bay has the following
attributes:
* Type: tell us if this bay allows for a pallet or a cart to be stored, mandatory
* Holding points: tell us how many pallets or carts can be stored (minimum is 1, maximum
is 9), mandatory
* How many holding points are busy or free
* Tags: a list of words to optimize the search. Each tag is unique per bay, cannot be
empty, and should be all lower case.

In order to support those requirements, we need a RESTful API to CRUD the warehouse and
the bays, as a SpringBoot application.
Please also include sample curl commands or Swagger Docs, so we can see the API working.
Feel free to upload the solution to github or gitlab, or to send it as a zip file.

### How to start the application
``` bash
mvn clean spring-boot:run
# or
./mvnw clean spring-boot:run
```
The system creates 3 demo warehouses and many bays allocated to them with different settings.

### Sample commands

I recommend using Postman to look at the API. The Postman collection is imported and attached to the project.
Just import the collection in your Postman account. https://learning.postman.com/docs/getting-started/importing-and-exporting-data/

There are  simple Curl command below. Since I use Windows to develop the solution, all quotes are escaped by backslash

#### List of warehouses
The command just shows the list of all warehouses
```bash
curl -v localhost:9000/warehouses
```

#### A warehosue by warehouse code
The command returns the data of the certain warehouse
```bash
curl -v localhost:9000/warehouses/000
```

#### Add a new warehouse
Allow to add a new warehouse
```bash
curl -i -X POST localhost:9000/warehouses -H 'Content-type:application/json' -d '{\"code\": \"011\",\"name\": \"München Süd\",\"address\": \"Berlinerstr. 1, 10984 München\"}'
```

#### Replace existing warehouse
The command allows to update the existing warehouse. If warehouse is not found it returns exception.
```bash
curl -i -X PUT localhost:9000/warehouses/001 -H 'Content-type:application/json' -d '{\"name\": \"München Süd2\", \"address\": \"Berlinerstr. 1, 10984 München\"}'
```

#### Delete a warehouse
It deletes warehouse and all allocated bays at all. The system is checked if the occupied points exist (!= 0).
If so the command returns an exception.
```bash
curl -i -X DELETE localhost:9000/warehouses/000
```

#### List of all bays
It returns the list of all bays by all warehouses
```bash
curl -v localhost:9000/bays
```

#### List of all bays by warehouse
Run it to get the list fof bays allocated to the certain warehouse
```bash
curl -v localhost:9000/warehouses/001/bays
```

#### One bay by warehouse and row, and shelf, and level
it returns the certain bay.
```bash
curl -v localhost:9000/warehouses/001/bays/1/1/1
```

#### Add a new bay
Adding a new bay to the certain warehouse
```bash
curl -i -X POST localhost:9000/warehouses/001/bays -H 'Content-type:application/json' -d '{\"rowNumber\": 9,\"shelfNumber\": 1,\"levelNumber\": 1,\"type\": \"CART\",\"holdingPoints\": 8,\"occupiedPoints\": 1,\"tags\": [\"row_1\",\"row_2\",\"row_3\"]}'
```

#### Replace a bay
The command is allows to update the bay
```bash
curl -i -X PUT localhost:9000/warehouses/001/bays -H 'Content-type:application/json' -d '{\"rowNumber\": 9,\"shelfNumber\": 1,\"levelNumber\": 1,\"type\": \"CART\",\"holdingPoints\": 8,\"occupiedPoints\": 1,\"tags\": [\"row_1\",\"row_2\",\"row_10\"]}'
```

#### Delete a bay
It deletes bay at all. The system is checked if the occupied points exist (!= 0).
If so the command returns an exception.
```bash
curl -i -X DELETE localhost:9000/warehouses/001/bays/1/1/1
```



