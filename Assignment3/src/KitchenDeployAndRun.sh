echo "Transfering data to the kitchen node."
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'rm -rf test/Restaurant/*'
sshpass -f password scp dirKitchen.zip sd106@l040101-ws02.ua.pt:test/Restaurant
echo "Decompressing data sent to the kitchen node."
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'cd test/Restaurant ; unzip -uq dirKitchen.zip'
echo "Executing program at the kitchen node."
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'cd test/Restaurant/dirKitchen ; bash kitchen_com_d.sh sd106'