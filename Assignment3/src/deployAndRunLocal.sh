xterm  -T "RMI registry" -hold -e " cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirRMIRegistry; ./set_rmiregistry_alt.sh 22150" &
sleep 5
xterm  -T "Registry" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirRegistry; ./registry_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &
sleep 3
xterm  -T "General Repository" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirGenRepos; ./repos_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &
sleep 2
xterm  -T "Kitchen" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirKitchen; ./kitchen_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &
xterm  -T "Table" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirTable; ./table_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &
sleep 2
xterm  -T "Bar" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirBar; ./bar_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &
sleep 2
xterm  -T "Chef" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirChef; ./chef_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &
xterm  -T "Waiter" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirWaiter; ./waiter_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &
xterm  -T "Student" -hold -e "cd ~/Documents/Ano4/SD/Praticas/SD/Assignment3/test/Restaurant/dirStudent; ./student_com_alt.sh bernardo/Documents/Ano4/SD/Praticas/SD/Assignment3" &