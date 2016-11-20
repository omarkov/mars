#!/bin/sh


case "$1" in
    start)
	./server.sh start &&
	./ops.sh start &&
	./mars_ir.sh start &&
	./light.sh start
	;;

    stop)
	./light.sh stop
	./mars_ir.sh stop
	./ops.sh stop
	./server.sh stop
	;;

    restart)
	;;
    *)
	echo "Usage: mars.sh {start|stop|restart}"
	exit 1
	;;
esac

exit 0
