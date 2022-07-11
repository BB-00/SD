echo "Transfering data to the bar node."
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'rm -rf test/Restaurant/*'
sshpass -f password scp dirBar.zip sd106@l040101-ws03.ua.pt:test/Restaurant
echo "Decompressing data sent to the bar node."
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'cd test/Restaurant ; unzip -uq dirBar.zip'
echo "Executing program at the bar node."
sshpass -f password ssh sd106@l040101-ws03.ua.pt 'cd test/Restaurant/dirBar ; bash bar_com_d.sh sd106'