echo "Transfering data to the kitchen node."
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'mkdir -p test/theRestaurant'
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'rm -rf test/theRestaurant/*'
sshpass -f password scp dirKitchen.zip sd106@l040101-ws03.ua.pt:test/theRestaurant
echo "Decompressing data sent to the table node."
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'cd test/theRestaurant ; unzip -uq dirKitchen.zip'
echo "Executing program at the table node."
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'cd test/theRestaurant/dirKitchen ; java serverSide.main.KitchenMain 22151 l040101-ws08.ua.pt 22159'
