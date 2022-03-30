# DedMorozPodarki

There are two REST services impletemented.

GiftService responsible for handling gifts and processing mails.
ProductionService responsible for producing new gifts.

Gift service expose following endpoints
1. GET localhost:8080/gifts - this return information about all available gifts and theier quantity
2. GET localhost:8080/gifts/<kind> - return information about gifts of particular kind
3. POST localhost:8080/mails/process - processes mail.
   Body should be a json in following format (middle name is optional):
   {
    "firstName": "Tommy",
    "middleName": "Lee",
    "secondName": "Johnson",
    "giftKind": "phone"
   }
4. POST localhost:8080/gifts/<kind>/populate - increase quantity of gifts of given kind
   Body should be a json in the following format:
   {
    "increment": 5
   }
5. GET localhost:8080/deliveries - lists information about all deliveries that was made
  
ProductionService expose following endpoints
1. POST localhost:8081/produce - starts production for gift of requested kind
   Body should be a json in the following format:
   {
    "kind": "car"
   }
