echo "Transfering data to the registry node."
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password scp dirRegistry.zip sd106@l040101-ws10.ua.pt:test/Restaurant
echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'cd test/Restaurant ; unzip -uq dirRegistry.zip'
echo "Executing program at the registry node."
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'cd test/Restaurant/dirRegistry ; bash registry_com_d.sh sd106'