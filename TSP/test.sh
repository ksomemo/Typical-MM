#!/bin/sh

for i in `seq 1 10`
do
    echo "case:$i"
    java -jar tester.jar -exec ./a.out -seed $i -vis -save -num
    # java Tester -exec ./a.out -seed $i -vis -save -num
done