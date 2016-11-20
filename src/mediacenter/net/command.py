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

import marshall
import controller


class Command:
    def __init__(self, name, return_type, arg_types, arg_names, handler = None):
	self.name = name
	self.return_type = return_type
	self.arg_types = arg_types
	self.arg_names = arg_names
	self.handler = handler

	# arg_types and arg_names must correlate 1:1
	if len(arg_types) != len(arg_names):
	    raise RuntimeError, "Number of argument types and names must be equal."

	# remote or local?
	if handler == None:
	    self.remote = True
	else:
	    self.remote = False


    def describe(self):
	str = "BEGIN COMMAND" + " " + self.name + " " + self.return_type + '\n'

	for type, name in zip(self.arg_types, self.arg_names):
	    str += type + " " + name + '\n'

	str += "END COMMAND"

	return str


    def call(self, *args):
	if self.remote:
	    # check argument count
	    if len(args) != len(self.arg_names):
		raise TypeError, self.name + " takes exactly " + str(len(self.arg_names))\
		    + " arguments (" + str(len(args)) + ") given"

	    # construct argument list
	    argument_list = []
	    type_table = {"BOOL": bool, "STRING": str, "NUMERIC": int, "LIST": list}

	    for i in range(len(args)):
		marshalled_type = type_table[self.arg_types[i]]

		# check type
		if type(args[i]) != marshalled_type:
		    raise TypeError, "Argument " + str(i) + " must be "\
			+ str(marshalled_type) + ", but is " + str(type(args[i]))

		argument_list.append(marshall.marshall(args[i]))

	    # do the call
	    return controller.get_controller().remote_call(self, argument_list)
	else:
	    argument_list = []

        if len(args) == len(self.arg_types):
	    for i in range(len(args)):
		argument_list.append(marshall.unmarshall(self.arg_types[i], args[i]))

	return self.handler(*argument_list)
