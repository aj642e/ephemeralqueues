ngrok https --url=sunfish-sacred-dogfish.ngrok-free.app 8080


curl --location --request POST 'https://5ca6-73-219-87-79.ngrok-free.app/queue';

curl --location --request POST 'https://5ca6-73-219-87-79.ngrok-free.app/queue/0/addition/3';

curl --location --request POST 'https://5ca6-73-219-87-79.ngrok-free.app/queue/0/addition/4';

curl --location --request POST 'https://5ca6-73-219-87-79.ngrok-free.app/queue/0/addition/5';

curl --location 'https://5ca6-73-219-87-79.ngrok-free.app/polling/0';

curl --location 'https://5ca6-73-219-87-79.ngrok-free.app/polling/0';

curl --location 'https://5ca6-73-219-87-79.ngrok-free.app/polling/0';

curl --location --request DELETE 'https://5ca6-73-219-87-79.ngrok-free.app/queue/0';
