echo "Transfering data to the table node."
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'mkdir -p test/theRestaurant'
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'rm -rf test/theRestaurant/*'
sshpass -f password scp dirTable.zip sd106@l040101-ws02.ua.pt:test/theRestaurant
echo "Decompressing data sent to the table node."
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'cd test/theRestaurant ; unzip -uq dirTable.zip'
echo "Executing program at the table node."
sshpass -f password ssh sd106@l040101-ws02.ua.pt 'cd test/theRestaurant/dirTable ; java serverSide.main.TableMain 22152 l040101-ws08.ua.pt 22159'
