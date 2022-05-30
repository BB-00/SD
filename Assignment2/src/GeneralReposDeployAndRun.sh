echo "Transfering data to the general repository node."
sshpass -f password ssh sd106@l040101-ws08.ua.pt 'mkdir -p test/theRestaurant'
sshpass -f password ssh sd106@l040101-ws08.ua.pt 'rm -rf test/theRestaurant/*'
sshpass -f password scp dirGeneralRepos.zip sd106@l040101-ws08.ua.pt:test/theRestaurant
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd106@l040101-ws08.ua.pt 'cd test/theRestaurant ; unzip -uq dirGeneralRepos.zip'
echo "Executing program at the server general repository."
sshpass -f password ssh sd106@l040101-ws08.ua.pt 'cd test/theRestaurant/dirGeneralRepos ; java serverSide.main.GenReposMain 22159'
echo "Server shutdown."
sshpass -f password ssh sd106@l040101-ws08.ua.pt 'cd test/theRestaurant/dirGeneralRepos ; less log'
