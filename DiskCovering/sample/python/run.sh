TESTER=../../tester/Tester.jar

for i in `seq 1 10`; do 
    echo "case:$i"
    java -jar $TESTER -exec "python DiskCovering.py" -seed $i -vis -save
done
