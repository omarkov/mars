# -*- Mode: Python -*-
#
# Copyright (c) 2004, Oliver Markovic <entrox@entrox.org> 
#   All rights reserved. 
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
#  o Redistributions of source code must retain the above copyright notice,
#    this list of conditions and the following disclaimer. 
#  o Redistributions in binary form must reproduce the above copyright
#    notice, this list of conditions and the following disclaimer in the
#    documentation and/or other materials provided with the distribution. 
#  o Neither the name of the author nor the names of the contributors may be
#    used to endorse or promote products derived from this software without
#    specific prior written permission. 
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.

# system modules
import socket
import thread
import threading
import time
import select

# our modules
import marshall
from command import *
import parameter
import module


class ConnectionError(Exception):
    pass

class ControllerConnection:
    def __init__(self):
	self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # -------------------------------------------------------------------------
    # Connecting/Disconnecting
    # -------------------------------------------------------------------------

    def open(self, host, port):
	print "; Opening connection to " + host + ":" + str(port) + "...",
	self.socket.connect((host, port))
	print "OK"

    def close(self):
	print "; Quitting"
	self.send("QUIT")
	self.socket.close()


    # -------------------------------------------------------------------------
    # Sending/Receiving
    # -------------------------------------------------------------------------

    def send(self, msg):
	characters_sent = 0
	msg += "\n<<END>>\n"
	while characters_sent < len(msg):
	    sent = self.socket.send(msg[characters_sent:])
	    if sent == 0:
		raise ConnectionError, "Connection broken during send."
	    characters_sent += sent


    def receive(self):
	msg = ''

	while True:
	    line = self.receive_line()
	    if line == "<<END>>":
		return msg[:-1] # snip off the final newline
	    else:
		msg += line + '\n'

    def receive_line(self):
	msg = ''

	# read until we get a newline
	while True:
	    chunk = self.socket.recv(1)

	    # this can't happen unless the socket has been closed
	    if chunk == '':
		raise ConnectionError, "Connection broken during recv."

	    # got a complete message?
	    if chunk == '\n':
		if msg != '':
		    return msg # yes, return
		else:
		    continue # no, discard this chunk

	    msg += chunk

    def wait_for_data(self):
	select.select([self.socket], [], [])

    def data_available(self):
	i, o, e = select.select([self.socket], [], [], 0)

	if i == []:
	    return False
	else:
	    return True


    # -------------------------------------------------------------------------
    # Helper functions
    # -------------------------------------------------------------------------

    def ok(self):
	self.send("OK")

    def error(self, msg = ""):
	# add a space between error and msg.. hurr!
	if msg != "":
	    msg = " " + msg

	self.send("ERROR" + msg)


class ControllerError(Exception):
    pass

class Controller(threading.Thread):
    def __init__(self):
	threading.Thread.__init__(self)
	self.setDaemon(True)
	self.lock = thread.allocate_lock()
	self.serial = 1
	self.send_connection = ControllerConnection()
	self.recv_connection = ControllerConnection()
	self.params = {}
	self.cmds = {}
	self.modules = {}
	self.running = False

    # -------------------------------------------------------------------------
    # Running
    # -------------------------------------------------------------------------

    def signon(self):
	print "; Initializing send connection"
	self.send_connection.open("localhost", 1234)
	self.perform_handshake(self.send_connection, True)

	print "; Initializing receive connection"
	self.recv_connection.open("localhost", 1234)
	self.perform_handshake(self.recv_connection, False)

	# get module list
	self.update_module_list()

	self.start()

    def signoff(self):
	self.send_connection.close()
	self.recv_connection.close()
	self.running = False

    def perform_handshake(self, connection, receiver):
	print "; Performing handshake...",

	# perform handshake
	connection.send("HELO 1.0 Mediacenter " + str(self.serial) + " " + marshall.marshall_bool(receiver))
	response = connection.receive()

	# check response for our hello
	if response == "OK":
	    pass
	elif response.find("ERROR") != -1:
	    raise ControllerError, "Error during handshake: " + response[6:]
	else:
	    raise ControllerError, "Illegal response during handshake"

	# expect next HELO
	response = connection.receive()
	if response[:4] != "HELO":
	    raise ControllerError, "Error during login: expected HELO, got " + response

	# alright, we're through the handshake
	connection.ok()
	print "OK"

    def run(self):
	command_table = {"BEGIN": self.handle_begin,
			 "DESCRIBE": self.handle_describe,
			 "GET": self.handle_get,
			 "SET": self.handle_set,
			 "QUIT": self.handle_quit,
			 "LIST": self.handle_list,
			 "CALL": self.handle_call,
			 "UNKNOWN": self.handle_unknown}

	self.running = True

	while(self.running):
	    self.recv_connection.wait_for_data()
	    self.lock.acquire()

	    if self.recv_connection.data_available():
		lines = self.recv_connection.receive().split('\n')

		if len(lines) > 0:
		    # dispatch to the appropriate handler
		    try:
			command_tag = lines[0].split()[0] # first token on the first line
			command_table[command_tag](lines)

			print "Received command " + command_tag
		    except KeyError:
			print "; Received unknown command: " + lines[0]
			self.recv_connection.send("UNKNOWN")

	    self.lock.release()



    # -------------------------------------------------------------------------
    # Handlers
    # -------------------------------------------------------------------------

    def handle_begin(self, lines):
	command = lines[0].split()

	if self.check_parameter_count(command, 4):
	    tag = command[1]
	    module_name = command[2]
	    module_serial = command[3]
	    web_module = marshall.unmarshall_bool(command[4])

	    if web_module == False:
		return

            print tag
	    # we only understand description
	    if tag != "DESCRIPTION":
		return

	    new_module = module.Module(module_name)

	    # parse out every line except the first one
	    try:
		it = iter(lines[1:])
		while(True):
		    current_line = it.next()
		    if current_line.find("BEGIN COMMAND") != -1:
			current_line = current_line.split()

			# parse header
			name = current_line[2]
			return_type = current_line[3]

			# parse body
			arg_types = []
			arg_names = []
			while current_line[0] != "END":
			    current_line = it.next().split()
			    arg_types.append(current_line[0])
			    arg_names.append(current_line[1])

			cmd = Command(name, return_type, arg_types, arg_names)
			new_module.add_command(cmd)
		    elif current_line.find("END DESCRIPTION") != -1:
			break
		    elif current_line == '\n':
			pass
		    else:
			new_module.add_parameter(self.parse_parameter(current_line))		    
	    except StopIteration:
		pass

	    self.modules[module_name] = new_module # check serial here?

    def handle_describe(self, lines):
	line = "BEGIN DESCRIPTION Mediacenter " + str(self.serial) + " " + marshall.marshall_bool(True) + '\n'

	# describe parameters
	for name, obj in self.params.iteritems():
	    line += obj.describe() + '\n'

	# describe commands
	for name, obj in self.cmds.iteritems():
	    line += obj.describe() + '\n'

	line += "END DESCRIPTION"

	self.recv_connection.send(line)

    def handle_get(self, lines):
	msg = lines[0].split()

	if self.check_parameter_count(msg, 1):
	    try:
		param = self.params[msg[1]]
		self.recv_connection.send(marshall.marshall(param.value))
	    except KeyError:
		self.recv_connection.error("Unknown parameter " + msg[1] + ".")

    def handle_set(self, lines):
	msg = lines[0].split()

	print "SET: " + str(msg)

	if self.check_parameter_count(msg, 2):
	    try:
		modulename = msg[1]
		param = self.params[msg[2]]

		if param.type == 'LIST':
		    param.remote_set("".join(lines[1:]))
		else:
		    param.remote_set(lines[1][0])

		self.recv_connection.send("true")
	    except KeyError:
		self.recv_connection.error("Unknown parameter " + msg[1] + ".")

    def handle_list(self, lines):
	self.recv_connection.send("Mediacenter")

    def handle_call(self, lines):
	msg = lines[0].split('<<PARAMSEP>>')

	command = msg[0].split()
	params = msg[1]

	module_name = command[1]
	fn_name = command[2]

	try:
	    self.cmds[fn_name].call(params)
	    self.recv_connection.send("TRUE")
        
	except KeyError:
	    self.recv_connection.error("Unknown command " + fn_name)

    def handle_quit(self, lines):
	self.running = False

    def handle_unknown(self, lines):
	# skip
	return


    # -------------------------------------------------------------------------
    # Parameter and Command registration
    # -------------------------------------------------------------------------

    def register_parameter(self, param):
	self.params[param.name] = param

    def register_command(self, cmd):
	self.cmds[cmd.name] = cmd


    # -------------------------------------------------------------------------
    # Module handling
    # -------------------------------------------------------------------------

    def update_module_list(self):
	print "; Updating module list...",

	# get the list from the server
	self.send_connection.send("LIST")
	module_list = self.send_connection.receive().split('\n')

	# get all descriptions
	for module in module_list:
	    self.send_connection.send("DESCRIBE " + module)

	    # dirty
	    self.handle_begin(self.send_connection.receive().split('\n'))

	print "OK"



    # -------------------------------------------------------------------------
    # Remote call (used by Command.call)
    # -------------------------------------------------------------------------

    # def remote_call(self, command, args):
# 	self.lock.acquire()
# 	self.connection.send("CALL " + command.module.name + " " + command.name + " " + " ".join(args))
# 	retval = self.connection.receive()
# 	self.connection.socket.recv(1)
# 	self.lock.release()

# 	if retval.find("ERROR") != -1:
# 	    raise ControllerError, "Remote call errored: " + retval[6:]
# 	elif retval.find("UNKNOWN") != -1:
# 	    raise ControllerError, "Unknown remote command called. How did this happen?"

# 	return marshall.unmarshall(command.return_type, retval)

    # -------------------------------------------------------------------------
    # Remote parameter setting (used by Parameter.remote_set)
    # -------------------------------------------------------------------------

    def remote_set(self, parameter, value):
	self.lock.acquire()
	self.send_connection.send("SET " + command.module_name + " " + parameter.name + "\n" + marshall.marshall(value))
	retval = self.send_connection.receive()
	self.lock.release()

	return marshall.unmarshall_bool(retval)

    # -------------------------------------------------------------------------
    # Helper functions
    # -------------------------------------------------------------------------

    def check_parameter_count(self, msg, count):
	if len(msg)-1 != count:
	    self.send_connection.error("Need exactly " + str(count) + " " +
				       "parameters, got " + str(len(msg)-1) + ".")
	    return False
	return True


    def parse_parameter(self, str):
	msg = str.split()
	type_table = {"BOOL": (parameter.BooleanParameter, 4),
		      "STRING": (parameter.StringParameter, 4),
		      "NUMERIC": (parameter.NumericParameter, 7),
		      "LIST": (parameter.ListParameter, 6) }

	try:
	    parameter_class, count = type_table[msg[0]]

	    # check argument count
	    if len(msg) != count:
		self.send_connection.error(msg[0] + " need exactly " + str(count) + " arguments.")
		return

	    param = parameter_class()
	    param.parse(msg)
	except KeyError:
	    self.send_connection.error("Unknown parameter type: " + msg[0])
	    raise RuntimeError, "parse error " + msg[0]

	return param


controller_instance = None

def get_controller():
    global controller_instance
    if controller_instance == None:
	controller_instance = Controller()

    return controller_instance

def test_handler(parm):
    print "Handler!"

def test():
    c = get_controller()

    c.register_parameter(parameter.NumericParameter("foo", "rw", 0))
    c.signon()
