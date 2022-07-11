echo "Transfering data to the general repository node."
sshpass -f password ssh sd106@l040101-ws01.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password ssh sd106@l040101-ws01.ua.pt 'rm -rf test/Restaurant/*'
sshpass -f password scp dirGenRepos.zip sd106@l040101-ws01.ua.pt:test/Restaurant
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd106@l040101-ws01.ua.pt 'cd test/Restaurant ; unzip -uq dirGenRepos.zip'
echo "Executing program at the general repository node."
sshpass -f password ssh sd106@l040101-ws01.ua.pt 'cd test/Restaurant/dirGenRepos ; bash repos_com_d.sh sd106'
echo "Server shutdown."
sshpass -f password ssh sd106@l040101-ws01.ua.pt 'cd test/Restaurant/dirGenRepos ;'