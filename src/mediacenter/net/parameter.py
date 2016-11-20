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

from marshall import *


class Parameter:
    def __init__(self):
	raise RuntimeError, "Can't instantiate generic Parameter"

    def parse(self, msg):
	self.name = unmarshall_string(msg[1])
	self.default = unmarshall_string(msg[2])
	self.value = self.default
	self.flags = msg[3]

    def remote_set(self, value):
	self.value = unmarshall(self.type, value)

    def get(self):
	return self.value

    def describe(self):
	return ""


class BooleanParameter(Parameter):
    def __init__(self, name = "unknown", flags = "rw", default = False):
	self.name = name
	self.flags = flags
	self.default = default
	self.value = self.default
	self.type = "BOOL"

    def set(self, value):
	if type(value) != bool:
	    raise TypeError

	self.value = value

    def remote_set(self, value):
	self.value = unmarshall(self.type, value);

    def describe(self):
	s = self.type + " "
	s += marshall(self.name) + " "
	s += marshall(self.default) + " "
	s += self.flags

	return s


class NumericParameter(Parameter):
    def __init__(self, name = "unknown", flags = "rw", default = 0):
	self.name = name
	self.flags = flags
	self.default = default
	self.value = self.default
	self.type = "NUMERIC"

	# initialize range to signed int
	self.min = -(2 ** 30)
	self.max = (2 ** 30)
	self.step = 1

    def parse(self, msg):
    	self.name = unmarshall_string(msg[1])
	self.default = unmarshall_numeric(msg[2])
	self.value = self.default
	self.flags = msg[3]
	self.min = unmarshall_numeric(msg[4])
	self.max = unmarshall_numeric(msg[5])
	self.step = unmarshall_numeric(msg[6])

    def set(self, value):
	if type(value) != int:
	    raise TypeError

	self.value = value

    def remote_set(self, value):
	i = unmarshall(self.type, value)

	# check bounds
	if (i < self.min) or (i > self.max):
	    raise RuntimeError, "Out of bounds"

	# check stepping
	if ((i - self.min) % self.step):
	    raise RuntimeError, "Stepping error"

	self.value = i

    def describe(self):
	s = self.type + " "
	s += marshall(self.name) + " "
	s += marshall(self.default) + " "
	s += self.flags + " "
	s += marshall(self.min) + " "
	s += marshall(self.max) + " "
	s += marshall(self.step)

	return s


class StringParameter(Parameter):
    def __init__(self, name = "unknown", flags = "rw", default = ""):
	self.name = name
	self.flags = flags
	self.default = default
	self.value = self.default
	self.type = "STRING"

    def set(self, value):
	if type(value) != string:
	    raise TypeError

	self.value = value

    def remote_set(self,value):
	self.value = unmarshall(self.type, value)

    def describe(self):
	s = self.type + " "
	s += marshall(self.name) + " "
	s += marshall(self.default) + " "
	s += self.flags

	return s


class ListParameter(Parameter):
    def __init__(self, name = "unknown", flags = "rw", default = 0, list_type = "generic"):
	self.name = name
	self.flags = flags
	self.default = default
	self.value = self.default
	self.type = "LIST"
	self.list_type = list_type

    def set(self, value):
	if type(value) != list:
	    raise TypeError

	self.value = value

    def remote_set(self, value):
	self.value = unmarshall(self.type, value)

    def parse(self, msg):
	self.name = unmarshall_string(msg[1])
	self.default = unmarshall_numeric(msg[2])
	self.value = self.default
	self.flags = msg[3]
	self.list_type = msg[4];

    def describe(self):
	s = self.type + " "
	s += self.name + " "
	s += marshall(self.default) + " "
	s += self.flags + " "
	s += self.list_type + " "
	s += "java.lang.String"

	return s
