echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'rm -rf test/Restaurant/*'
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'mkdir -p Public/classes/commInfra'
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'rm -rf Public/classes/commInfra/*'
sshpass -f password scp dirRMIRegistry.zip sd106@l040101-ws10.ua.pt:test/Restaurant
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'cd test/Restaurant ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'cd test/Restaurant/dirRMIRegistry ; cp interfaces/*.class /home/sd106/Public/classes/interfaces ; cp commInfra/*.class /home/sd106/Public/classes/commInfra ; cp set_rmiregistry_d.sh /home/sd106'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd106@l040101-ws10.ua.pt 'bash set_rmiregistry_d.sh sd106 22150'
