###############################################################################
##### Makefile                                                             ####
###############################################################################

JAVAC  = javac
TARGET = SlidingPuzzle
TESTER = ../../tester/Tester.jar

.PHONY: all clean run

###############################################################################

all: $(TARGET)

$(TARGET): $(TARGET).java
	$(JAVAC) $^

clean:
	rm -f *.class

run:
	for i in `seq 1 10`; do \
		echo "case:$$i"; \
		java -jar $(TESTER) -exec "java $(TARGET)" -seed $$i -vis -delay 0.1; \
	done
