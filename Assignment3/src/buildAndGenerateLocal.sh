rm -rf dir*/*/
echo "Compiling source code."
javac -cp -source 8 -target 8 genclass.jar */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  RMI registry"
rm -rf dirRMIRegistry/interfaces dirRMIRegistry/commInfra
mkdir -p dirRMIRegistry/interfaces dirRMIRegistry/commInfra
cp interfaces/*.class dirRMIRegistry/interfaces
cp commInfra/*.class dirRMIRegistry/commInfra
echo "  Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces dirRegistry/commInfra
mkdir -p dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects dirRegistry/interfaces dirRegistry/commInfra
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces
cp commInfra/*.class dirRegistry/commInfra
echo "  General Repository of Information"
rm -rf dirGenRepos/serverSide dirGenRepos/clientSide dirGenRepos/interfaces dirGenRepos/commInfra
mkdir -p dirGenRepos/serverSide dirGenRepos/serverSide/main dirGenRepos/serverSide/objects dirGenRepos/interfaces \
         dirGenRepos/clientSide dirGenRepos/clientSide/entities dirGenRepos/commInfra
cp serverSide/main/GenReposMain.class dirGenRepos/serverSide/main
cp serverSide/objects/GenRepos.class dirGenRepos/serverSide/objects
cp interfaces/Register.class interfaces/GenReposInterface.class dirGenRepos/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/ChefStates.class clientSide/entities/StudentStates.class dirGenRepos/clientSide/entities
cp commInfra/*.class dirGenRepos/commInfra
echo "  Bar"
rm -rf dirBar/serverSide dirBar/clientSide dirBar/interfaces dirBar/commInfra
mkdir -p dirBar/serverSide dirBar/serverSide/main dirBar/serverSide/objects dirBar/interfaces \
         dirBar/clientSide dirBar/clientSide/entities dirBar/commInfra
cp serverSide/main/BarMain.class dirBar/serverSide/main
cp serverSide/objects/Bar.class serverSide/objects/Table.class serverSide/objects/Kitchen.class dirBar/serverSide/objects
cp interfaces/*.class dirBar/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/StudentStates.class clientSide/entities/ChefStates.class dirBar/clientSide/entities
cp commInfra/*.class dirBar/commInfra
echo "  Table"
rm -rf dirTable/serverSide dirTable/clientSide dirTable/interfaces dirTable/commInfra
mkdir -p dirTable/serverSide dirTable/serverSide/main dirTable/serverSide/objects dirTable/interfaces \
         dirTable/clientSide dirTable/clientSide/entities dirTable/commInfra
cp serverSide/main/TableMain.class dirTable/serverSide/main
cp serverSide/objects/Table.class dirTable/serverSide/objects
cp interfaces/*.class dirTable/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/StudentStates.class dirTable/clientSide/entities
cp commInfra/*.class dirTable/commInfra
echo "  Kitchen"
rm -rf dirKitchen/serverSide dirKitchen/clientSide dirKitchen/interfaces dirKitchen/commInfra
mkdir -p dirKitchen/serverSide dirKitchen/serverSide/main dirKitchen/serverSide/objects dirKitchen/interfaces \
         dirKitchen/clientSide dirKitchen/clientSide/entities dirKitchen/commInfra
cp serverSide/main/KitchenMain.class dirKitchen/serverSide/main
cp serverSide/objects/Kitchen.class dirKitchen/serverSide/objects
cp interfaces/*.class dirKitchen/interfaces
cp clientSide/entities/WaiterStates.class clientSide/entities/ChefStates.class dirKitchen/clientSide/entities
cp commInfra/*.class dirKitchen/commInfra
echo "  Chef"
rm -rf dirChef/serverSide dirChef/clientSide dirChef/interfaces dirChef/commInfra
mkdir -p dirChef/clientSide dirChef/clientSide/main dirChef/clientSide/entities \
         dirChef/interfaces dirChef/commInfra
cp clientSide/main/ChefMain.class dirChef/clientSide/main
cp clientSide/entities/Chef.class clientSide/entities/ChefStates.class dirChef/clientSide/entities
cp interfaces/*.class dirChef/interfaces
cp commInfra/*.class dirChef/commInfra
echo "  Waiter"
rm -rf dirWaiter/serverSide dirWaiter/clientSide dirWaiter/interfaces dirWaiter/commInfra
mkdir -p dirWaiter/clientSide dirWaiter/clientSide/main dirWaiter/clientSide/entities \
         dirWaiter/interfaces dirWaiter/commInfra
cp clientSide/main/WaiterMain.class dirWaiter/clientSide/main
cp clientSide/entities/Waiter.class clientSide/entities/WaiterStates.class dirWaiter/clientSide/entities
cp interfaces/*.class dirWaiter/interfaces
cp commInfra/*.class dirWaiter/commInfra
echo "  Student"
rm -rf dirStudents/serverSide dirStudents/clientSide dirStudents/interfaces dirStudents/commInfra
mkdir -p dirStudents/clientSide dirStudents/clientSide/main dirStudents/clientSide/entities \
         dirStudents/interfaces dirStudents/commInfra
cp clientSide/main/StudentMain.class dirStudents/clientSide/main
cp clientSide/entities/Student.class clientSide/entities/StudentStates.class dirStudents/clientSide/entities
cp interfaces/*.class dirStudents/interfaces
cp commInfra/*.class dirStudents/commInfra
echo "Compressing execution environments."
echo "  Genclass"
rm -f genclass.zip
zip -rq genclass.zip genclass.jar
echo "  RMI registry"
rm -f  dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry
echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry
echo "  General Repository of Information"
rm -f  dirGenRepos.zip
zip -rq dirGenRepos.zip dirGenRepos
echo "  Bar"
rm -f  dirBar.zip
zip -rq dirBar.zip dirBar
echo "  Kitchen"
rm -f  dirKitchen.zip
zip -rq dirKitchen.zip dirKitchen
echo "  Table"
rm -f  dirTable.zip
zip -rq dirTable.zip dirTable
echo "  Chef"
rm -f  dirChef.zip
zip -rq dirChef.zip dirChef
echo "  Waiter"
rm -f  dirWaiter.zip
zip -rq dirWaiter.zip dirWaiter
echo "  Student"
rm -f  dirStudents.zip
zip -rq dirStudents.zip dirStudents
echo "Deploying and decompressing execution environments."
mkdir -p ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
rm -rf ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/*
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirRegistry.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirGenRepos.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirBar.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirKitchen.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirTable.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirChef.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirWaiter.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirStudents.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cp dirRMIRegistry.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant
unzip -q genclass.zip
unzip -q dirRegistry.zip
unzip -q dirGenRepos.zip
unzip -q dirBar.zip
unzip -q dirKitchen.zip
unzip -q dirTable.zip
unzip -q dirWaiter.zip
unzip -q dirChef.zip
unzip -q dirStudents.zip
unzip -q dirRMIRegistry.zip
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirBar
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirKitchen
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirTable
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirWaiter
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirStudents
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirChef
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirGenRepos
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirRegistry
cp genclass.zip ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirRMIRegistry
cd dirBar
unzip -q genclass.zip
cd ../dirKitchen
unzip -q genclass.zip
cd ../dirTable
unzip -q genclass.zip
cd ../dirChef
unzip -q genclass.zip
cd ../dirWaiter
unzip -q genclass.zip
cd ../dirStudents
unzip -q genclass.zip
cd ../dirGenRepos
unzip -q genclass.zip
cd ../dirRegistry
unzip -q genclass.zip
cd ../dirRMIRegistry
unzip -q genclass.zip