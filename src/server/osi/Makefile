INCLUDES = -I/usr/lib/j2sdk1.5-sun/include -I/usr/lib/j2sdk1.5-sun/include/linux
CFLAGS = -fPIC
LDFLAGS = -lcrypt
TARGET = libLinuxUserManager.so

all:
	gcc -shared $(LDFLAGS) $(INCLUDES) $(CFLAGS) -o $(TARGET) LinuxUserManager.c
	cp $(TARGET) ../../../native

clean:
	rm -f $(TARGET)
