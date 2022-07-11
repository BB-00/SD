cd src
javadoc -cp genclass.jar ./*/*.java -d ../javadoc
cd ../javadoc
google-chrome index.html
