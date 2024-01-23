mvn clean package
rc=$?
if [[ $rc -eq 0 ]] ; then
  docker build --no-cache -t configserver:0.0.1 .
fi
