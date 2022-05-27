cd /home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/
javac -cp /home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar clientSide.main.ChefMain.java


#javac /.java /*/.java
 gnome-terminal --title="GenReposMain" -- java -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar serverSide.main.GenReposMain 22169 
 gnome-terminal --title="BarMain" -- java -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar serverSide.main.BarMain 22160 127.0.0.1 22169 
 gnome-terminal --title="KitchenMain" -- java  -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar serverSide.main.KitchenMain 22161 127.0.0.1 22169 
 gnome-terminal --title="TableMain" -- java -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar serverSide.main.TableMain 22162 127.0.0.1 22169 
  
 sleep 1 
 gnome-terminal --title="WaiterMain" -- java -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar clientSide.main.WaiterMain 127.0.0.1 22161 127.0.0.1 22160 127.0.0.1 22162 127.0.0.1 22169 
 gnome-terminal --title="ChefMain" -- java -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar clientSide.main.ChefMain 127.0.0.1 22161 127.0.0.1 22160 127.0.0.1 22169 
 sleep 1 
 gnome-terminal --title="StudentMain" -- java -cp .:/home/bernardo/Documents/Ano4/SD/Praticas/SD/Assignment2/src/genclass.jar clientSide.main.StudentMain 127.0.0.1 22160 127.0.0.1 22162 127.0.0.1 22169
