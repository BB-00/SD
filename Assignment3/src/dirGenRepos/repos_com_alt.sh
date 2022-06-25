CODEBASE="file:///home/"$1"/test/Restaurant/dirGenRepos/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.GenReposMain 22153 localhost 22150