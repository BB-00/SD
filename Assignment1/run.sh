cd /home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment1/src/
javac -cp /home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment1/src/genclass.jar ./*/*.java

for i in $(seq 1 10)
do
echo -e "\nRun n.o " $i
java -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment1/src/genclass.jar main.Main >> output.txt
done
