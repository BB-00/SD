echo "Transfering data to the students node."
sshpass -f password ssh sd106@l040101-ws07.ua.pt 'mkdir -p test/Restaurant'
sshpass -f password ssh sd106@l040101-ws07.ua.pt 'rm -rf test/Restaurant/*'
sshpass -f password scp dirStudents.zip sd106@l040101-ws07.ua.pt:test/Restaurant
echo "Decompressing data sent to the students node."
sshpass -f password ssh sd106@l040101-ws07.ua.pt 'cd test/Restaurant ; unzip -uq dirStudents.zip'
echo "Executing program at the students node."
sshpass -f password ssh sd106@l040101-ws07.ua.pt 'cd test/Restaurant/dirStudents ; bash student_com_d.sh'